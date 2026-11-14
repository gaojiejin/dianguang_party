package com.example.spb.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.*;

@Service
public class LearningReportService implements LearningReports {
 private final JdbcTemplate db; private final ClassLearnInfoService learning;
 public LearningReportService(JdbcTemplate db,ClassLearnInfoService learning){this.db=db;this.learning=learning;}
 @Transactional
 public String submit(String reportId,int user,int course,int chapter,int completed,int seconds){
  if(reportId==null||!reportId.matches("[A-Za-z0-9_-]{8,80}")||seconds<0||(completed!=0&&completed!=1))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"学习记录参数无效");
  Integer valid=db.queryForObject("SELECT COUNT(*) FROM chapter WHERE chapter_id=? AND class_id=?",Integer.class,chapter,course);
  if(valid==null||valid==0)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"章节不存在");
  int inserted=db.update("INSERT IGNORE INTO learning_report(report_id,user_id,class_id,chapter_id,if_learned,learn_duration) VALUES(?,?,?,?,?,?)",reportId,user,course,chapter,completed,seconds);
  if(inserted==0){Map<String,Object> old=db.queryForMap("SELECT * FROM learning_report WHERE report_id=? LOCK IN SHARE MODE",reportId);
   if(((Number)old.get("user_id")).intValue()!=user||((Number)old.get("class_id")).intValue()!=course||((Number)old.get("chapter_id")).intValue()!=chapter||((Number)old.get("if_learned")).intValue()!=completed||((Number)old.get("learn_duration")).intValue()!=seconds)throw new ResponseStatusException(HttpStatus.CONFLICT,"学习记录标识重复，内容不一致");
   return Objects.toString(old.get("result"),"学习记录已保存");}
  String result=learning.addLearnDuration(course,chapter,user,completed,seconds);
  db.update("UPDATE learning_report SET result=? WHERE report_id=?",result,reportId);return result;
 }
 public List<Map<String,Object>> progress(int user){
  String role=db.queryForObject("SELECT role FROM user WHERE id=?",String.class,user);
  int authority="积极分子".equals(role)?1:0;
  List<Map<String,Object>> rows=db.queryForList("SELECT c.class_id,COUNT(ch.chapter_id) total,SUM(CASE WHEN COALESCE(l.done,0)=1 THEN 1 ELSE 0 END) finished FROM class c LEFT JOIN chapter ch ON ch.class_id=c.class_id LEFT JOIN (SELECT chapter_id,MAX(ifLearned) done FROM learn_rate WHERE user_id=? GROUP BY chapter_id) l ON l.chapter_id=ch.chapter_id WHERE c.authority=? GROUP BY c.class_id",user,authority);
  List<Map<String,Object>> result=new ArrayList<>();for(Map<String,Object> row:rows){Map<String,Object> item=new HashMap<>();int total=((Number)row.get("total")).intValue();int finished=((Number)row.get("finished")).intValue();item.put("classId",row.get("class_id"));item.put("total",total);item.put("finished",finished);item.put("completed",total>0&&finished==total);result.add(item);}return result;
 }
}



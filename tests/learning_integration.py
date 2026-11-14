"""Local integration checks. Uses only an isolated disposable test user."""
import os,json,subprocess,urllib.request,urllib.parse,urllib.error,uuid,concurrent.futures
from pathlib import Path
MYSQL=r'D:/mysql/install/MySQL Server 8.0/bin/mysql.exe'
def sql(statement):
 env=dict(os.environ,MYSQL_PWD=os.environ.get('DB_PASSWORD','root'))
 p=subprocess.run([MYSQL,'--user='+os.environ.get('DB_USERNAME','root'),'--default-character-set=utf8mb4','--database=dianguang_party','--batch','--skip-column-names'],input=statement,encoding='utf-8',capture_output=True,env=env,check=True)
 return p.stdout.strip()
def api(path,data=None):
 req=urllib.request.Request('http://127.0.0.1:8080/'+path,data=urllib.parse.urlencode(data).encode() if data is not None else None)
 with urllib.request.urlopen(req,timeout=30) as response:
  text=response.read().decode();return json.loads(text) if text[:1] in '[{' else text
checks=[];job='dg_check_'+uuid.uuid4().hex[:12];ids=[]
try:
 for account,password in [('admin','admin'),('branchadmin','Demo123!'),('member','Demo123!'),('activist','Demo123!')]:
  assert api('login/jobIDLogin',{'job_id':account,'password':password}).get('token');checks.append(account+' login')
 sql("INSERT INTO user(job_id,password,name,role) VALUES('%s','test','refactor-test','党员');"%job)
 user=int(sql("SELECT id FROM user WHERE job_id='%s';"%job));course,chapter=map(int,sql("SELECT class_id,chapter_id FROM chapter WHERE title='学思结合：从日常行动开始' ORDER BY chapter_id LIMIT 1;").split('\t'))
 base={'user_id':user,'class_id':course,'chapter_id':chapter,'if_learned':0,'learn_duration':13,'report_id':'check_'+uuid.uuid4().hex};ids.append(base['report_id'])
 api('classLearnInfo/addLearnDuration',base);api('classLearnInfo/addLearnDuration',base)
 assert int(sql('SELECT learning_duration FROM learn_rate WHERE user_id=%s AND chapter_id=%s;'%(user,chapter)))==13;checks.append('duplicate report counted once')
 concurrent_payload={**base,'learn_duration':17,'report_id':'check_'+uuid.uuid4().hex};ids.append(concurrent_payload['report_id'])
 with concurrent.futures.ThreadPoolExecutor(max_workers=4) as pool:list(pool.map(lambda _:api('classLearnInfo/addLearnDuration',concurrent_payload),range(4)))
 assert int(sql('SELECT learning_duration FROM learn_rate WHERE user_id=%s AND chapter_id=%s;'%(user,chapter)))==30;checks.append('four concurrent retries counted once')
 try:api('classLearnInfo/addLearnDuration',{**base,'learn_duration':99});raise AssertionError('expected conflict')
 except urllib.error.HTTPError as e:assert e.code==409
 checks.append('report payload conflict rejected')
 invalid={**base,'chapter_id':-1,'report_id':'check_'+uuid.uuid4().hex};ids.append(invalid['report_id'])
 try:api('classLearnInfo/addLearnDuration',invalid);raise AssertionError('expected invalid chapter')
 except urllib.error.HTTPError as e:assert e.code==404
 assert sql("SELECT COUNT(*) FROM learning_report WHERE report_id='%s';"%invalid['report_id'])=='0';checks.append('invalid chapter leaves no report')
 rows=api('learning/course-progress?user_id='+str(user));assert any(int(row['classId'])==course for row in rows);checks.append('role-filtered course progress')
 for account in ['member','activist']:
  u=api('user/queryByJobID?job_id='+account);rows=api('learning/course-progress?user_id='+str(u['id']));expected=1 if account=='activist' else 0
  for row in rows:assert int(sql('SELECT authority FROM class WHERE class_id=%s;'%row['classId']))==expected
 checks.append('member and activist course isolation')
 old={**base,'report_id':'check_'+uuid.uuid4().hex,'learn_duration':0};del old['report_id'];api('classLearnInfo/addLearnDuration',old);checks.append('legacy client remains compatible')
 print(json.dumps({'passed':checks},ensure_ascii=False,indent=2))
finally:
 if 'user' in locals():sql('DELETE FROM learning_report WHERE user_id=%s;DELETE FROM learn_rate WHERE user_id=%s;DELETE FROM class_learn_info WHERE user_id=%s;DELETE FROM user WHERE id=%s;'%(user,user,user,user))


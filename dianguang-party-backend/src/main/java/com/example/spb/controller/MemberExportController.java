package com.example.spb.controller;

import com.example.spb.utils.AliOSSUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class MemberExportController {
    private final AliOSSUtils storage;
    public MemberExportController(AliOSSUtils storage) { this.storage = storage; }
    @PostMapping("/exportMembers")
    public Map<String,Object> exportMembers(@RequestBody List<Map<String,Object>> members) throws IOException {
        try (XSSFWorkbook book = new XSSFWorkbook()) {
            Sheet sheet = book.createSheet("支部成员");
            String[] fields = {"name", "branchName", "role"};
            String[] titles = {"姓名", "支部", "角色"};
            Row header = sheet.createRow(0);
            for (int i=0;i<3;i++) { header.createCell(i).setCellValue(titles[i]); sheet.setColumnWidth(i, 28*256); }
            for(int i=0;i<members.size();i++) {
                Row row=sheet.createRow(i+1);
                for(int j=0;j<3;j++) row.createCell(j).setCellValue(Objects.toString(members.get(i).get(fields[j]), ""));
            }
            Map<String,Object> result = new HashMap<>();
            result.put("success",true);
            result.put("fileUrl",storage.uploadWorkbook(book));
            return result;
        }
    }
}

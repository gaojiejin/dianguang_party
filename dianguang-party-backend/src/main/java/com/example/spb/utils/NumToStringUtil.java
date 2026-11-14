package com.example.spb.utils;


import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.stereotype.Component;

@Component
public class NumToStringUtil {

    public static String convertNumToDate(int seconds){
        // 计算总小时数，秒数除以3600
        double hours = seconds / 3600.0;

        // 保留一位小数
        hours = Math.round(hours * 10) / 10.0;

        // 返回格式化后的字符串，形如 "xx小时"
        return String.format("%.1f小时", hours);
    }
}

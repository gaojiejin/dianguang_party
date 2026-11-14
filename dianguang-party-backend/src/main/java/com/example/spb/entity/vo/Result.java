package com.example.spb.entity.vo;

import com.example.spb.enums.ResponseCodeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private ResponseCodeEnum code;
    private String msg;
    private Object data;
    private String stringData;

    public static Result succ(Object data) {
        return succ(ResponseCodeEnum.OK, "操作成功", data);
    }
    public static Result succ() {
        return succ(ResponseCodeEnum.OK,"操作成功");
    }

    public static Result fail(String msg) {
        return fail(ResponseCodeEnum.BAD_REQUEST, msg, null);
    }

    public static Result succ (ResponseCodeEnum responseCodeEnum, String msg) {
        Result result = new Result();
        result.setCode(responseCodeEnum);
        result.setMsg(msg);
        return result;
    }
    public static Result succ (ResponseCodeEnum responseCodeEnum, String msg, Object data) {
        Result result = new Result();
        result.setCode(responseCodeEnum);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result fail (ResponseCodeEnum responseCodeEnum, String msg, Object data) {
        Result result = new Result();
        result.setCode(responseCodeEnum);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}

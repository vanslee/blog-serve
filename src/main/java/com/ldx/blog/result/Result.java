package com.ldx.blog.result;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Uaena
 * @date 2023/5/16 22:29
 */
@ToString
@Data
public class Result<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    private long timestamp ;

    public Result (){
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> Result<T> success(T data){
        Result Res = new Result();
        Res.setCode(ResultCodeEnum.SUCCESS.getCode());
        Res.setMsg(ResultCodeEnum.SUCCESS.getMessage());
        Res.setData(data);
        return  Res;
    }
    public static <T> Result<T> success(ResultCodeEnum msg, T data) {
        return baseCreate(msg.getCode(), msg.getMessage(), true, data);
    }

    public static Result fail(Integer code, String msg) {
        return baseCreate(code, msg, false, false);
    }
    public static <T> Result<T> fail(ResultCodeEnum codeEnum){
        return fail(codeEnum.getCode(), codeEnum.getMessage());
    }
    public static Result fail() {
        return fail(ResultCodeEnum.UNKNOWN_ERROR);
    }

    private static <T> Result<T> baseCreate(Integer code, String msg, boolean success, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setTimestamp(System.currentTimeMillis());
        result.setData(data);
        return result;
    }
}

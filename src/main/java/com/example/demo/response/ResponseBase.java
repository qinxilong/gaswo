package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 统一返回格式
 *
 * @author lipei01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBase {

    /**
     * 状态码
     */
    private int code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 请求成功
     *
     * @return ResponseBase
     */
    public static ResponseBase ok() {
        return new ResponseBase(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 操作成功
     *
     * @return ResponseBase
     */
    public static ResponseBase success() {
        return new ResponseBase(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 请求失败
     *
     * @param errorMsg 错误信息
     * @return ResponseBase
     */
    public static ResponseBase fail(String errorMsg) {
        return new ResponseBase(HttpStatus.BAD_REQUEST.value(), errorMsg);
    }

    /**
     * 异常处理
     *
     * @param exceptionResult 异常信息
     * @return ResponseBase
     */
//    public static ResponseBase exception(ExceptionResult exceptionResult) {
//        return new ResponseBase(exceptionResult.getCode(), exceptionResult.getMessage());
//    }
}

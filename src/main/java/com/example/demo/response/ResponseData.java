package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData extends ResponseBase {

    /**
     * 返回数据集
     */
    private Object data;

    public ResponseData(int code, String message, Object data) {
        super(code, message);
        this.data = data;
    }

    /**
     * 请求成功
     *
     * @param data 返回结果集
     * @return
     */
    public static ResponseBase ok(Object data) {
        return new ResponseData(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    /**
     * 操作成功
     *
     * @return
     */
    public static ResponseBase success() {
        return new ResponseData(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);
    }

    /**
     * 请求失败
     *
     * @param errorMsg 错误信息
     * @return
     */
    public static ResponseBase fail(String errorMsg) {
        return new ResponseData(HttpStatus.BAD_REQUEST.value(), errorMsg, null);
    }

    /**
     * 无法找到内容
     *
     * @return
     */
    public static ResponseBase noContent() {
        return new ResponseData(HttpStatus.NO_CONTENT.value(), "没返回值", null);
    }
}

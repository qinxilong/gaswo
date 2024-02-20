package com.example.demo.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * @author lipei01
 */
@Data
@NoArgsConstructor
public class ResponsePage extends ResponseData {

    /**
     * 总记录数
     */
    private Long total;

    public ResponsePage(int code, String message, List<?> data, long total) {
        super(code, message, data);
        this.total = total;
    }

    /**
     * 请求成功
     *
     * @param data  返回结果集
     * @param total
     * @return
     */
    public static ResponsePage ok(List<?> data, long total) {
        return new ResponsePage(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data, total);
    }

    /**
     * 请求失败
     *
     * @param errorMsg 错误信息
     * @return
     */
    public static ResponsePage fail(String errorMsg) {
        return new ResponsePage(HttpStatus.BAD_REQUEST.value(), errorMsg, null, 0);
    }
}

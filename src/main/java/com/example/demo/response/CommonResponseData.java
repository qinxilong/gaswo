package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lipei01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseData<T> extends ResponseBase {
    private T data;
}

package com.example.demo.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBasePageParams {

    /**
     * 当前页码，从1开始
     */
    @NotNull
    private Integer page;

    /**
     * 分页大小
     */
    @NotNull
    private Integer size;

}

package com.iqeq.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.iqeq.util.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Response {

    private int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status = CommonConstants.SUCCESS_MSG;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object[] parameters;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
}

package com.tsq.utils;

import lombok.Data;

@Data
public class R {
    private Boolean success=true;
    private Object data;

    public static R data(Object data){
        R r = new R();
        r.setData(data);
        return r;
    }
}

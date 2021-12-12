package com.huajuan.stafftrainingsystembackend.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 我的http响应的数据对象
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyHttpResponseObj {
    private String timestamp;
    private int status;
    private String message;

    public MyHttpResponseObj(int status, String message) {
        this.timestamp = new Date().toString();
        this.status = status;
        this.message = message;
    }
}

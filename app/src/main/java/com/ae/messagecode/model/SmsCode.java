package com.ae.messagecode.model;

import org.litepal.crud.DataSupport;

/**
 * Created by AE on 2015/12/26.
 */
public class SmsCode extends DataSupport {
    private int id;
    private String code;
    private String sender;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

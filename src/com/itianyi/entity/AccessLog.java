package com.itianyi.entity;

import java.util.Date;

/**
 * 访问日志实体类
 *
 * @author
 * @create 2018-06-04 15:43
 **/
public class AccessLog {
    private int id;
    private String ip;
    private Date accessTime;
    private String webname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public String getWebname() {
        return webname;
    }

    public void setWebname(String webname) {
        this.webname = webname;
    }


}

package com.itianyi;

import com.itianyi.entity.AccessLog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//GitHub测试
@WebServlet(name = "AccessLogAction")
public class AccessLogAction extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ip = getLocalIp(request);
        Date date = new Date();
        String webname = request.getParameter("webname");
        System.out.println(webname);
        DBHelper dbHelper = new DBHelper();
        System.out.println(dbHelper);
        List<Object> accessLogs = new ArrayList<>();
        accessLogs.add(ip);
        accessLogs.add(webname);
        accessLogs.add(date);
        dbHelper.doUpdate("insert into access_log (ip,webname,accesstime) values(?,?,?)", accessLogs);
    }


    public static String getLocalIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String forwarded = request.getHeader("X-Forwarded-For");
        String realIp = request.getHeader("X-Real-IP");

        String ip = null;
        if (realIp == null) {
            if (forwarded == null) {
                ip = remoteAddr;
            } else {
                ip = remoteAddr + "/" + forwarded.split(",")[0];
            }
        } else {
            if (realIp.equals(forwarded)) {
                ip = realIp;
            } else {
                if (forwarded != null) {
                    forwarded = forwarded.split(",")[0];
                }
                ip = realIp + "/" + forwarded;
            }
        }
        return ip;
    }
}

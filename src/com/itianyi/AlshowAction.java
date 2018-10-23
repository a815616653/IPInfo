package com.itianyi;

import com.itianyi.entity.AccessLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 展示访问记录
 */
@WebServlet(name = "AlshowAction")
public class AlshowAction extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String webname = request.getParameter("webname");
        String page=request.getParameter("page");
        String rows=request.getParameter("rows");
        DBHelper dbHelper = new DBHelper();
        List<Object> objects = new ArrayList<>();
        objects.add(webname);
        List<Map<String, Object>> list = dbHelper.findMulitObject("select * from access_log where webname=?", objects);
        List<AccessLog> accessLogs= new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            AccessLog accessLog = new AccessLog();
            accessLog.setId(Integer.parseInt(map.get("id").toString()));
            accessLog.setIp(map.get("ip").toString());
            SimpleDateFormat simpleDateForma= new SimpleDateFormat("yyyy-MM-ss HH:ss:mm");
            Date date = null;
            try {
                date = simpleDateForma.parse(map.get("accesstime").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            accessLog.setAccessTime(date);
            accessLog.setWebname(webname);
            accessLogs.add(accessLog);
        }
        Map<String,Object> map = new HashMap();
         map.put("rows",accessLogs);
         map.put("total",accessLogs.size());

        PrintWriter out =null ;
        out =response.getWriter() ;
        out.write(toNetSFJson(map));
        out.close();
    }
    private static String toNetSFJson(Object obj) {
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class,
                new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
        config.registerJsonValueProcessor(Timestamp.class,
                new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
        String objStr = net.sf.json.JSONObject.fromObject(obj, config)
                .toString();
        return objStr;
    }
}

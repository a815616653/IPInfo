<%--
  Created by IntelliJ IDEA.
  User: TONY
  Date: 2018/6/4
  Time: 15:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
    <link rel="stylesheet" type="text/css" href="static/easyui/themes/default/easyui.css">
    <script type="text/javascript" src="static/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="static/easyui/jquery.easyui.min.js"></script>

</head>
<body>
<table id="dd"></table>
</body>
<script>
    $(function () {
        dg = $('#dd').datagrid({
            method: "get",
            url: '/alshow?webname=kxc',
            fit: true,
            fitColumns: true,
            border: false,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'id', title: 'id', sortable: true},
                {field: 'ip', title: 'IP', sortable: true, width: 100},
                {field: 'accessTime', title: '访问时间', sortable: true, width: 100
                },
                {field: 'webname', title: 'webname', sortable: true
                },
            ]]

        });
    })


</script>
</html>

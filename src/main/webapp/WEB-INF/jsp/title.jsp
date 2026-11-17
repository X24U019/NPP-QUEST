<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>タイトル - NPPクエスト</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/title.css?v=2">
</head>
<body onclick="location.href='<%= request.getContextPath() %>/LoadServlet'">
    <div class="start-container">
        <div class="start-msg">ぼうけんをはじめる</div>
        <div class="sub-msg">※画面のどこかをクリックしてください</div>
    </div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>ロード中 - NPPクエスト</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/load.css?v=2">
    <script>
        setTimeout(function() {
            location.href = '<%= request.getContextPath() %>/LoginServlet';
        }, 3000);
    </script>
</head>
<body>
    <div class="loading-container">
        <h2>NOW LOADING...</h2>
        <div class="loading-character"></div>
    </div>
</body>
</html>
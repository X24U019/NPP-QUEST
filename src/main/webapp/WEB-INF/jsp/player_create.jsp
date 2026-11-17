<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>新規アカウント作成 - NPPクエスト</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css?v=2">
</head>
<body>
    <div class="login-container">
        <%-- エラー表示 --%>
        <% String errorMsg = (String) request.getAttribute("errorMsg"); %>
        <% if (errorMsg != null) { %>
            <div class="error-msg"><%= errorMsg %></div>
        <% } %>

        <form action="<%= request.getContextPath() %>/PlayerCreateServlet" method="post">
            <div class="form-group">
                <span class="form-label">ユーザーID</span>
                <input type="text" name="id" class="form-input" required placeholder="例: hero123" autocomplete="off">
            </div>

            <div class="form-group">
                <span class="form-label">プレイヤー名</span>
                <input type="text" name="name" class="form-input" required placeholder="例: アーサー" autocomplete="off">
            </div>
            
            <div class="form-group">
                <span class="form-label">🔑 パスワード</span>
                <input type="password" name="password" class="form-input" required placeholder="半角英数字">
            </div>

            <div class="submit-wrapper">
                <button type="submit" class="submit-btn">この内容で冒険を始める</button>
            </div>
        </form>

        <div class="link-box">
            <a href="<%= request.getContextPath() %>/LoginServlet">◀ ログイン画面に戻る</a>
        </div>
    </div>
</body>
</html>
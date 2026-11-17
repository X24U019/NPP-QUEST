<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>ログイン - NPPクエスト</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css?v=2">
</head>
<body>
    <div class="login-container">
        <%-- エラー表示 --%>
        <% String errorMsg = (String) request.getAttribute("errorMsg"); %>
        <% if (errorMsg != null) { %>
            <div class="error-msg"><%= errorMsg %></div>
        <% } %>

        <form action="<%= request.getContextPath() %>/LoginServlet" method="post">
            <div class="form-group">
                <span class="form-label">🪪 ID</span>
                <input type="text" name="id" class="form-input" required autocomplete="off">
            </div>

            <div class="form-group">
                <span class="form-label">🔑 パスワード</span>
                <input type="password" name="password" class="form-input" required>
            </div>

            <div class="option-group">
                <label>
                    <input type="checkbox" name="remember"> ログイン情報を記憶する
                </label>
            </div>

            <button type="submit" class="submit-btn">ゲームスタート</button>
        </form>

        <div class="link-box">
            <a href="<%= request.getContextPath() %>/PlayerCreateServlet">▶ 新規アカウント作成はこちら</a>
        </div>
    </div>
</body>
</html>
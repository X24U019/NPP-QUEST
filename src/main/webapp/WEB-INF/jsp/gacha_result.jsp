<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.GachaItem" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>ガチャ結果</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/gacha_result.css?v=2">
</head>
<body>
    <div class="result-wrapper">
        <h1 class="result-title">ガチャリザルト</h1>

        <%
            List<GachaItem> resultList = (List<GachaItem>) request.getAttribute("resultList");
            int size = (resultList != null) ? resultList.size() : 0;
            String containerClass = (size == 1) ? "result-single" : "result-multi";
        %>

        <!-- 結果表示エリア -->
        <div class="result-container <%= containerClass %>">
            <% if (resultList != null) {
                for (GachaItem item : resultList) { 
                    
                    String rarityClass = "";
                    if (item.getRarity() == 3) {
                        rarityClass = " super-rare-anim";
                    }
            %>
                    <div class="result-item rarity-<%= item.getRarity() %><%= rarityClass %>">
                        <img src="${pageContext.request.contextPath}/img/<%= item.getImageFileName() %>" alt="<%= item.getName() %>">
                        <p class="item-name"><%= item.getName() %></p>
                    </div>
            <%  }
            } %>
        </div>

        <button class="back-btn" onclick="location.href='GachaServlet'">ガチャ画面に戻る</button>
    </div>
</body>
</html>
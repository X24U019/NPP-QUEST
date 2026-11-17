<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>ガチャ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/gacha.css?v=2">
</head>
<body>
    <div class="gacha-container">
        <button class="back-btn" onclick="location.href='MapServlet'">戻る</button>
        <div class="coin-balance">
            <img src="${pageContext.request.contextPath}/img/coin.png" alt="C" class="coin-icon-small">
            <span class="coin-amount">${loginUser != null ? loginUser.gold : 0}</span>
        </div>
        <!-- 左側：宝箱画像エリア -->
        <div class="gacha-left">
            <div class="treasure-box-placeholder">
                <img src="${pageContext.request.contextPath}/img/treasure_box.png" alt="宝箱" class="animated-image">
            </div>
        </div>

        <!-- 右側：テキストおよびボタンエリア -->
        <div class="gacha-right">
            <h1 class="gacha-title">宝箱</h1>
            <p class="gacha-description">
                ノーマル、レア<br>
                スーパーレア<br>
                装備を入手できます
            </p>
            
            <!-- 右下：ガチャボタン配置 -->
            <div class="button-container">
                <button type="button" class="gacha-btn btn-single" onclick="executeGacha(1, 100)">
                    <div class="btn-times">1回</div>
                    <div class="btn-cost"><img src="${pageContext.request.contextPath}/img/coin.png" alt="C" class="coin-icon"> ×100</div>
                </button>
                <button type="button" class="gacha-btn btn-multi" onclick="executeGacha(10, 1000)">
                    <div class="btn-times">10回</div>
                    <div class="btn-cost"><img src="${pageContext.request.contextPath}/img/coin.png" alt="C" class="coin-icon"> ×1000</div>
                </button>
            </div>
        </div>
    </div>

    <script>
        function executeGacha(times, cost) {
            const currentGold = ${loginUser != null ? loginUser.gold : 0};

            if (currentGold < cost) {
                alert("資金不足です");
                return;
            }

            location.href = 'GachaExecuteServlet?times=' + times;
        }
    </script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Player"%>
<%@ page import="model.Map_Funabashi"%>
<%@ page import="model.Map_Ichikawa"%>
<%
Player player = (Player) session.getAttribute("loginUser");

if (player == null) {
	response.sendRedirect("LoginServlet");
	return;
}

int mapId = player.getMapId();
int posX = player.getPosX();
int posY = player.getPosY();

int[][] currentMapData;
String mapJsName;

if (mapId == 2) {
	currentMapData = Map_Ichikawa.MAP;
	mapJsName = "map_ichikawa.js";
} else {
	currentMapData = Map_Funabashi.MAP;
	mapJsName = "map_hunabashi.js";
}
%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>探索マップ - NPP QUEST</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/map.css">
<link rel="icon" href="data:,">
</head>
<body>



	<div class="game-container">
		<!-- ヘッダーUI -->
		<div class="header-container">
			<span class="game-title">NPP QUEST</span>
			<div class="status-bar">
				座標: (X: <span id="posX"><%=posX%></span>, Y: <span id="posY"><%=posY%></span>)
			</div>

			<!-- メニューボタンとドロップダウン -->
			<div class="menu-container">
				<button id="menuBtn" class="menu-btn">メニュー ≡</button>
				<div id="dropdownMenu" class="dropdown-menu hidden">
					<ul>
						<li><a href="#"
							onclick="alert('ステータス画面は準備中です'); return false;">ステータス</a></li>
						<li><a href="#" onclick="alert('装備画面は準備中です'); return false;">装備</a></li>
						<li><a href="<%=request.getContextPath()%>/InventoryServlet">アイテム</a></li>
						<li><a href="<%=request.getContextPath()%>/GachaServlet">ガチャ</a></li>
						<li><a href="#" onclick="alert('ミニゲームは準備中です'); return false;">ミニゲーム</a></li>
						<hr>
						<!-- ログアウトは直接Servletへリンク -->
						<li><a href="<%=request.getContextPath()%>/LogoutServlet"
							class="logout-link">ログアウト</a></li>
					</ul>
				</div>
			</div>
		</div>

		<!-- カメラ追従 キャンバス -->
		<div class="canvas-wrapper">
			<canvas id="gameCanvas" width="1024" height="640"></canvas>
		</div>

		<!-- アバター（左下・四角形）と名前表示 -->
		<div class="avatar-wrapper">
			<div class="avatar-name"><%=player.getName()%></div>
			<div class="avatar-display">
				<img src="<%=request.getContextPath()%>/img/hero_face.png"
					alt="プレイヤー">
			</div>
		</div>

		<!-- スマホ横持ち用 仮想十字キー -->
		<div id="dpadContainer" class="dpad-container">
			<div class="dpad-row">
				<button type="button" class="dpad-btn" data-dir="up" aria-label="上">▲</button>
			</div>
			<div class="dpad-row">
				<button type="button" class="dpad-btn" data-dir="left"
					aria-label="左">◀</button>
				<div class="dpad-center"></div>
				<button type="button" class="dpad-btn" data-dir="right"
					aria-label="右">▶</button>
			</div>
			<div class="dpad-row">
				<button type="button" class="dpad-btn" data-dir="down"
					aria-label="下">▼</button>
			</div>
		</div>

		<div class="instructions">【操作】 矢印キー（↑ ↓ ← →）または WASD キーで移動</div>

		<!-- ワープ先選択メニュー（初期はhiddenで非表示） -->
		<div id="warpMenu" class="warp-menu-overlay hidden">
			<div class="warp-menu-box">
				<p>どこに移動しますか？</p>
				<!-- 引数は (マップID, 移動先X, 移動先Y) -->
				<button id="btnToIchikawa" onclick="warpTo(2, 25, 6)">市川へ行く</button>
				<button id="btnToFunabashi" onclick="warpTo(1, 35, 6)">船橋へ行く</button>
				<button onclick="alert('タイは現在準備中です！')">タイへ行く</button>
				<button onclick="closeWarpMenu()">やめる</button>
			</div>
		</div>
	</div>

	<script>
        // 動的に切り替わったマップ配列をJSに渡す
        const mapData = <%=java.util.Arrays.deepToString(currentMapData)%>;
        let playerX = <%=posX%>;
        let playerY = <%=posY%>;
        const contextPath = "<%=request.getContextPath()%>";

        // 今いるマップのIDをJSに教える
        const currentMapId = <%=mapId%>;
	</script>

	<!-- マップ専用のJSを動的に読み込む -->
	<script src="<%=request.getContextPath()%>/js/<%=mapJsName%>"></script>

	<!-- 共通の操作・描画スクリプト -->
	<script src="<%=request.getContextPath()%>/js/map.js"></script>
</body>
</html>
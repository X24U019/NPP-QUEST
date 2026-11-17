<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.GachaItem"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>インベントリ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inventory.css?v=6">
</head>
<body>
    <div class="inventory-container">

        <!-- 上部エリア：戻るボタン・表示順ボタン・タイトル・所持Gold -->
        <div class="header-area">
            <div class="header-left">
                <button type="button" class="back-btn" onclick="location.href='${pageContext.request.contextPath}/MapServlet'">マップへ戻る</button>
                
                <!-- 表示順メニュー -->
                <div class="sort-wrapper" id="sortWrapper">
                    <button type="button" class="sort-main-btn" id="sortMainBtn" onclick="toggleSortMenu()">表示順 ▼</button>
                    
                    <div class="sort-dropdown" id="sortDropdown" style="display: none;">
                        <div class="sort-step">
                            <button type="button" class="sort-cat-btn" id="catRarityBtn" onclick="selectSortCategory('rarity')">レア度</button>
                            <button type="button" class="sort-cat-btn" id="catOrderBtn" onclick="selectSortCategory('order')">取得順</button>
                        </div>
                        <div class="sort-step sort-sub-step" id="sortOrderStep" style="display: none;">
                            <button type="button" class="sort-dir-btn" onclick="applySort('asc')">昇順</button>
                            <button type="button" class="sort-dir-btn" onclick="applySort('desc')">降順</button>
                        </div>
                    </div>
                </div>
            </div>

            <h2 class="inventory-title">インベントリ</h2>

            <!-- 右上：現在の所持Gold表示 -->
            <div class="header-right">
                <div class="gold-display">
                    <img src="${pageContext.request.contextPath}/img/coin.png" alt="G" class="gold-icon">
                    <span>${loginUser != null ? loginUser.gold : 0}</span>
                </div>
            </div>
        </div>

        <!-- 売却命令を送るための隠しフォーム -->
        <form id="sellForm" action="ItemDeleteServlet" method="post">
            <input type="hidden" name="action" id="sellAction">
            <input type="hidden" name="boxId" id="sellBoxId">
            <input type="hidden" name="rarityTypeId" id="sellRarityTypeId">
        </form>

        <!-- メインエリア（左：格納アイテム一覧 ／ 右：アイテム詳細） -->
        <div class="main-area">
            
            <!-- 【左側】格納アイテムエリア -->
            <div class="left-panel">
                <div class="item-grid" id="itemGrid">
                    <%
                    List<GachaItem> inventoryList = (List<GachaItem>) request.getAttribute("inventoryList");

                    int totalSlots = 30;
                    if (inventoryList != null && inventoryList.size() > 30) {
                        totalSlots = inventoryList.size();
                    }

                    for (int i = 0; i < totalSlots; i++) {
                        if (inventoryList != null && i < inventoryList.size()) {
                            GachaItem item = inventoryList.get(i);
                            
                            String power = (item.getPower() != null && !item.getPower().isEmpty()) 
                                           ? item.getPower() : "0";
                            String skill = (item.getSkill() != null && !item.getSkill().isEmpty()) 
                                           ? item.getSkill() : "スキル無し";
                            String desc  = (item.getDescription() != null && !item.getDescription().isEmpty()) 
                                           ? item.getDescription() : "詳細情報はありません。";
                            
                            String escapedName  = item.getName().replace("&", "&amp;").replace("\"", "&quot;");
                            String escapedPower = power.replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;");
                            String escapedSkill = skill.replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;");
                            String escapedDesc  = desc.replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;");
                    %>
                    <div class="item-slot grid-item rarity-<%= item.getRarity() %>"
                         data-box-id="<%= item.getBoxId() %>"
                         data-name="<%= escapedName %>"
                         data-rarity="<%= item.getRarity() %>"
                         data-stars="<%= item.getRarityStars() %>"
                         data-img="${pageContext.request.contextPath}/img/<%= item.getImageFileName() %>"
                         data-power="<%= escapedPower %>"
                         data-skill="<%= escapedSkill %>"
                         data-desc="<%= escapedDesc %>"
                         onclick="selectItem(this)">
                        
                        <img src="${pageContext.request.contextPath}/img/<%= item.getImageFileName() %>"
                             alt="<%= escapedName %>" class="item-img">
                        
                        <!-- 武器の下側に星を表示 -->
                        <div class="item-stars"><%= item.getRarityStars() %></div>
                    </div>
                    <%
                        } else {
                    %>
                    <div class="item-slot empty-slot"></div>
                    <%
                        }
                    }
                    %>
                </div>

                <!-- 左パネル下部：一括売却メニュー -->
                <div id="bulkSellMenu" class="bulk-sell-area">
                    <div id="rarityButtons" style="display: none;">
                        <button type="button" class="rarity-sell-btn r1" onclick="executeBulkSell(1)">ノーマル売却 (10G)</button>
                        <button type="button" class="rarity-sell-btn r2" onclick="executeBulkSell(2)">レア売却 (50G)</button>
                        <button type="button" class="rarity-sell-btn r3" onclick="executeBulkSell(3)">Sレア売却 (500G)</button>
                    </div>
                    <button type="button" class="bulk-btn" onclick="toggleRarityButtons()">一括売却</button>
                </div>
            </div>

            <!-- 【右側】アイテム詳細エリア -->
            <div class="right-panel">
                
                <!-- 初期表示：本の表紙画像 -->
                <div id="bookCoverView" class="book-cover-view">
                    <img src="${pageContext.request.contextPath}/img/book_cover.png" alt="武器図鑑" class="book-cover-img">
                    <p class="book-hint">左の武器を選択すると詳細が表示されます</p>
                </div>

                <!-- 武器選択時：詳細表示カード（上から power → skill → description の順） -->
                <div id="itemDetailView" class="item-detail-view" style="display: none;">
                    <div class="detail-header">
                        <div id="detailIconBox" class="detail-icon-box">
                            <img id="detailImg" src="" alt="" class="detail-img">
                            <div id="detailStars" class="item-stars"></div>
                        </div>
                        <h3 id="detailName" class="detail-name">武器名</h3>
                    </div>

                    <hr class="detail-divider">

                    <!-- 1. power -->
                    <div class="detail-row">
                        <span class="detail-label">POWER</span>
                        <span id="detailPower" class="detail-value">0</span>
                    </div>

                    <!-- 2. skill -->
                    <div class="detail-row">
                        <span class="detail-label">SKILL</span>
                        <span id="detailSkill" class="detail-value">スキル無し</span>
                    </div>

                    <!-- 3. description -->
                    <div class="detail-desc-box">
                        <div class="detail-desc-label">DESCRIPTION</div>
                        <div id="detailDesc" class="detail-desc"></div>
                    </div>

                    <!-- 個別売却ボタン -->
                    <div class="detail-footer">
                        <button type="button" id="singleSellBtn" class="single-sell-btn" onclick="executeSingleSell()">売却</button>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <script>
        let selectedBoxId = null;
        let selectedItemName = "";
        let selectedItemPrice = 0;
        let currentSortCategory = null;

        // レアリティごとの売却価格
        function getSellPrice(rarity) {
            if (rarity === 1) return 10;
            if (rarity === 2) return 50;
            if (rarity === 3) return 500;
            return 0;
        }

        // --- 表示順メニューの開閉 ---
        function toggleSortMenu() {
            const dropdown = document.getElementById('sortDropdown');
            if (dropdown.style.display === 'none') {
                dropdown.style.display = 'flex';
            } else {
                closeSortMenu();
            }
        }

        function closeSortMenu() {
            document.getElementById('sortDropdown').style.display = 'none';
            document.getElementById('sortOrderStep').style.display = 'none';
            document.getElementById('catRarityBtn').classList.remove('active');
            document.getElementById('catOrderBtn').classList.remove('active');
            currentSortCategory = null;
        }

        function selectSortCategory(category) {
            currentSortCategory = category;
            document.getElementById('catRarityBtn').classList.toggle('active', category === 'rarity');
            document.getElementById('catOrderBtn').classList.toggle('active', category === 'order');
            document.getElementById('sortOrderStep').style.display = 'flex';
        }

        function applySort(direction) {
            if (!currentSortCategory) return;

            const grid = document.getElementById('itemGrid');
            const items = Array.from(grid.querySelectorAll('.grid-item'));
            const emptySlots = Array.from(grid.querySelectorAll('.empty-slot'));

            items.sort(function(a, b) {
                const rarityA = parseInt(a.dataset.rarity, 10);
                const rarityB = parseInt(b.dataset.rarity, 10);
                const boxIdA = parseInt(a.dataset.boxId, 10);
                const boxIdB = parseInt(b.dataset.boxId, 10);

                let diff = 0;
                if (currentSortCategory === 'rarity') {
                    diff = (direction === 'asc') ? (rarityA - rarityB) : (rarityB - rarityA);
                    if (diff === 0) {
                        diff = boxIdA - boxIdB;
                    }
                } else if (currentSortCategory === 'order') {
                    diff = (direction === 'asc') ? (boxIdA - boxIdB) : (boxIdB - boxIdA);
                }
                return diff;
            });

            grid.innerHTML = '';
            items.forEach(function(item) { grid.appendChild(item); });
            emptySlots.forEach(function(slot) { grid.appendChild(slot); });

            const catLabel = (currentSortCategory === 'rarity') ? 'レア度' : '取得順';
            const dirLabel = (direction === 'asc') ? '昇順' : '降順';
            document.getElementById('sortMainBtn').textContent = '表示順: ' + catLabel + '(' + dirLabel + ') ▼';

            closeSortMenu();
        }

        document.addEventListener('click', function(event) {
            const sortWrapper = document.getElementById('sortWrapper');
            if (sortWrapper && !sortWrapper.contains(event.target)) {
                closeSortMenu();
            }
        });

        // --- 武器スロットをクリックした時の処理 ---
        function selectItem(element) {
            document.querySelectorAll('.grid-item').forEach(el => el.classList.remove('selected'));
            element.classList.add('selected');

            selectedBoxId = element.dataset.boxId;
            selectedItemName = element.dataset.name;
            const rarity = parseInt(element.dataset.rarity, 10);
            selectedItemPrice = getSellPrice(rarity);

            const stars  = element.dataset.stars;
            const imgSrc = element.dataset.img;
            const power  = element.dataset.power;
            const skill  = element.dataset.skill;
            const desc   = element.dataset.desc;

            document.getElementById('bookCoverView').style.display = 'none';
            document.getElementById('itemDetailView').style.display = 'flex';

            document.getElementById('detailName').textContent  = selectedItemName;
            document.getElementById('detailImg').src           = imgSrc;
            document.getElementById('detailStars').textContent = stars;
            document.getElementById('detailPower').textContent = power;
            document.getElementById('detailSkill').textContent = skill;
            document.getElementById('detailDesc').textContent  = desc;

            // 売却ボタンに獲得Goldを表示
            document.getElementById('singleSellBtn').textContent = '売却 (' + selectedItemPrice + 'G)';

            const iconBox = document.getElementById('detailIconBox');
            iconBox.className = 'detail-icon-box rarity-' + rarity;
        }

        // --- 個別売却の実行 ---
        function executeSingleSell() {
            if (selectedBoxId != null && confirm("「" + selectedItemName + "」を " + selectedItemPrice + "G で売却しますか？")) {
                document.getElementById("sellAction").value = "single";
                document.getElementById("sellBoxId").value = selectedBoxId;
                document.getElementById("sellForm").submit();
            }
        }

        // --- 一括売却のメニュー開閉 ---
        function toggleRarityButtons() {
            const btnDiv = document.getElementById("rarityButtons");
            btnDiv.style.display = (btnDiv.style.display === "none") ? "inline-flex" : "none";
        }

        // --- 一括売却の実行 ---
        function executeBulkSell(rarityId) {
            const targetItems = document.querySelectorAll('.grid-item[data-rarity="' + rarityId + '"]');
            const count = targetItems.length;

            if (count === 0) {
                alert("売却できる対象レアリティのアイテムがありません。");
                return;
            }

            const totalGold = count * getSellPrice(rarityId);
            const rarityName = (rarityId === 1) ? "ノーマル(☆)" : (rarityId === 2) ? "レア(☆☆)" : "スーパーレア(☆☆☆)";

            if (confirm(rarityName + " " + count + "個 を合計 " + totalGold + "G で一括売却しますか？")) {
                document.getElementById("sellAction").value = "bulk";
                document.getElementById("sellRarityTypeId").value = rarityId;
                document.getElementById("sellForm").submit();
            }
        }
    </script>
</body>
</html>
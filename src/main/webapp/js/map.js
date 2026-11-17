// --- 基本設定 ---
const TILE_SIZE = 32;
const VIEW_COLS = 32;       // 画面に表示する横マス数
const VIEW_ROWS = 20;       // 画面に表示する縦マス数

const MAP_ROWS = mapData.length;
const MAP_COLS = mapData[0].length;

// ワープメニュー表示中の操作ロック用フラグ
let isWarpMenuOpen = false;

// --- 画像アセットロード ---
const images = {};
const imageSources = {
    hero: contextPath + "/img/hero.png",
    hero_back: contextPath + "/img/hero_back.png",
    hero_left: contextPath + "/img/hero_left.png",
    hero_right: contextPath + "/img/hero_right.png",
};

if (typeof mapSpecificImages !== 'undefined') {
    Object.assign(imageSources, mapSpecificImages);
}

let loadedCount = 0;
const totalImages = Object.keys(imageSources).length;
let currentDirection = "hero";

for (let key in imageSources) {
    images[key] = new Image();
    images[key].src = imageSources[key];
    images[key].onload = () => {
        loadedCount++;
        if (loadedCount === totalImages) {
            render();
        }
    };
}

const canvas = document.getElementById("gameCanvas");
canvas.width = TILE_SIZE * VIEW_COLS;
canvas.height = TILE_SIZE * VIEW_ROWS;
const ctx = canvas.getContext("2d");

// --- 歩行可能判定 ---
function isWalkable(x, y) {
    if (x < 0 || x >= MAP_COLS || y < 0 || y >= MAP_ROWS) return false;
    const tile = mapData[y][x];

    if (typeof checkWalkableTile !== 'undefined') {
        return checkWalkableTile(tile);
    }
    return tile === 0;
}

// --- カメラ追従 描画メイン処理 ---
function render() {
    let cameraX = playerX - Math.floor(VIEW_COLS / 2);
    let cameraY = playerY - Math.floor(VIEW_ROWS / 2);

    cameraX = Math.max(0, Math.min(cameraX, MAP_COLS - VIEW_COLS));
    cameraY = Math.max(0, Math.min(cameraY, MAP_ROWS - VIEW_ROWS));

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    for (let r = 0; r < VIEW_ROWS; r++) {
        for (let c = 0; c < VIEW_COLS; c++) {
            const mapX = cameraX + c;
            const mapY = cameraY + r;
            const drawX = c * TILE_SIZE;
            const drawY = r * TILE_SIZE;

            if (mapY >= 0 && mapY < MAP_ROWS && mapX >= 0 && mapX < MAP_COLS) {
                const tile = mapData[mapY][mapX];

                if (typeof tileMapping !== 'undefined' && tileMapping[tile]) {
                    const imgKey = tileMapping[tile];
                    if (images[imgKey]) {
                        ctx.drawImage(images[imgKey], drawX, drawY, TILE_SIZE, TILE_SIZE);
                    }
                } else if (tile !== 0) {
                    ctx.fillStyle = "#333333";
                    ctx.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                }

                ctx.strokeStyle = "rgba(0, 0, 0, 0.15)";
                ctx.strokeRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    const screenX = (playerX - cameraX) * TILE_SIZE;
    const screenY = (playerY - cameraY) * TILE_SIZE;

    const heroImg = images[currentDirection] || images.hero;
    if (heroImg) {
        const drawSize = 48;
        const offsetX = (drawSize - TILE_SIZE) / 2;
        const offsetY = drawSize - TILE_SIZE;
        ctx.drawImage(heroImg, screenX - offsetX, screenY - offsetY, drawSize, drawSize);
    }

    document.getElementById("posX").textContent = playerX;
    document.getElementById("posY").textContent = playerY;
}

// --- プレイヤー移動共通処理（PC・スマホ共通） ---
function movePlayer(direction) {
    // ワープメニューが開いている時はキャラクターを動かさない
    if (isWarpMenuOpen) return;

    let nextX = playerX;
    let nextY = playerY;

    switch (direction) {
        case "up":
            nextY--;
            currentDirection = "hero_back";
            break;
        case "down":
            nextY++;
            currentDirection = "hero";
            break;
        case "left":
            nextX--;
            currentDirection = "hero_left";
            break;
        case "right":
            nextX++;
            currentDirection = "hero_right";
            break;
        default:
            return;
    }

    if (isWalkable(nextX, nextY)) {
        playerX = nextX;
        playerY = nextY;
        savePosition(playerX, playerY);

        const currentTile = mapData[playerY][playerX];
        
        // ① 船橋(mapId=1)でタイル140か141を踏んだらメニューを開く
        if (currentMapId === 1 && (currentTile === 140 || currentTile === 141)) {
            stopMoving();
            render(); // メニューが開く前にキャラクターの位置を描画しておく
            openWarpMenu(1); // 1(船橋)を引数として渡す
            return; 
        }
        
        // ② 市川(mapId=2)で帰りのタイル（99または100）を踏んだらメニューを開く
        if (currentMapId === 2 && (currentTile === 99 || currentTile === 100)) {
            stopMoving();
            render();
            openWarpMenu(2); // 2(市川)を引数として渡す
            return;
        }
    }

    render();
}

// --- PCキーボード操作イベント ---
window.addEventListener("keydown", (e) => {
    if (isWarpMenuOpen) return;

    let direction = null;
    switch (e.key) {
        case "ArrowUp":
        case "w":
        case "W":
            direction = "up";
            break;
        case "ArrowDown":
        case "s":
        case "S":
            direction = "down";
            break;
        case "ArrowLeft":
        case "a":
        case "A":
            direction = "left";
            break;
        case "ArrowRight":
        case "d":
        case "D":
            direction = "right";
            break;
        default:
            return;
    }

    e.preventDefault();
    movePlayer(direction);
});

// --- スマホ仮想十字キー操作（タップ＆長押し連続移動対応） ---
let moveTimeout = null;
let moveInterval = null;

function startMoving(direction) {
    stopMoving();
    movePlayer(direction);

    // 250ms以上押し続けたら150ms間隔で連続移動
    moveTimeout = setTimeout(() => {
        moveInterval = setInterval(() => {
            movePlayer(direction);
        }, 150);
    }, 250);
}

function stopMoving() {
    if (moveTimeout) {
        clearTimeout(moveTimeout);
        moveTimeout = null;
    }
    if (moveInterval) {
        clearInterval(moveInterval);
        moveInterval = null;
    }
}

document.querySelectorAll(".dpad-btn").forEach((btn) => {
    const dir = btn.getAttribute("data-dir");

    btn.addEventListener("pointerdown", (e) => {
        e.preventDefault();
        btn.classList.add("active");
        startMoving(dir);
    });

    const handleRelease = () => {
        btn.classList.remove("active");
        stopMoving();
    };

    btn.addEventListener("pointerup", handleRelease);
    btn.addEventListener("pointerleave", handleRelease);
    btn.addEventListener("pointercancel", handleRelease);
    btn.addEventListener("contextmenu", (e) => e.preventDefault());
});

// --- ★ワープメニューの制御関数 ---
function openWarpMenu(sourceMapId) {
    document.getElementById("warpMenu").classList.remove("hidden");
    isWarpMenuOpen = true;

    // 今いるマップに応じて表示するボタンを切り替える
    const btnToIchikawa = document.getElementById("btnToIchikawa");
    const btnToFunabashi = document.getElementById("btnToFunabashi");

    if (sourceMapId === 1) {
        // 船橋にいるときは「市川へ行く」を表示、「船橋へ行く」を隠す
        btnToIchikawa.style.display = "block";
        btnToFunabashi.style.display = "none";
    } else if (sourceMapId === 2) {
        // 市川にいるときは「船橋へ行く」を表示、「市川へ行く」を隠す
        btnToIchikawa.style.display = "none";
        btnToFunabashi.style.display = "block";
    }
}

function closeWarpMenu() {
    document.getElementById("warpMenu").classList.add("hidden");
    isWarpMenuOpen = false;
}

function warpTo(targetMapId, targetX, targetY) {
    window.location.href = contextPath + "/ChangeMapServlet?map=" + targetMapId + "&x=" + targetX + "&y=" + targetY;
}

// --- メニューの開閉処理 ---
const menuBtn = document.getElementById("menuBtn");
const dropdownMenu = document.getElementById("dropdownMenu");

menuBtn.addEventListener("click", () => {
    dropdownMenu.classList.toggle("hidden");
});

window.addEventListener("click", (e) => {
    if (!menuBtn.contains(e.target) && !dropdownMenu.contains(e.target)) {
        dropdownMenu.classList.add("hidden");
    }
});

function savePosition(x, y) {
    fetch(contextPath + "/SavePositionServlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "x=" + x + "&y=" + y
    }).catch(error => {
        console.error("セーブに失敗しました:", error);
    });
}
let autoTransitionTimer = null;

function goToMap(url) {
    window.location.href = url;
}

function startOpening() {
    // 1. 黒いオーバーレイを消す
    const overlay = document.getElementById("start-overlay");
    if (overlay) {
        overlay.style.display = "none";
    }

    // 2. クロールアニメーションを起動
    const crawlContent = document.getElementById("crawl-content");
    if (crawlContent) {
        crawlContent.classList.add("running");
    }

    // 3. YouTube BGM再生
    const player = document.getElementById("youtube-player");
    if (player) {
        player.src = "https://www.youtube.com/embed/8U7XRoRWTbA?autoplay=1&enablejsapi=1";
    }

    // 4. 通常時：65秒後に自動でマップ画面へ移動
    autoTransitionTimer = setTimeout(() => {
        goToMap(mapUrl);
    }, 65000);
}

// 早送りボタンのトグル処理（Web Animations APIを使用）
function toggleFastForward() {
    const crawlContent = document.getElementById("crawl-content");
    const ffBtn = document.getElementById("ff-btn");

    if (!crawlContent) return;

    // 現在動作中のアニメーションを取得
    const animations = crawlContent.getAnimations();

    if (animations.length > 0) {
        const anim = animations[0];

        // 再生速度が通常（1倍）なら8倍速に変更
        if (anim.playbackRate === 1) {
            anim.playbackRate = 8; // 8倍速スクロール
            ffBtn.classList.add("active");
            ffBtn.textContent = "▶▶ 通常速度";
        } else {
            anim.playbackRate = 1; // 通常速度に戻す
            ffBtn.classList.remove("active");
            ffBtn.textContent = "⏩ 早送り";
        }
    }
}
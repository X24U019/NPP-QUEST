<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>ガチャ演出</title>
    <style>
        :root {
            --app-w: 100vw;
            --app-h: 100vh;
        }
        @supports (width: 100dvw) {
            :root {
                --app-w: 100dvw;
                --app-h: 100dvh;
            }
        }
        body {
            margin: 0;
            background-color: black;
            display: flex;
            justify-content: center;
            align-items: center;
            width: var(--app-w);
            height: var(--app-h);
            overflow: hidden;
        }
        video {
            max-width: 100%;
            max-height: 100%;
        }
        .skip-btn {
            position: absolute;
            top: 20px;
            right: 20px;
            padding: 10px 20px;
            background-color: rgba(255, 255, 255, 0.5);
            border: none;
            cursor: pointer;
            border-radius: 5px;
            font-weight: bold;
            z-index: 10;
        }
        @media (hover: none) and (pointer: coarse) and (orientation: portrait) {
            :root {
                --app-w: 100vh;
                --app-h: 100vw;
            }
            @supports (width: 100dvw) {
                :root {
                    --app-w: 100dvh;
                    --app-h: 100dvw;
                }
            }
            html {
                width: 100vw;
                height: 100vh;
                overflow: hidden;
                background-color: black;
            }
            body {
                position: fixed;
                top: 0;
                left: 0;
                transform: rotate(90deg) translateY(-100%);
                transform-origin: top left;
            }
        }
        @media (hover: none) and (pointer: coarse),
               (max-height: 550px) and (orientation: landscape) {
            .skip-btn {
                top: 12px;
                right: 12px;
                padding: 7px 14px;
                font-size: 13px;
            }
        }
    </style>
</head>
<body>
    <!-- スキップボタン -->
    <button class="skip-btn" onclick="location.href='GachaResultServlet'">スキップ</button>

    <!-- 動画プレーヤー (自動再生、ミュート設定) -->
    <!-- ※ブラウザの仕様上、音声付き動画の自動再生はミュート(muted)が必要な場合があります -->
    <video id="gachaVideo" src="${pageContext.request.contextPath}/video/gacha_animation.mp4" autoplay muted playsinline></video>

    <script>
        const video = document.getElementById('gachaVideo');
        
        // 動画の再生が終了(ended)したら、結果画面へ自動遷移
        video.addEventListener('ended', function() {
            window.location.href = 'GachaResultServlet'; 
        });
    </script>
</body>
</html>
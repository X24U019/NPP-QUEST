<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>オープニング - NPPクエスト</title>
    <!-- パラメータ ?v=2 を付けて最新のCSSを強制読込させます -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/opening.css?v=6">
</head>
<body>

    <!-- 1. ゲームスタート用オーバーレイ -->
    <div id="start-overlay">
        <button id="start-btn" onclick="startOpening()">ゲームスタート</button>
    </div>

    <!-- 2. 右上コントロールボタン（早送り・SKIP） -->
    <div class="control-btns">
        <button id="ff-btn" onclick="toggleFastForward()">⏩ 早送り</button>
        <button id="skip-btn" onclick="goToMap('<%= request.getContextPath() %>/MapServlet')">SKIP ▶</button>
    </div>

    <!-- 3. スター・ウォーズ ＋ 鬼滅風演出コンテナ -->
    <div class="star-wars-container">
        <div id="crawl-content" class="crawl">
            <div class="title">
                <p>Episode I</p>
                <h1>NPP QUEST</h1>
            </div>
            
            <p>過酷な掟と暴力が支配する辺境の村から、一人の少年が逃げ出した。 少年の名は、石井。 大人の仲間入りとして強制される１５歳の狩りの儀式を前に、彼は自由だけを渇望し、故郷を捨てた。</p>
            
            <p>辿り着いた街の入り口で衛兵に捕縛された石井青年は、そこから数奇な運命を辿る。 たゆまぬ鍛錬により剣士としての才能を開花させた彼は、世界を脅かす「魔王」を討伐すべく, 一人の魔法使いと共に旅立った。 そして激絶なる死闘の末、見事に魔王を打ち倒す。</p>
            
            <p>しかし、英雄の凱旋が果たされることはなかった。 誰からも愛される勇者・石井にどす黒い嫉妬を抱いていた魔法使いが、その手柄を独占すべく、魔王討伐の地で彼を深い闇へと封印したのだ。 歴史の闇に葬られた英雄。だが、その裏切りの一部始終を息を潜めて見つめる者がいた。エルフ族のハピニーである。 彼女はいつの日か、無念の勇者・石井の封印を解き放つ新たな希望が現れることを待ち望み、この残酷な真実を４００年もの間、密かに語り継いできた。</p>
            
            <p>時は流れ、４００年後の現在。 世界は未だ混沌の中にあった。はびこる魔王軍の残党たちが、あろうことか封印された「勇者石井」の強大な力を媒体とし、魔王の完全復活を目論んでいるのだ。 石井が眠る地へ近づくほど残党の勢力は増し、生半可な者では足を踏み入れることすら叶わない。</p>
            
            <p>今、ハピニーの願いを継ぐ５人の若き冒険者たちが立ち上がる。 魔王軍の野望を打ち砕き、４００年の眠りから勇者石井を解き放つために。 その悲願の先に待つのが、暴走したかつての英雄との哀しき死闘であることなど、今はまだ誰も知らない……。</p>

            <p class="ending-title">
                『NPPクエスト』――さあ、歪められた歴史を正す冒険の幕を開けよう。
            </p>

            <!-- 鬼滅風：ラストを飾る石井くんのド迫力顔写真 -->
            <div class="hero-image-box">
                <img src="<%= request.getContextPath() %>/img/ishii_face.jpg" alt="勇者石井" class="hero-img">
            </div>
        </div>
    </div>

    <!-- 4. YouTube BGM -->
    <iframe id="youtube-player" 
            src="https://www.youtube.com/embed/8U7XRoRWTbA?enablejsapi=1&autoplay=0" 
            style="display:none;" 
            allow="autoplay">
    </iframe>

    <script src="<%= request.getContextPath() %>/js/opening.js?v=6"></script>
    <script>
        const mapUrl = '<%= request.getContextPath() %>/MapServlet';
    </script>
</body>
</html>
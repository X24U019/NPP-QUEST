// map_ichikawa.js

// 1. 市川マップ固有の画像リスト
// ※画像はWebコンテンツの /img/ フォルダ内に入っている想定です。
const mapSpecificImages = {
    forest_tile: contextPath + "/img/forest tile.png",
    forest_road: contextPath + "/img/forest road.png",
    forest_right: contextPath + "/img/forest right.png",
    forest_left: contextPath + "/img/forest left.png",
    forest_down: contextPath + "/img/forest down.png",
    forest_corner_rtop: contextPath + "/img/forest corner rtop.png",
    forest_corner_rdown: contextPath + "/img/forest corner rdown.png",
    forest_corner_ltop: contextPath + "/img/forest corner ltop.png",
    forest_corner_ldown: contextPath + "/img/forest corner ldown.png",
	train99: contextPath + "/img/Station_gate1.png",
	train100: contextPath + "/img/Station_gate2.png",
};

// 2. タイルIDと画像の紐付け
// ※Java側（Map_Ichikawa.javaなど）の二次元配列で使う数字です。
// 自由に数字を変更していただいて構いません。
const tileMapping = {
    1: "forest_tile",
    2: "forest_road",
    3: "forest_right",
    4: "forest_left",
    5: "forest_down",
    6: "forest_corner_rtop",
    7: "forest_corner_rdown",
    8: "forest_corner_ltop",
    9: "forest_corner_ldown",
   99: "train99",   // ← ここを修正しました！
  100: "train100",  // ← ここを修正しました！
};

// 3. 当たり判定（歩行可能なタイル）
function checkWalkableTile(tile) {
    // キャラクターが上を歩いていいタイルの番号を指定します。
    // 例として「0（デフォルトの道など）」と「2（forest road）」を歩けるようにしています。
    return (
        tile === 0 || tile === 99 || tile === 100 ||
        ( tile >= 1 && tile <= 9) // forest_road は歩ける
        // 他にも歩けるタイル（例えば草地など）があれば、 || tile === 1 のように追加します
    );
}
package model;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    // 基本情報
    private String id;          // プレイヤーID (DAOのStringに合わせる)
    private String name;        // プレイヤー名
    private String password;    // パスワード (ER図より)

    // ステータス情報 (テーブル画像の構成に合わせています)
    private int level;          // レベル
    private int hp;             // 現在HP
    private int maxHp;          // 最大HP
    private int atk;            // 攻撃力
    private int def;            // 防御力
    private int sp;             // すばやさ
    private int exp;            // 経験値
    private int gold;           // 所持金

    // マップ情報
    private int mapId;          // マップID (今回は数値のようです)
    private int posX;           // X座標
    private int posY;           // Y座標

    // デフォルトコンストラクタ
    public Player() {
    }

    // --- 以下、GetterとSetter ---

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public int getMaxHp() {
        return maxHp;
    }
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }
    public int getAtk() {
        return atk;
    }
    public void setAtk(int atk) {
        this.atk = atk;
    }
    public int getDef() {
        return def;
    }
    public void setDef(int def) {
        this.def = def;
    }
    public int getSp() {
        return sp;
    }
    public void setSp(int sp) {
        this.sp = sp;
    }
    public int getExp() {
        return exp;
    }
    public void setExp(int exp) {
        this.exp = exp;
    }
    public int getGold() {
        return gold;
    }
    public void setGold(int gold) {
        this.gold = gold;
    }
    public int getMapId() {
        return mapId;
    }
    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
    public int getPosX() {
        return posX;
    }
    public void setPosX(int posX) {
        this.posX = posX;
    }
    public int getPosY() {
        return posY;
    }
    public void setPosY(int posY) {
        this.posY = posY;
    }
}
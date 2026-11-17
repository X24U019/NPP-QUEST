package model;

public class GachaItem {
    private int id;             // 武器の種類ID (weapon_id)
    private String name;
    private String type;
    private int rarity;
    private int boxId;          // インベントリ(item_box)の固有ID
    private String power;       // 攻撃力・威力 (power)
    private String skill;       // スキル (skill)
    private String description; // 武器の詳細説明文 (description)

    // コンストラクタ
    public GachaItem(int id, String name, String type, int rarity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.rarity = rarity;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getRarity() { return rarity; }
    
    public int getBoxId() { return boxId; }
    public void setBoxId(int boxId) { this.boxId = boxId; }
    
    public String getPower() { return power; }
    public void setPower(String power) { this.power = power; }

    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    // レアリティに応じた星の文字列を返す
    public String getRarityStars() {
        if (this.rarity == 1) return "☆";
        if (this.rarity == 2) return "☆☆";
        if (this.rarity == 3) return "☆☆☆";
        return "";
    }
    
    public String getImageFileName() {
        return this.type + "_" + this.id + ".png";
    }
}
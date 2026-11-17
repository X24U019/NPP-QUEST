package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.GachaItem;

public class GachaDao {
	// ※ ご自身のデータベース環境に合わせて以下の3行を変更してください
	private static final String URL = "jdbc:postgresql://aws-0-ap-northeast-1.pooler.supabase.com:5432/postgres?sslmode=require";
	private static final String USER = "postgres.tduvviqnrnkknfflajie";
	private static final String PASS = "NppGame2026!";

	public List<GachaItem> getAllWeapons() {
        List<GachaItem> list = new ArrayList<>();
        String sql = "SELECT weapon_id, name, rarity_type_id, power, skill, description FROM gacha_weapon";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("weapon_id");
                String name = rs.getString("name");
                int rarity = rs.getInt("rarity_type_id");
                GachaItem item = new GachaItem(id, name, "weapon", rarity);
                item.setPower(rs.getString("power"));
                item.setSkill(rs.getString("skill"));
                item.setDescription(rs.getString("description"));
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

	// プレイヤーのインベントリ（item_box）にアイテムを保存するメソッド
	public void insertItemBox(String playerId, int itemTypeId, int itemId, int quantity) {
        String sql = "INSERT INTO item_box (player_id, item_type_id, item_id, quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, playerId); // ★setInt から setString に変更
            pstmt.setInt(2, itemTypeId);
            pstmt.setInt(3, itemId);
            pstmt.setInt(4, quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	// プレイヤーのインベントリ（item_box）から所持アイテム一覧を取得するメソッド
	public List<GachaItem> getPlayerInventory(String playerId) {
        List<GachaItem> inventory = new ArrayList<>();
        String sql = "SELECT i.id, w.weapon_id, w.name, w.rarity_type_id, w.power, w.skill, w.description "
                   + "FROM item_box i "
                   + "JOIN gacha_weapon w ON i.item_id = w.weapon_id "
                   + "WHERE i.player_id = ? "
                   + "ORDER BY i.id ASC";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, playerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("weapon_id");
                    String name = rs.getString("name");
                    int rarity = rs.getInt("rarity_type_id");
                    GachaItem item = new GachaItem(id, name, "weapon", rarity);
                    item.setBoxId(rs.getInt("id"));
                    item.setPower(rs.getString("power"));
                    item.setSkill(rs.getString("skill"));
                    item.setDescription(rs.getString("description"));
                    inventory.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventory;
    }

	public boolean deleteItem(int boxId) {
		String sql = "DELETE FROM item_box WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, boxId);
			int count = pstmt.executeUpdate();
			return count > 0; // 1件以上削除されれば成功
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// --- 追加: レアリティごとの一括削除 ---
	// ※gacha_weaponテーブルと結合し、指定したレアリティのアイテムをitem_boxから消します
	public boolean deleteItemsByRarity(String playerId, int rarityTypeId) {
        String sql = "DELETE FROM item_box WHERE id IN ("
                   + "  SELECT i.id FROM item_box i "
                   + "  JOIN gacha_weapon w ON i.item_id = w.weapon_id "
                   + "  WHERE i.player_id = ? AND w.rarity_type_id = ?"
                   + ")";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, playerId); // ★setString に変更
            pstmt.setInt(2, rarityTypeId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	// --- 追加: プレイヤーのGold残高を更新する ---
    public boolean updatePlayerGold(String playerId, int newGold) {
        String sql = "UPDATE players SET gold = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newGold);
            pstmt.setString(2, playerId);
            int count = pstmt.executeUpdate();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
 // --- 追加: レアリティに応じた売却単価を返す ---
    private int getSellPrice(int rarityTypeId) {
        if (rarityTypeId == 1) return 10;    // ノーマル
        if (rarityTypeId == 2) return 50;    // レア
        if (rarityTypeId == 3) return 500;  // スーパーレア
        return 0;
    }

    // --- 追加: 個別売却（削除して獲得Goldを返す） ---
    public int sellItem(int boxId) {
        String selectSql = "SELECT w.rarity_type_id FROM item_box i "
                         + "JOIN gacha_weapon w ON i.item_id = w.weapon_id "
                         + "WHERE i.id = ?";
        String deleteSql = "DELETE FROM item_box WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            int rarity = 0;
            try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                selectPstmt.setInt(1, boxId);
                try (ResultSet rs = selectPstmt.executeQuery()) {
                    if (rs.next()) {
                        rarity = rs.getInt("rarity_type_id");
                    } else {
                        return 0;
                    }
                }
            }

            try (PreparedStatement deletePstmt = conn.prepareStatement(deleteSql)) {
                deletePstmt.setInt(1, boxId);
                int count = deletePstmt.executeUpdate();
                if (count > 0) {
                    return getSellPrice(rarity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // --- 追加: レアリティ別一括売却（削除して合計獲得Goldを返す） ---
    public int sellItemsByRarity(String playerId, int rarityTypeId) {
        String sql = "DELETE FROM item_box WHERE id IN ("
                   + "  SELECT i.id FROM item_box i "
                   + "  JOIN gacha_weapon w ON i.item_id = w.weapon_id "
                   + "  WHERE i.player_id = ? AND w.rarity_type_id = ?"
                   + ")";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, playerId);
            pstmt.setInt(2, rarityTypeId);
            int deletedCount = pstmt.executeUpdate();
            return deletedCount * getSellPrice(rarityTypeId);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
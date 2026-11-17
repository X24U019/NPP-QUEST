package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.PasswordUtil;
import model.Player;

public class PlayerDAO extends BaseDAO {

    public Player login(String id, String password) {
        Player player = null;
        String sql = "SELECT * FROM players WHERE id = ? AND password = ?";
        String hashedPassword = PasswordUtil.hash(id + password); // ハッシュ化

        try {
            this.connect();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, id);
                ps.setString(2, hashedPassword);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        player = new Player();
                        player.setId(rs.getString("id"));
                        player.setName(rs.getString("name"));
                        player.setPassword(rs.getString("password"));

                        player.setLevel(rs.getInt("level"));
                        player.setHp(rs.getInt("hp"));
                        player.setMaxHp(rs.getInt("max_hp"));
                        player.setAtk(rs.getInt("atk"));
                        player.setDef(rs.getInt("def"));
                        player.setSp(rs.getInt("sp"));
                        player.setExp(rs.getInt("exp"));
                        player.setGold(rs.getInt("gold"));
                        player.setMapId(rs.getInt("map_id"));
                        player.setPosX(rs.getInt("pos_x"));
                        player.setPosY(rs.getInt("pos_y"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.disconnect();
        }
        return player;
    }

    public boolean createPlayer(String id, String password, String name) {
        String hashedPassword = PasswordUtil.hash(id + password); // ハッシュ化
        String sql = "INSERT INTO players (id, password, name, level, hp, max_hp, atk, def, sp, exp, gold, map_id, pos_x, pos_y) "
                   + "VALUES (?, ?, ?, 1, 100, 100, 10, 10, 10, 0, 0, 1, 13, 9)";

        try {
            this.connect();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, id);
                ps.setString(2, hashedPassword);
                ps.setString(3, name);

                int result = ps.executeUpdate();
                return result > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            this.disconnect();
        }
    }

    public boolean updatePlayer(Player player) {
        String sql = "UPDATE players SET level=?, hp=?, max_hp=?, atk=?, def=?, sp=?, exp=?, gold=?, map_id=?, pos_x=?, pos_y=? WHERE id=?";
        try {
            this.connect();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, player.getLevel());
                ps.setInt(2, player.getHp());
                ps.setInt(3, player.getMaxHp());
                ps.setInt(4, player.getAtk());
                ps.setInt(5, player.getDef());
                ps.setInt(6, player.getSp());
                ps.setInt(7, player.getExp());
                ps.setInt(8, player.getGold());
                ps.setInt(9, player.getMapId());
                ps.setInt(10, player.getPosX());
                ps.setInt(11, player.getPosY());
                ps.setString(12, player.getId());

                int result = ps.executeUpdate();
                return result > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            this.disconnect();
        }
    }

    // マップと座標のみを更新するメソッド
    public boolean updateMapAndPosition(String id, int mapId, int x, int y) {
        String sql = "UPDATE players SET map_id = ?, pos_x = ?, pos_y = ? WHERE id = ?";

        try {
            this.connect();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, mapId);
                ps.setInt(2, x);
                ps.setInt(3, y);
                ps.setString(4, id);

                int result = ps.executeUpdate();
                return result > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            this.disconnect();
        }
    }

    public boolean existsId(String id) {
        String sql = "SELECT id FROM players WHERE id = ?";
        try {
            this.connect();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        } finally {
            this.disconnect();
        }
    }
}
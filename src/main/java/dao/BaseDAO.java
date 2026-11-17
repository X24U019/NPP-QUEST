package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDAO {
    // Supabaseの接続情報
    private static final String URL = "jdbc:postgresql://aws-0-ap-northeast-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER = "postgres.tduvviqnrnkknfflajie";
    private static final String PASS = "NppGame2026!";

    protected Connection con = null;

    // DB接続
    protected void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection(URL, USER, PASS);
    }

    // DB切断
    protected void disconnect() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
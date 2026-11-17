package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.PlayerDAO;
import model.Player;

@WebServlet("/SavePositionServlet")
public class SavePositionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // セッションからログイン中のプレイヤー情報を取得
        HttpSession session = request.getSession();
        Player player = (Player) session.getAttribute("loginUser");

        if (player != null) {
            try {
                // JavaScriptから送られてきた新しいX座標とY座標を受け取る
                int x = Integer.parseInt(request.getParameter("x"));
                int y = Integer.parseInt(request.getParameter("y"));

                // 1. セッションの座標を更新（次回マップ読み込み時のため）
                player.setPosX(x);
                player.setPosY(y);
                session.setAttribute("posX", x);
                session.setAttribute("posY", y);
                session.setAttribute("loginUser", player);

                // 2. データベースを更新（ログアウトしても消えないようにするため）
                PlayerDAO dao = new PlayerDAO();
                dao.updatePlayer(player);

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
}
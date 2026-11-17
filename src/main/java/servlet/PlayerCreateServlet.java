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

@WebServlet("/PlayerCreateServlet")
public class PlayerCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 画面表示（GET）
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/player_create.jsp").forward(request, response);
    }

    // 登録実行（POST）
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String name = request.getParameter("name");

        PlayerDAO dao = new PlayerDAO();

        // 重複チェック
        if (dao.existsId(id)) {
            request.setAttribute("errorMsg", "そのIDは既に使用されています。");
            request.getRequestDispatcher("/WEB-INF/jsp/player_create.jsp").forward(request, response);
            return;
        }

        // DB登録実行
        boolean isSuccess = dao.createPlayer(id, password, name);

        if (isSuccess) {
            // ★追加：登録したプレイヤー情報をDBから取得し、セッションにログイン状態を保存する
            Player player = dao.login(id, password);
            if (player != null) {
                HttpSession session = request.getSession();
                session.setAttribute("loginUser", player);
                session.setAttribute("posX", player.getPosX());
                session.setAttribute("posY", player.getPosY());
            }

            // オープニング画面へ遷移
            response.sendRedirect(request.getContextPath() + "/OpeningServlet");
        } else {
            request.setAttribute("errorMsg", "アカウント作成に失敗しました。もう一度お試しください。");
            request.getRequestDispatcher("/WEB-INF/jsp/player_create.jsp").forward(request, response);
        }
    }
}
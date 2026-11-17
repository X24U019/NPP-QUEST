package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.GachaDao;
import model.Player;

@WebServlet("/ItemDeleteServlet")
public class ItemDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Player loginUser = (Player) session.getAttribute("loginUser");
        
        String playerId = (loginUser != null) ? String.valueOf(loginUser.getId()) : "1";

        String action = request.getParameter("action");
        GachaDao dao = new GachaDao();
        int earnedGold = 0;

        if ("single".equals(action)) {
            // 個別売却
            int boxId = Integer.parseInt(request.getParameter("boxId"));
            earnedGold = dao.sellItem(boxId);
        } else if ("bulk".equals(action)) {
            // 一括売却
            int rarityTypeId = Integer.parseInt(request.getParameter("rarityTypeId"));
            earnedGold = dao.sellItemsByRarity(playerId, rarityTypeId);
        }

        // 獲得したGoldをセッションとDBの所持Goldに加算
        if (earnedGold > 0 && loginUser != null) {
            int newGold = loginUser.getGold() + earnedGold;
            loginUser.setGold(newGold);
            session.setAttribute("loginUser", loginUser);
            dao.updatePlayerGold(playerId, newGold);
        }

        // 売却処理が終わったら、インベントリ画面(InventoryServlet)にリダイレクトして再表示
        response.sendRedirect(request.getContextPath() + "/InventoryServlet");
    }
}
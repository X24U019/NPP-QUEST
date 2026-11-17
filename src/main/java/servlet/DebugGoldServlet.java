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

@WebServlet("/DebugGoldServlet")
public class DebugGoldServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Player loginUser = (Player) session.getAttribute("loginUser");
        
        if (loginUser != null) {
            // URLの「?gold=〇〇」から金額を取得（指定がなければ5000）
            String goldParam = request.getParameter("gold");
            int newGold = (goldParam != null) ? Integer.parseInt(goldParam) : 5000;
            
            // 1. 画面表示用（セッション）のGoldを更新
            loginUser.setGold(newGold);
            session.setAttribute("loginUser", loginUser);
            
            // 2. データベースのGoldを更新
            GachaDao dao = new GachaDao();
            dao.updatePlayerGold(String.valueOf(loginUser.getId()), newGold);
        }
        
        // ガチャ画面へ戻る（※ガチャ画面を開くServlet名に合わせて変更してください）
        response.sendRedirect(request.getContextPath() + "/GachaServlet");
    }
}
package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.GachaDao;
import model.GachaItem;
import model.Player;

@WebServlet("/InventoryServlet")
public class InventoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// セッションからログイン中のプレイヤー情報を取得
		HttpSession session = request.getSession();
		Player loginUser = (Player) session.getAttribute("loginUser");
		
		// ログイン中ならそのID（"test1"など）を文字列で取得、未ログインなら仮の"1"
		String playerId = (loginUser != null) ? String.valueOf(loginUser.getId()) : "1";
		
		// データベースから所持アイテム一覧を取得
		GachaDao dao = new GachaDao();
		List<GachaItem> inventoryList = dao.getPlayerInventory(playerId);
		
		System.out.println("インベントリ取得件数: " + (inventoryList != null ? inventoryList.size() : "null"));

		// 取得したリストをリクエストに保存（JSPに渡すため）
		request.setAttribute("inventoryList", inventoryList);
		
		// インベントリ画面（JSP）へフォワード
		request.getRequestDispatcher("/WEB-INF/jsp/inventory.jsp").forward(request, response);
	}
}
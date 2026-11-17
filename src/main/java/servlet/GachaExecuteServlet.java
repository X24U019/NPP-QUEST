package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.GachaDao;
import model.GachaItem;
import model.Player;

@WebServlet("/GachaExecuteServlet")
public class GachaExecuteServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Player loginUser = (Player) session.getAttribute("loginUser");

		String timesParam = request.getParameter("times");
		int times = 1;
		int cost = 100; // 単発の消費Gold
		if (timesParam != null && timesParam.equals("10")) {
			times = 10;
			cost = 1000; // 10連の消費Gold
		}

		// --- 残高不足チェック ---
		if (loginUser == null || loginUser.getGold() < cost) {
			response.sendRedirect(request.getContextPath() + "/GachaServlet");
			return;
		}

		GachaDao dao = new GachaDao();
		List<GachaItem> allWeapons = dao.getAllWeapons();

		// 1. 取得した全武器をレアリティごとにリストに分ける
		List<GachaItem> normalWeapons = new ArrayList<>();
		List<GachaItem> rareWeapons = new ArrayList<>();
		List<GachaItem> superRareWeapons = new ArrayList<>();

		for (GachaItem item : allWeapons) {
			if (item.getRarity() == 1) {
				normalWeapons.add(item);
			} else if (item.getRarity() == 2) {
				rareWeapons.add(item);
			} else if (item.getRarity() == 3) {
				superRareWeapons.add(item);
			}
		}

		List<GachaItem> resultList = new ArrayList<>();
		Random random = new Random();

		// 2. 指定回数（1回 or 10回）だけルーレットを回す
		for (int i = 0; i < times; i++) {
			int roulette = random.nextInt(100) + 1;
			List<GachaItem> targetList;

			if (roulette <= 69) {
				targetList = normalWeapons;
			} else if (roulette <= 99) {
				targetList = rareWeapons;
			} else {
				targetList = superRareWeapons;
			}

			if (targetList.isEmpty()) {
				targetList = allWeapons;
			}

			int randomIndex = random.nextInt(targetList.size());
			resultList.add(targetList.get(randomIndex));
		}

		// ★修正：数値に変換せず、StringのままプレイヤーIDを取得する
		String playerId = String.valueOf(loginUser.getId());
		int weaponItemTypeId = 1;
		
		for (GachaItem item : resultList) {
			dao.insertItemBox(playerId, weaponItemTypeId, item.getId(), 1);
		}

		// --- 3. Goldを消費してセッションとDBを更新 ---
		int newGold = loginUser.getGold() - cost;
		loginUser.setGold(newGold);
		session.setAttribute("loginUser", loginUser);
		dao.updatePlayerGold(playerId, newGold);

		// --- 4. 抽選結果をセッションに保存 ---
		session.setAttribute("gachaResult", resultList);

		// --- 5. 動画再生用のJSPへフォワード ---
		request.getRequestDispatcher("/WEB-INF/jsp/gacha_animation.jsp").forward(request, response);
	}
}
package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data_access.LendingBookDAO;
import javabeans.LendBookHistroy;
import javabeans.UserDTO;

/**
 * Servlet implementation class UserHistoryForServlet
 */
@WebServlet("/UserHistoryForServlet")
public class UserHistoryForServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserHistoryForServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());

		//■Get通信(全ユーザー情報一覧画面から/直接アクセス）********************************
		//リクエストパラメータ取得
		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		UserDTO user = (UserDTO) session.getAttribute("user");

		//アクセス元により分岐-------------------------------------------------------
		if (user.getId() != 0) {//直接アクセスした場合
			//TOP画面へリダイレクト
			response.sendRedirect("/YourShelf/Index");
			return;

		}
		int index = Integer.parseInt(request.getParameter("index"));
		List<UserDTO> userList = (List<UserDTO>) session.getAttribute("userList");
		//value=index番号のため、Index番号のユーザーに対応する「借りた履歴」を取得する
		LendingBookDAO dao = new LendingBookDAO();
		List<LendBookHistroy> userBookHis = dao.getBookHistroy(userList.get(index));//※index番号のユーザーの一覧を取得するメソッド

		//if(AllBooksDTO!=null) {//データの取得に成功した場合
		//取得した借りた履歴一覧情報をセッションスコープに保存
		request.setAttribute("userBookHis", userBookHis);
		request.setAttribute("user", userList.get(index));

		//借りた履歴一覧表示画面へフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/userHistory.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
	}

}

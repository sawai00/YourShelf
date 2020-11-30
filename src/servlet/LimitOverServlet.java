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

import javabeans.LendingBookDTO;

/**
 * Servlet implementation class LimitOverServlet
 */
@WebServlet("/LimitOverServlet")
public class LimitOverServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LimitOverServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());

		//■Get通信(管理者ログイン画面[返却期限切れ一覧]/直接）********************************
		//リクエストパラメータ取得
		request.setCharacterEncoding("UTF-8");
		String value = request.getParameter("value");

		//アクセス元により分岐-------------------------------------------------------
		if (value == null) {//直接アクセスした場合
			//TOP画面へリダイレクト
			response.sendRedirect("/YourShelf/Index");

		} else {//管理者ログイン画面[返却期限切れ一覧]からのアクセスした場合
			//期限切れの一覧情報を取得
			List<LendingBookDTO> list=LendingBookDAO.getLendingBookList();
			List<LimitOverBooks> bookList=AllBooksDAO.getLimitOverBookList(list);

			//期限切れ一覧情報をセッションスコープに保存
			HttpSession session = request.getSession();
			session.setAttribute("bookList",bookList);

			//期限切れ一覧表示画面へフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/limitOver.jsp");
			dispatcher.forward(request, response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);

		//■POST通信（直接アクセス）********************************************
			//ＴＯＰへリダイレクト
			response.sendRedirect("/YourServlet/Index");
	}

}

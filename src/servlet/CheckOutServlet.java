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
import javabeans.Book;
import javabeans.UserDTO;

/**
 * Servlet implementation class CheckOutServlet
 */
@WebServlet("/CheckOutServlet")
public class CheckOutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckOutServlet() {
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

		//■GET通信（直接アクセス）*********************************************************************
		//ＴＯＰへリダイレクト
		response.sendRedirect("/YourServlet/Index");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);

		//■POST通信（直接/一般ユーザー[借りる]ボタンからアクセス）**************************************
		//リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String value = request.getParameter("value");
		RequestDispatcher dispatcher;

		//アクセス元により分岐---------------------------------------------------------------------------
		if (value == null) {//直接アクセスの場合
			//ＴＯＰへリダイレクト
			response.sendRedirect("/YourServlet/Index");

		} else if (value.equals("〇〇")) {//一般ユーザー[借りる]ボタンからアクセスの場合
			//ログイン確認のためセッションスコープからユーザ情報を取得
			HttpSession session = request.getSession();
			UserDTO loginUser = (UserDTO) session.getAttribute("UserDTO");

			if (loginUser != null) {//ログイン済の場合（セッションスコープからユーザー情報を取得できた場合）
				int index = Integer.parseInt(request.getParameter("index"));//どの位置の本が押されたか取得
				//セッションに保存されているbookリストを取得
				List<Book> books = (List<Book>) session.getAttribute("book");

				//※書籍情報を「book」変数に格納
				Book lendBook = books.get(index);

				//もし該当書籍が借りられている場合所有書籍一覧に戻す
				if(lendBook.isCheckedOut()) {
					dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/viewBook.jsp");
					dispatcher.forward(request, response);
					return;
				}
				//★取得した書籍データをDBへ保存⇒結果をbooleanで返す
				LendingBookDAO dao = new LendingBookDAO();
				boolean result = dao.lendBook(loginUser, lendBook);

				if (result) {//借りることに成功した場合
					//借りた書籍の情報をセッションスコープに保存
					request.setAttribute("lendBook", lendBook);

					//貸出完了表示画面にフォワード
					dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/checkOutOK.jsp");
					dispatcher.forward(request, response);

				} else {//借りることに失敗した場合
						//エラーメッセージを保存
						//String errorMsg="借りられませんでした";
						//エラーメッセージをセッションスコープに保存
						//HttpSession session=request.getSession();
						//session.setAttribute("errorMsg",errorMsg);

					//貸出完了表示画面にフォワード
					dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/viewBook.jsp");
					dispatcher.forward(request, response);
				}

			} else {//ログインしていない場合
				//ログイン画面へフォワード
				dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/CheckOut.jsp");
				dispatcher.forward(request, response);
			}
		}
	}

}
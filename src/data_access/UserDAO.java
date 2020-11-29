package data_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import javabeans.Login;
import javabeans.UserDTO;

public class UserDAO {
	ConnectionShelf connector;

	/**
	 * @param userName
	 * @param password
	 * @return UserDTO
	 * 新規登録時のユーザー登録(INSERT)メソッド
	 */
	public UserDTO addUser(String userName, String password) {
		//idはオートインクリメントなためデフォルト入力値はnull
		final String SQL = "INSERT INTO USER(id,name,password) VALUES(NULL,?,?)";
		connector = new ConnectionUser();

		try (Connection connection = connector.getConnection();
				//第二引数にRETURN_GENERATED_KEYSを設定する
				PreparedStatement statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

			statement.setString(1, userName);
			statement.setString(2, password);

			statement.executeUpdate();

			// getGeneratedKeys()により、Auto_IncrementされたIDを取得する
			ResultSet rs = statement.getGeneratedKeys();

			if (rs.next()) {
				//取得したidをもとにdtoを作成し、それをリターンする
				int id = rs.getInt(1);
				UserDTO dto = new UserDTO(id,userName, password);

				return dto;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		//何かしらアップデートができなければnullを返す
		return null;
	}

	/**
	 * @return List<UserDTO>
	 * 管理者専用メソッド
	 * 全ユーザーの情報を取得する
	 */
	public List<UserDTO> getUserList() {
		final String SQL = "SELECT id,name,password FROM USER";
		connector = new ConnectionAdmin();

		try (Connection connection = connector.getConnection();
				Statement statement = connection.createStatement()) {

			List<UserDTO> lists = new ArrayList<>();

			ResultSet rs = statement.executeQuery(SQL);
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String password = rs.getString("password");

				lists.add(new UserDTO(id, name, password));
			}
			return lists;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * @param login
	 * @return UserDTO
	 * ログイン時に入力されたパラメータから該当するユーザーを抽出
	 * 抽出されたユーザーでUserDTOのインスタンスを作成し、リターンする
	 * 該当するユーザーがいなければnullを返す
	 */
	public UserDTO findByLogin(Login login) {
		final String SQL = "SELECT id,name,password FROM USER WHERE name = ? AND password = ?";
		connector = new ConnectionUser();

		try (Connection connection = connector.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL)) {

			statement.setString(1, login.getName());
			statement.setString(2, login.getPassword());

			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String password = rs.getString("password");

				return new UserDTO(id, name, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	}
}

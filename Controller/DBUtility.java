package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtility {
	public static Connection getConnection(){		
		Connection conn=null;
		try {
			//1. mySQL database class를 로드한다. 
			Class.forName("com.mysql.jdbc.Driver");
			//2. 주소, 아이디, 비밀번호를 통해서 접속요청
			conn= DriverManager.getConnection("jdbc:mysql://localhost/swimcenter", "root", "123456");
			CallAlert.callAlert("연결성공: 데이터베이스에 연결되었습니다.");
		} catch (Exception e) {
			CallAlert.callAlert("연결실패: 데이터베이스 연결에 실패하였습니다.");
			e.printStackTrace();
			return null;
		}
		return conn;
	}
	
	public static void close(PreparedStatement psmt, Connection conn) {
		try {
			if (psmt != null) {
				psmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			CallAlert.callAlert("자원닫기 실패: psmt, con 닫기 실패했습니다.");
			e.printStackTrace();
		}
	}
}

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
			//1. mySQL database class�� �ε��Ѵ�. 
			Class.forName("com.mysql.jdbc.Driver");
			//2. �ּ�, ���̵�, ��й�ȣ�� ���ؼ� ���ӿ�û
			conn= DriverManager.getConnection("jdbc:mysql://localhost/swimcenter", "root", "123456");
			CallAlert.callAlert("���Ἲ��: �����ͺ��̽��� ����Ǿ����ϴ�.");
		} catch (Exception e) {
			CallAlert.callAlert("�������: �����ͺ��̽� ���ῡ �����Ͽ����ϴ�.");
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
			CallAlert.callAlert("�ڿ��ݱ� ����: psmt, con �ݱ� �����߽��ϴ�.");
			e.printStackTrace();
		}
	}
}

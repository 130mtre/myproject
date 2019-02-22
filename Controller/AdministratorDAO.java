package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.AttendanceData;
import Model.CustomerData;
import Model.ProgramData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class AdministratorDAO {
	public static Connection conn;
	private PreparedStatement psmt;
	private ResultSet rs;
	private StringBuffer sql;
	private boolean isException;
	private int count;
	private boolean custCheck;
	private boolean timcCheckFlag;
	
	public static ArrayList<ProgramData> programArrayList= new ArrayList<ProgramData>();
	public static ArrayList<AttendanceData> attendanceArrayList= new ArrayList<AttendanceData>();
	public boolean insertTimeCheck(int custno) { // 출석체크를 위해 회원번호를 체크한다.
		try {
			conn = DBUtility.getConnection();
			sql = new StringBuffer();
			sql.append("SELECT customerno FROM customertbl where customerno = ? "); // << 회원번호를 입력받아 DB에 있는지 확인한다
			psmt = conn.prepareStatement(sql.toString());
			psmt.setInt(1, custno);
			rs = psmt.executeQuery();
			if (rs.next()) {
				CallAlert.callAlert("성공: 성공입니다."+rs);
				StringBuffer insertAttendance = new StringBuffer();
				insertAttendance.append("call ");
				insertAttendance.append("selectAttendance(?)");
				psmt.setInt(1, custno);
				count= psmt.executeUpdate();
				if(count==0) {
					CallAlert.callAlert("삽입 쿼리실패: 삽입 쿼리문 실패했습니다.");
				}
				timcCheckFlag = true;
			}else{	
				timcCheckFlag = false;
			}
		} catch (Exception e) {
			CallAlert.callAlert("에러: 에러입니다.");
			e.printStackTrace();
		} finally {
			DBUtility.close(psmt, conn);
		}
		return timcCheckFlag;
	}
	 //오프라인 회원가입
	public static int insertOffCustomerData(String name, String gender, String birth, String phone, String address) {
		StringBuffer insertCustomer = new StringBuffer();
		 insertCustomer.append("call ");
		 insertCustomer.append("insertOfflinecustomer(?,?,?,?,?)");
		
		//1.2 데이터베이스 Connection을 가져와야 한다.
		Connection con= null;		
		//1.3 쿼리문을 실행해야할 Statement를 만들어야한다.
		PreparedStatement psmt= null;
		int count=0;
		
		try {
			con= DBUtility.getConnection();
			psmt= con.prepareStatement(insertCustomer.toString());
			//1.4 쿼리문에 실제데이터를 연결한다.
			psmt.setString(1, name);
			psmt.setString(2, gender);
			psmt.setString(3, birth);
			psmt.setString(4, phone);
			psmt.setString(5, address);
			
			//1.5 실제데이터를 연결한 쿼리문을 실행하라.
			count= psmt.executeUpdate();
			if(count==0) {
				CallAlert.callAlert("off삽입 쿼리실패: 삽입 쿼리문 실패했습니다.");
				return count;
			}
		} catch (SQLException e) {
			CallAlert.callAlert("off삽입실패: 데이터베이스 삽입 실패했습니다.");
			e.printStackTrace();
		}finally {
			//1.6 자원객체를 닫아야한다.
			DBUtility.close(psmt, conn);
		}//end of finally
		return count;
	}	
	//프로그램 전체 정보
	public static ArrayList<ProgramData> getProgramTotalData() { 
		StringBuffer selectProgram = new StringBuffer();
		selectProgram.append("select * from programtbl");		
		Connection conn=null;		
		PreparedStatement psmt= null;
		ResultSet rs= null;
		try {
			conn= DBUtility.getConnection();
			psmt= conn.prepareStatement(selectProgram.toString());
			rs= psmt.executeQuery(); //번개아이콘
			if(rs==null) {
				CallAlert.callAlert("select 실패: select 쿼리문 실패했습니다.");
				return null;
			}
			
			while(rs.next()) {
				ProgramData program= new ProgramData(
				rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4),
				rs.getInt(5), rs.getInt(6), rs.getString(7), rs.getInt(8));
				programArrayList.add(program);
			}
		} catch (SQLException e) {
			CallAlert.callAlert("select실패: 데이터베이스 program total 가져오기 실패했습니다.");
			e.printStackTrace();
		}finally {
			//1.6 자원객체를 닫아야한다.
			DBUtility.close(psmt, conn);
		}//end of finally
		return programArrayList;
	}
	//프로그램 정보 넣기
	public static int insertProgramData(String selection, String classname, String group, int code, int customerno, int to, String target,
			int tuition) {
		StringBuffer insertProgram= new StringBuffer();
		insertProgram.append("insert into programtbl ");
		insertProgram.append("values ");
		insertProgram.append("(?,?,?,?,?,?,?,?) ");
		
		//1.2 데이터베이스 Connection을 가져와야 한다.
		Connection con= null;		
		//1.3 쿼리문을 실행해야할 Statement를 만들어야한다.
		PreparedStatement psmt= null;
		int count=0;
		
		try {
			con= DBUtility.getConnection();
			
			psmt= con.prepareStatement(insertProgram.toString());
			//1.4 쿼리문에 실제데이터를 연결한다.
			psmt.setString(1, selection);
			psmt.setString(2, classname);
			psmt.setString(3, group);
			psmt.setInt(4, code);
			psmt.setInt(5, customerno);
			psmt.setInt(6, to);
			psmt.setString(7, target);
			psmt.setInt(8, tuition);
			
			//1.5 실제데이터를 연결한 쿼리문을 실행하라.
			count= psmt.executeUpdate();
			if(count==0) {
				CallAlert.callAlert("삽입 쿼리실패: 삽입 쿼리문 실패했습니다.");
				return count;
			}
		} catch (SQLException e) {
			CallAlert.callAlert("삽입실패: 데이터베이스 삽입 실패했습니다.");
			e.printStackTrace();
		}finally {
			//1.6 자원객체를 닫아야한다.
			DBUtility.close(psmt, conn);
		}//end of finally
		return count;
	}
	//출석정보
	public static ArrayList<AttendanceData> getAttendanceData() {
		StringBuffer selectAttendance = new StringBuffer();
		selectAttendance.append("call ");
		selectAttendance.append("selectAttendance()");
		Connection conn=null;		
		PreparedStatement psmt= null;
		ResultSet rs= null;
		try {
			conn= DBUtility.getConnection();
			psmt= conn.prepareStatement(selectAttendance.toString());
			rs= psmt.executeQuery(); //번개아이콘
			if(rs==null) {
				CallAlert.callAlert("select 실패: select 쿼리문 실패했습니다.");
				return null;
			}
			
			while(rs.next()) {
				AttendanceData attendance= new AttendanceData(
				rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
				rs.getString(6), rs.getString(7), rs.getString(8),rs.getString(9) );
				attendanceArrayList.add(attendance);
			}
		} catch (SQLException e) {
			CallAlert.callAlert("select실패: 데이터베이스 attendance total 가져오기 실패했습니다.");
			e.printStackTrace();
		}finally {
			//1.6 자원객체를 닫아야한다.
			DBUtility.close(psmt, conn);
		}//end of finally
		return attendanceArrayList;
	}
	
}

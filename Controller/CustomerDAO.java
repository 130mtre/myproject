package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.CustomerData;
import Model.ProgramData;

public class CustomerDAO {
	public static Connection conn;
	private PreparedStatement psmt;
	private ResultSet rs;
	private StringBuffer sql;
	private CustomerData customerData;
	public static int customerno;
	public boolean isException= false;
	public boolean idcheckFlag= false;
	public boolean custNoFlag= false;
	private String id;
	private String pw;
	
	public static ArrayList<CustomerData> custArrayList= new ArrayList<CustomerData>();
	public static ArrayList<ProgramData> cProgramArrayList= new ArrayList<ProgramData>();
	
	public CustomerDAO() {
		super();
	}
	public void setCustomerData(CustomerData customerData) {
		this.customerData = customerData;
	}
	// 온라인 회원가입
	public static int insertCustomerData(String id, String pw, String name, String gender, String birth, String phone, String address, String picture) { 
		StringBuffer insertCustomer = new StringBuffer();
		 insertCustomer.append("call ");
		 insertCustomer.append("insertcustomer(?,?,?,?,?,?,?,?)");
		
		//1.2 데이터베이스 Connection을 가져와야 한다.
		Connection conn= null;		
		//1.3 쿼리문을 실행해야할 Statement를 만들어야한다.
		PreparedStatement psmt= null;
		int count=0;
		
		try {
			conn= DBUtility.getConnection();
			
			psmt= conn.prepareStatement(insertCustomer.toString());
			//1.4 쿼리문에 실제데이터를 연결한다.
			psmt.setString(1, id);
			psmt.setString(2, pw);
			psmt.setString(3, name);
			psmt.setString(4, gender);
			psmt.setString(5, birth);
			psmt.setString(6, phone);
			psmt.setString(7, address);
			psmt.setString(8, picture);
			
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
	//2. 테이블에서 전체내용을 모두 가져오는 함수
	public  static ArrayList<CustomerData> getCustomerTotalData(){
		StringBuffer selectCustomer = new StringBuffer();
		selectCustomer.append("select * from customertbl");		
		Connection conn=null;		
		PreparedStatement psmt= null;
		ResultSet rs= null;
		try {
			conn= DBUtility.getConnection();
			psmt= conn.prepareStatement(selectCustomer.toString());
			rs= psmt.executeQuery(); //번개아이콘
			if(rs==null) {
				CallAlert.callAlert("select 실패: select 쿼리문 실패했습니다.");
				return null;
			}
			
			while(rs.next()) {
				CustomerData customer= new CustomerData(
				rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
				rs.getString(5), rs.getString(6), rs.getInt(7), rs.getString(8), 
				rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12));
				custArrayList.add(customer);
			}
		} catch (SQLException e) {
			CallAlert.callAlert("select실패: 데이터베이스 custom total 가져오기 실패했습니다.");
			e.printStackTrace();
		}finally {
			//1.6 자원객체를 닫아야한다.
			DBUtility.close(psmt, conn);
		}//end of finally
		return custArrayList;
	}	
	public boolean loginSelect(String id, String pw) { //로그인
		try {
			conn = DBUtility.getConnection();
			sql = new StringBuffer();
			//로그인을 위해 TextFiled에 입력한 값으로 SQL처리.
			sql.append("SELECT * FROM customertbl WHERE id = ? and pw = ? ");
			psmt = conn.prepareStatement(sql.toString());
			psmt.setString(1, id);
			psmt.setString(2, pw);
			rs = psmt.executeQuery();
			if (rs.next()) {
				isException = true;
				CallAlert.callAlert("성공: 성공입니다."+rs);
			}else{		
				isException = false;
			}
			return isException;
		} catch (Exception e) {
			CallAlert.callAlert("에러: 에러입니다.");
			return false;
		} finally {
			DBUtility.close(psmt, conn);
		}
	}	
	public boolean findIDSelect(String customerno, String birth, String phone) { //아이디찾기
		try {
			conn = DBUtility.getConnection();
			sql = new StringBuffer();
			sql.append("SELECT * FROM customertbl WHERE customerno = ? and birth = ? and phone = ?");
			psmt = conn.prepareStatement(sql.toString());
			psmt.setString(1, customerno);
			psmt.setString(2, birth);
			psmt.setString(3, phone);
			rs = psmt.executeQuery();
			if (rs.next()) {
				isException = true;
				id= rs.getString("id");
				CallAlert.callAlert("성공: 아이디는 "+id+" 입니다.");
			}else{		
				isException = false;
			}		
		} catch (Exception e) {
			CallAlert.callAlert("에러: 에러입니다.");
			return false;
		} finally {
			DBUtility.close(psmt, conn);
		}
		return isException;
	}
	public boolean findPWSelect(String id, String birth, String phone) { //비밀번호 찾기
		try {
			conn = DBUtility.getConnection();
			sql = new StringBuffer();
			sql.append("SELECT * FROM customertbl WHERE id = ? and birth = ? and phone = ? ");
			psmt = conn.prepareStatement(sql.toString());
			psmt.setString(1, id);
			psmt.setString(2, birth);
			psmt.setString(3, phone);
			rs = psmt.executeQuery();
			if (rs.next()) {
				isException = true;
				pw= rs.getString("pw");
				CallAlert.callAlert("성공: 비밀번호는 "+pw+" 입니다.");
			}else{		
				isException = false;
			}
			return isException;
		} catch (Exception e) {
			CallAlert.callAlert("에러: 에러입니다.");
			return false;
		} finally {
			try {
				if(conn!=null) { conn.close(); }
			} catch (SQLException e) {
				CallAlert.callAlert("자원닫기 실패: con 닫기 실패했습니다.");
			} 
		}
	}	
	public boolean custNoSelect(String name, String birth, String phone) { // 회원번호찾기
		try {
			conn = DBUtility.getConnection();
			sql = new StringBuffer();
			sql.append("SELECT * FROM customertbl WHERE name = ? and birth = ? and phone = ? ");
			psmt = conn.prepareStatement(sql.toString());
			psmt.setString(1, name);
			psmt.setString(2, birth);
			psmt.setString(3, phone);
			rs = psmt.executeQuery();
			if (rs.next()) {
				customerno= rs.getInt("customerno");
				CallAlert.callAlert("회원번호 찾기성공: 성공입니다."+customerno);
				custNoFlag=true;
			}else{		
				CallAlert.callAlert("회원번호 찾기실패: 실패입니다.");
				custNoFlag=false;
			}
		} catch (Exception e) {
			CallAlert.callAlert("에러: 에러입니다.");
		} finally {
			try {
				if(conn!=null) { conn.close(); }
			} catch (SQLException e) {
				CallAlert.callAlert("자원닫기 실패: con 닫기 실패했습니다.");
			} 
		}
		return custNoFlag;
	}		
	public boolean custIDCheck(String id) {//고객 ID중복검사를 통해 중복되면 에러메세지 
		try {
			conn = DBUtility.getConnection();
			sql = new StringBuffer();
			sql.append("SELECT id FROM customertbl WHERE id = ? ");
			psmt = conn.prepareStatement(sql.toString());
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			if (rs.next()) {			
				CallAlert.callAlert("중복된 아이디: 중복된 아이디입니다.");
				idcheckFlag= true;
			}else{			
				CallAlert.callAlert("사용가능한 아이디: 사용가능한 아이디입니다.");
				idcheckFlag= false;
			}
		} catch (Exception e) {
			CallAlert.callAlert("에러: 에러입니다.");
		} finally {
			DBUtility.close(psmt, conn);
		}
		return isException;
	}
	//프로그램 전체 정보 (회원에게 보이는 프로그램 정보- 강사번호가 안보이게함)
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
				ProgramData program= new ProgramData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4),
				rs.getInt(5), rs.getString(6), rs.getInt(7));
				cProgramArrayList.add(program);
			}
		} catch (SQLException e) {
			CallAlert.callAlert("select실패: 데이터베이스 program total 가져오기 실패했습니다.");
			e.printStackTrace();
		}finally {
			//1.6 자원객체를 닫아야한다.
			DBUtility.close(psmt, conn);
		}//end of finally
		return cProgramArrayList;
	}
	// 테이블뷰에서 선택한 프로그램 정보 지우기
		public static int deleteCustomerData(int customerno) {
			String deleteCustomer= "delete from programtbl where code = ?";		
			Connection con= null;		
			PreparedStatement psmt= null;
			int count= 0;
			try {
				con= DBUtility.getConnection();
				psmt= con.prepareStatement(deleteCustomer);
				psmt.setInt(1, customerno);
				
				count= psmt.executeUpdate();
				if(count==0) {
					CallAlert.callAlert("delete 실패: select 쿼리문 실패했습니다.");
					return count;
				}
				
			} catch (SQLException e) {
				CallAlert.callAlert("delete 실패: 데이터베이스 delete 실패했습니다.");
			}finally {
				//1.6 자원객체를 닫아야한다.
				DBUtility.close(psmt, conn);
			}//end of finally
			return count;
		}
}

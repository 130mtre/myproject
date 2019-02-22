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
	// �¶��� ȸ������
	public static int insertCustomerData(String id, String pw, String name, String gender, String birth, String phone, String address, String picture) { 
		StringBuffer insertCustomer = new StringBuffer();
		 insertCustomer.append("call ");
		 insertCustomer.append("insertcustomer(?,?,?,?,?,?,?,?)");
		
		//1.2 �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection conn= null;		
		//1.3 �������� �����ؾ��� Statement�� �������Ѵ�.
		PreparedStatement psmt= null;
		int count=0;
		
		try {
			conn= DBUtility.getConnection();
			
			psmt= conn.prepareStatement(insertCustomer.toString());
			//1.4 �������� ���������͸� �����Ѵ�.
			psmt.setString(1, id);
			psmt.setString(2, pw);
			psmt.setString(3, name);
			psmt.setString(4, gender);
			psmt.setString(5, birth);
			psmt.setString(6, phone);
			psmt.setString(7, address);
			psmt.setString(8, picture);
			
			//1.5 ���������͸� ������ �������� �����϶�.
			count= psmt.executeUpdate();
			if(count==0) {
				CallAlert.callAlert("���� ��������: ���� ������ �����߽��ϴ�.");
				return count;
			}
		} catch (SQLException e) {
			CallAlert.callAlert("���Խ���: �����ͺ��̽� ���� �����߽��ϴ�.");
			e.printStackTrace();
		}finally {
			//1.6 �ڿ���ü�� �ݾƾ��Ѵ�.	
				DBUtility.close(psmt, conn);
		}//end of finally
		return count;
	}	
	//2. ���̺��� ��ü������ ��� �������� �Լ�
	public  static ArrayList<CustomerData> getCustomerTotalData(){
		StringBuffer selectCustomer = new StringBuffer();
		selectCustomer.append("select * from customertbl");		
		Connection conn=null;		
		PreparedStatement psmt= null;
		ResultSet rs= null;
		try {
			conn= DBUtility.getConnection();
			psmt= conn.prepareStatement(selectCustomer.toString());
			rs= psmt.executeQuery(); //����������
			if(rs==null) {
				CallAlert.callAlert("select ����: select ������ �����߽��ϴ�.");
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
			CallAlert.callAlert("select����: �����ͺ��̽� custom total �������� �����߽��ϴ�.");
			e.printStackTrace();
		}finally {
			//1.6 �ڿ���ü�� �ݾƾ��Ѵ�.
			DBUtility.close(psmt, conn);
		}//end of finally
		return custArrayList;
	}	
	public boolean loginSelect(String id, String pw) { //�α���
		try {
			conn = DBUtility.getConnection();
			sql = new StringBuffer();
			//�α����� ���� TextFiled�� �Է��� ������ SQLó��.
			sql.append("SELECT * FROM customertbl WHERE id = ? and pw = ? ");
			psmt = conn.prepareStatement(sql.toString());
			psmt.setString(1, id);
			psmt.setString(2, pw);
			rs = psmt.executeQuery();
			if (rs.next()) {
				isException = true;
				CallAlert.callAlert("����: �����Դϴ�."+rs);
			}else{		
				isException = false;
			}
			return isException;
		} catch (Exception e) {
			CallAlert.callAlert("����: �����Դϴ�.");
			return false;
		} finally {
			DBUtility.close(psmt, conn);
		}
	}	
	public boolean findIDSelect(String customerno, String birth, String phone) { //���̵�ã��
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
				CallAlert.callAlert("����: ���̵�� "+id+" �Դϴ�.");
			}else{		
				isException = false;
			}		
		} catch (Exception e) {
			CallAlert.callAlert("����: �����Դϴ�.");
			return false;
		} finally {
			DBUtility.close(psmt, conn);
		}
		return isException;
	}
	public boolean findPWSelect(String id, String birth, String phone) { //��й�ȣ ã��
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
				CallAlert.callAlert("����: ��й�ȣ�� "+pw+" �Դϴ�.");
			}else{		
				isException = false;
			}
			return isException;
		} catch (Exception e) {
			CallAlert.callAlert("����: �����Դϴ�.");
			return false;
		} finally {
			try {
				if(conn!=null) { conn.close(); }
			} catch (SQLException e) {
				CallAlert.callAlert("�ڿ��ݱ� ����: con �ݱ� �����߽��ϴ�.");
			} 
		}
	}	
	public boolean custNoSelect(String name, String birth, String phone) { // ȸ����ȣã��
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
				CallAlert.callAlert("ȸ����ȣ ã�⼺��: �����Դϴ�."+customerno);
				custNoFlag=true;
			}else{		
				CallAlert.callAlert("ȸ����ȣ ã�����: �����Դϴ�.");
				custNoFlag=false;
			}
		} catch (Exception e) {
			CallAlert.callAlert("����: �����Դϴ�.");
		} finally {
			try {
				if(conn!=null) { conn.close(); }
			} catch (SQLException e) {
				CallAlert.callAlert("�ڿ��ݱ� ����: con �ݱ� �����߽��ϴ�.");
			} 
		}
		return custNoFlag;
	}		
	public boolean custIDCheck(String id) {//�� ID�ߺ��˻縦 ���� �ߺ��Ǹ� �����޼��� 
		try {
			conn = DBUtility.getConnection();
			sql = new StringBuffer();
			sql.append("SELECT id FROM customertbl WHERE id = ? ");
			psmt = conn.prepareStatement(sql.toString());
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			if (rs.next()) {			
				CallAlert.callAlert("�ߺ��� ���̵�: �ߺ��� ���̵��Դϴ�.");
				idcheckFlag= true;
			}else{			
				CallAlert.callAlert("��밡���� ���̵�: ��밡���� ���̵��Դϴ�.");
				idcheckFlag= false;
			}
		} catch (Exception e) {
			CallAlert.callAlert("����: �����Դϴ�.");
		} finally {
			DBUtility.close(psmt, conn);
		}
		return isException;
	}
	//���α׷� ��ü ���� (ȸ������ ���̴� ���α׷� ����- �����ȣ�� �Ⱥ��̰���)
	public static ArrayList<ProgramData> getProgramTotalData() { 
		StringBuffer selectProgram = new StringBuffer();
		selectProgram.append("select * from programtbl");		
		Connection conn=null;		
		PreparedStatement psmt= null;
		ResultSet rs= null;
		try {
			conn= DBUtility.getConnection();
			psmt= conn.prepareStatement(selectProgram.toString());
			rs= psmt.executeQuery(); //����������
			if(rs==null) {
				CallAlert.callAlert("select ����: select ������ �����߽��ϴ�.");
				return null;
			}
			
			while(rs.next()) {
				ProgramData program= new ProgramData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4),
				rs.getInt(5), rs.getString(6), rs.getInt(7));
				cProgramArrayList.add(program);
			}
		} catch (SQLException e) {
			CallAlert.callAlert("select����: �����ͺ��̽� program total �������� �����߽��ϴ�.");
			e.printStackTrace();
		}finally {
			//1.6 �ڿ���ü�� �ݾƾ��Ѵ�.
			DBUtility.close(psmt, conn);
		}//end of finally
		return cProgramArrayList;
	}
	// ���̺�信�� ������ ���α׷� ���� �����
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
					CallAlert.callAlert("delete ����: select ������ �����߽��ϴ�.");
					return count;
				}
				
			} catch (SQLException e) {
				CallAlert.callAlert("delete ����: �����ͺ��̽� delete �����߽��ϴ�.");
			}finally {
				//1.6 �ڿ���ü�� �ݾƾ��Ѵ�.
				DBUtility.close(psmt, conn);
			}//end of finally
			return count;
		}
}

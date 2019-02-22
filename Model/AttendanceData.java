package Model;

public class AttendanceData {
	private int customerno;
	private int code;
	private String name;
	private String gender;
	private String birth;
	private String classfication;
	private String phone;
	private String picture;
	private String attdate;
	public AttendanceData(int customerno, int code, String name, String gender, String birth, String classfication,
			String phone, String picture, String attdate) {
		super();
		this.customerno = customerno;
		this.code = code;
		this.name = name;
		this.gender = gender;
		this.birth = birth;
		this.classfication = classfication;
		this.phone = phone;
		this.picture = picture;
		this.attdate = attdate;
	}
	public int getCustomerno() {
		return customerno;
	}
	public void setCustomerno(int customerno) {
		this.customerno = customerno;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getClassfication() {
		return classfication;
	}
	public void setClassfication(String classfication) {
		this.classfication = classfication;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getAttdate() {
		return attdate;
	}
	public void setAttdate(String attdate) {
		this.attdate = attdate;
	}
	
	
}

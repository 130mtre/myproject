package Model;

public class CustomerData {
	private int customerno;
	private String id;
	private String pw;
	private String name;
	private String gender;
	private String birth;
	private int age;
	private String classfication;
	private String phone;
	private String address;
	private String picture;
	private String joindate;
	
	public CustomerData(){}
	public CustomerData(int customerno, String id, String pw, String name, String gender, String birth, int age,
			String classfication, String phone, String address, String picture, String joindate) {
		super();
		this.customerno = customerno;
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.gender = gender;
		this.birth = birth;
		this.age = age;
		this.classfication = classfication;
		this.phone = phone;
		this.address = address;
		this.picture = picture;
		this.joindate = joindate;
	}
	public CustomerData(String name, String gender, String birth, String phone, String address) {
		super();
		this.name = name;
		this.gender = gender;
		this.birth = birth;
		this.phone = phone;
		this.address = address;
	}
	public CustomerData(String id, String pw, String name, String gender, String birth, String phone, String address,
			String picture) {
		super();
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.gender = gender;
		this.birth = birth;
		this.phone = phone;
		this.address = address;
		this.picture = picture;
	}
	public int getCustomerno() {
		return customerno;
	}
	public void setCustomerno(int customerno) {
		this.customerno = customerno;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getJoindate() {
		return joindate;
	}
	public void setJoindate(String joindate) {
		this.joindate = joindate;
	}
}

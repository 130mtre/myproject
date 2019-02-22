package Controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.CustomerData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JoinController implements Initializable{
	//private static final String String = null;
	@FXML private TextField  jTxtId;
	@FXML private TextField  jName;
	@FXML private Button jBtnIdCheck;
	@FXML private PasswordField  jTxtPw;
	@FXML private PasswordField  jTxtPwConfirm;
	@FXML private TextField  jAddr;
	@FXML private TextField  jAddr2;
	@FXML private DatePicker  jBirth;
	@FXML private TextField  jcustomerNo;
	@FXML private Button  jCustomerCheck;
	@FXML private TextField jPhone;
	@FXML private Button  jBtnCancel;
	@FXML private Button  jBtnJoin;
	@FXML private ImageView  jImageView;
	@FXML private Button  jBtnImage;
	@FXML private ImageView  jBtnImageReset;
	@FXML private ToggleGroup group;
	@FXML private RadioButton fFemale;
	@FXML private RadioButton fMale;
	
	public Stage joinStage;
	public Stage stage;
	
	private File selectFile=null;
	private String fileName="";
	private File imageDirectory= new File("C://images");
	private FileInputStream fis=null;
	private BufferedInputStream bis=null;
	private FileOutputStream fos=null;
	private BufferedOutputStream bos=null;
	
	private CustomerDAO customerDAO;
	private CustomerData customerData;
	private boolean idcheckFlag= true;
	public boolean custNoFlag= false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//inputDecimalFormat(jPhone); // 전화번호 숫자만 입력받기
		jBtnIdCheck.setOnAction(e-> handlerjBtnIdCheck()); //아이디 중복체크
		jCustomerCheck.setOnMouseClicked(e-> handlerCBtnCheck() ); // 회원번호 조회창
		jBtnImage.setOnAction(e-> handlet1BtnImageAction() ); //이미지등록 버튼을 누르면 파일창이 열린다.
		jBtnJoin.setOnAction(e-> handlerJBtnJoinAction() ); //가입버튼 클릭시 이벤트처리
		jBtnCancel.setOnAction(e-> joinStage.close() ); //취소버튼을 누르면 로그인화면으로 돌아감
		customerDAO = new CustomerDAO();
	}
	//-------------------------------------------------------------------------------------------------------------------------------------
	private void handlerjBtnIdCheck() {//아이디 중복체크
		if(jTxtId.getText().isEmpty()) {
			CallAlert.callAlert("경고: 아이디를 입력해주세요.");
			return;
		}
		idcheckFlag= customerDAO.custIDCheck(jTxtId.getText().toString()); //▶
		if(idcheckFlag==true) {	// 중복된 아이디가 있는경우
			jTxtId.clear();	
			return;
		}else if(idcheckFlag==false){	// 중복된 아이디 없음
		}
	}
	private void inputDecimalFormat(TextField jPhone) {// 전화번호 숫자만 입력받기
		// 숫자만 입력(정수만 입력받음)
		DecimalFormat format = new DecimalFormat("###########");
		// 점수 입력시 길이 제한 이벤트 처리
		jPhone.setTextFormatter(new TextFormatter<>(event -> {  
			//입력받은 내용이 없으면 이벤트를 리턴함.
			if (event.getControlNewText().isEmpty()) {return event; }
			//구문을 분석할 시작 위치를 지정함.
				ParsePosition parsePosition = new ParsePosition(0);
				//입력받은 내용과 분석위치를 지정한지점부터 format 내용과 일치한지 분석함.
				Object object = format.parse(event.getControlNewText(), parsePosition); 
			//리턴값이 null 이거나,입력한길이와 구문분석 위치값이 적어버리면(다 분석하지못했음을 뜻함)거나,입력한길이가 4이면(3자리를 넘었음을 뜻함.) 이면 null 리턴함.
			if (object == null || parsePosition.getIndex()<event.getControlNewText().length() || event.getControlNewText().length() == 12) {
			     return null;    
			}else {
			     return event;    
			}   
		}));
	}//end of inputDecimalFormat
	private void handlerCBtnCheck() { // 회원번호 조회창
		try {
			Stage checkStage= new Stage(StageStyle.UTILITY);
			checkStage.initModality(Modality.WINDOW_MODAL);
			checkStage.initOwner(joinStage);
			FXMLLoader loader= new FXMLLoader(getClass().getResource("../View/custNoCheck.fxml"));
			Parent checkRoot = loader.load();
			Scene scene=new Scene(checkRoot);
			//scene.getStylesheets().add(getClass().getResource("../application/cal.css").toString());
			checkStage.setScene(scene);
			checkStage.show();
			
			TextField cTxtName= (TextField) checkRoot.lookup("#cTxtName");
			DatePicker cBirth= (DatePicker) checkRoot.lookup("#cBirth");
			TextField cTxtPhone= (TextField) checkRoot.lookup("#cTxtPhone");
			Button cBtnCheck= (Button) checkRoot.lookup("#cBtnCheck");
			
			cBtnCheck.setOnAction(new EventHandler<ActionEvent>() {		
				@Override
				public void handle(ActionEvent arg0) {
					if (cTxtName.getText().trim().toString().equals("") || cBirth.getValue().toString().equals("") || cTxtPhone.getText().trim().toString().equals("")) { //텍스트필드 빈칸시 경고창
						CallAlert.callAlert("경고: 빈칸을 모두 채워주세요.");	 
						return ;
					}
					checkStage.close();

					//▶
					custNoFlag= customerDAO.custNoSelect(cTxtName.getText().trim().toString(), cBirth.getValue().toString(), cTxtPhone.getText().trim().toString()); 
					if(custNoFlag==true) {
						CallAlert.callAlert("회원번호 찾기성공: 성공입니다.");
						// 회원번호찾기 성공시 jName텍스트필드에 자동 입력
						jcustomerNo.setText(String.valueOf(CustomerDAO.customerno));
						// 회원번호찾기 성공시 이름,생년월일,연락처가 각 텍스트필드에 자동입력
						jName.setText(cTxtName.getText().toString());
						LocalDate cBirthDate = LocalDate.parse(cBirth.getValue().toString());
						jBirth.setValue(cBirthDate);
						jPhone.setText(cTxtPhone.getText().toString());
						
						// 기존회원은 이미 있는 정보는 새로받지 않는다.
						jAddr.setDisable(true);
						jAddr2.setDisable(true);
						fFemale.setDisable(true);
						fMale.setDisable(true);
						
					}else if(custNoFlag==false){
						System.out.println(cBirth.getValue().toString());
						CallAlert.callAlert("회원번호 찾기실패: 실패입니다.");			
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	private void handlet1BtnImageAction() { //이미지등록 버튼을 누르면 파일창이 열린다.
		FileChooser fileChooser= new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Image File","*.png","*.jpg"));
		selectFile= fileChooser.showOpenDialog(joinStage);
		String localURL=null;
		if(selectFile!=null) {
			try {
				localURL= selectFile.toURI().toURL().toString();
			} catch (MalformedURLException e) {		}
		}
		// ▼ false: indicates whether the image is being loaded in the background
		jImageView.setImage(new Image(localURL,false)); // <-- getClass 할 수 없다. images폴더에 들어있는게 아니니까! 
		fileName= selectFile.getName(); // <-- 선택된 파일명을 준다. fileName은 반드시 이미지파일을 선택했을때 값을 유지한다.
	}
	private void handlerJBtnJoinAction() { // 가입버튼 클릭시 이벤트처리 ★★★★★★★★★★★★★★★★★
		imageSave(); //기타: 이미지 C:/images/student12345678912_선택된파일명으로 저장한다.
		 // 입력되지 않은 정보에 따른 오류 출력.
		/*if(jTxtId.getText().toString().equals("") || jTxtPw.getText().toString().equals("") || jTxtPwConfirm.getText().toString().equals("")||
				jAddr.getText().toString().equals("") || jBirth.getValue().toString().equals("") || jPhone.getText().toString().equals("") || 
				jAddr2.getText().toString().equals("")  || group.getSelectedToggle().getUserData().toString().equals("")) {
			CallAlert.callAlert("입력오류: 입력되지않은 정보가 있습니다.");
			return;
		}*/
		if(idcheckFlag==false) {
			try {
				customerData = new CustomerData();
				//정규식을 통해 숫자와 문자만 입력.
				if(!jTxtPw.getText().toString().matches("\\w*")){
					CallAlert.callAlert("비밀번호 규칙 오류: 비밀번호는 숫자와 문자만 입력가능합니다.");
					jTxtPw.setText(null);
					jTxtPw.requestFocus();
					return;
				}	
				String id= jTxtId.getText().toString();
				String pw= jTxtPw.getText().toString();
				String name= jName.getText().toString();
				String gender= "여성"; //group.getSelectedToggle().getUserData().toString();
				String birth= jBirth.getValue().toString();	
				String phone= jPhone.getText().toString();
				String address= "인천광역시 연수구 송도동 123-1"; //jAddr.getText().toString()+" "+jAddr2.getText().toString();
				String picture= jImageView.getImage().toString();
				
				
				int count=CustomerDAO.insertCustomerData(id, pw, name, gender, birth, phone, address, picture);
				
				if(count!=0) {
					CallAlert.callAlert("입력성공: 데이터베이스에 입력되었습니다.");
					joinStage.close();
				}		
			}catch(Exception e) {
				e.printStackTrace();
				return;
			}	
		}else if(idcheckFlag==true) {
			CallAlert.callAlert("경고: 아이디 중복확인을 해주세요.");
			return;
		}//end of else if
	}
	private void imageSave() { //기타: 이미지 C:/images/student12345678912_선택된파일명으로 저장한다.	
		if(!imageDirectory.exists()) {
			imageDirectory.mkdir(); //디렉토리가 생성이 안되어 있으면 폴더를 만든다.
		}
		// 선택된 이미지를 c://images/에 선택된 이미지명으로 저장한다.
		try {
			fis= new FileInputStream(selectFile);
			bis= new BufferedInputStream(fis);
			fileName= "student"+System.currentTimeMillis()+"_"+selectFile.getName();
			fos= new FileOutputStream(imageDirectory.getAbsolutePath()+"\\"+fileName);
			bos= new BufferedOutputStream(fos);
			
			int data=-1;
			while((data= bis.read())!=-1) {
				bos.write(data);
				bos.flush();
			}
		} catch (Exception e) {
			CallAlert.callAlert("이미지저장 에러: C/images/저장파일 에러 점검바람");
		}finally {
				try {
					if(fis != null) {fis.close();}
					if(bis != null) {bis.close();}
					if(fos != null) {fos.close();}
					if(bos != null) {bos.close();}
				}catch (IOException e) { }
		}
	}
	
	
}

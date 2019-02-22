package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import Model.CustomerData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RootController implements Initializable{
	@FXML private Pane main;
	@FXML private Button btnLogin;
	@FXML private Button btnJoin;
	@FXML private TextField lTxtId;
	@FXML private PasswordField lTxtPw;
	@FXML private Pane lAlert;
	
	@FXML private Button btnFindIDPW;
	public Stage clStage;
	public Stage stage;
	private CustomerDAO customerDAO;
	private boolean isException;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		maintimer(); //메인화면 시간지연
		btnFindIDPW.setOnAction(event-> handlerBtnFindIDPW() ); //아이디,비밀번호 찾기
		btnJoin.setOnAction(event-> handlerBtnJoinAction() ); //회원가입 버튼 클릭시
		btnLogin.setOnAction(event-> handlerBtnLoginAction() ); //로그인 버튼 클릭시
		customerDAO = new CustomerDAO(); // database object
	}//end of initialize
	
	private void maintimer() { //메인화면 시간지연
		main.setVisible(true);
		Timer m_timer= new Timer();
		TimerTask m_task= new TimerTask() {
			
			@Override
			public void run() {
				main.setVisible(false);
			}
		};
		m_timer.schedule(m_task, 2200);		
	}
	private void handlerBtnFindIDPW() { //아이디,비밀번호 찾기
		Parent findRoot;
		try {
			Stage findStage= new Stage(StageStyle.UTILITY);
			findStage.initModality(Modality.WINDOW_MODAL);
			findStage.initOwner(stage);
			FXMLLoader loader= new FXMLLoader(getClass().getResource("../View/findIDPW.fxml"));
			findRoot = loader.load();
			Scene scene=new Scene(findRoot);
			findStage.setScene(scene);
			findStage.show();
			
			TextField fidTxtCustNo= (TextField) findRoot.lookup("#fidTxtCustNo");
			DatePicker fIDBirth= (DatePicker) findRoot.lookup("#fIDBirth");
			TextField fidTxtCustPhone= (TextField) findRoot.lookup("#fidTxtCustPhone");
			TextField fpwTxtUserName= (TextField) findRoot.lookup("#fpwTxtUserName");
			DatePicker fPWBirth= (DatePicker) findRoot.lookup("#fPWBirth");
			TextField fpwTxtPhone= (TextField) findRoot.lookup("#fpwTxtPhone");
			
			Button fidBtnCheck= (Button) findRoot.lookup("#fidBtnCheck");
			Button fpwBtnCheck= (Button) findRoot.lookup("#fpwBtnCheck");
			
			fidBtnCheck.setOnAction(new EventHandler<ActionEvent>() { //아이디찾기
				
				@Override
				public void handle(ActionEvent event) {
					if (fidTxtCustNo.getText().toString().equals("") || fIDBirth.getValue().toString().equals("") || fidTxtCustPhone.getText().toString().equals("")) { //텍스트필드 빈칸시 경고창
						CallAlert.callAlert("경고: 빈칸을 모두 채워주세요.");	 
						return ;
					}
					findStage.close();
					
					isException= customerDAO.findIDSelect(fidTxtCustNo.getText().toString(), fIDBirth.getValue().toString(), fidTxtCustPhone.getText().toString());
					if(isException==true) {
						CallAlert.callAlert("아이디 찾기성공: 성공입니다.");
					}else if(isException==false){
						CallAlert.callAlert("아이디 찾기실패: 실패입니다.");			
						System.out.println(fIDBirth.getValue().toString());
					}
				}
			});
			fpwBtnCheck.setOnAction(new EventHandler<ActionEvent>() { //비밀번호찾기				
				@Override
				public void handle(ActionEvent event) {
					if (fpwTxtUserName.getText().toString().equals("") || fPWBirth.getValue().toString().equals("") || fpwTxtPhone.getText().toString().equals("")) { //텍스트필드 빈칸시 경고창
						CallAlert.callAlert("경고: 빈칸을 모두 채워주세요.");	 
						return ;
					}
					findStage.close();
					
					isException= customerDAO.findPWSelect(fpwTxtUserName.getText().toString(), fPWBirth.getValue().toString(), fpwTxtPhone.getText().toString());
					if(isException==true) {
						CallAlert.callAlert("비밀번호 찾기성공: 성공입니다.");
					}else if(isException==false){
						CallAlert.callAlert("비밀번호 찾기실패: 실패입니다.");			
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void handlerBtnJoinAction() {  //회원가입 버튼 클릭시 
		try {
			Stage joinStage=new Stage();	
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/join.fxml"));
			Parent joinRoot=loader.load();
			JoinController joinController=loader.getController();
			joinController.joinStage=joinStage;
			Scene scene=new Scene(joinRoot);
			joinStage.setScene(scene);
			joinStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void handlerBtnLoginAction() { // 로그인 버튼 클릭시
		if (lTxtId.getText().toString().equals("") || lTxtPw.getText().toString().equals("")) { //로그인 빈칸시 경고창
			lAlert.setVisible(true);
			//CallAlert.callAlert("아이디 또는 비밀번호를 입력해주세요.");		// <-- 상단 Pane Visible
			return;
		}else {
			lAlert.setVisible(false);
		}
		//관리자 로그인
		if(lTxtId.getText().equals("root")&&lTxtPw.getText().equals("1111")) { 
			CallAlert.callAlert("관리자로그인 성공: "+lTxtId.getText()+" 님 환영합니다.");					
			try {
				Stage admStage=new Stage();	
				FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/administrator2.fxml"));
				Parent admRoot=loader.load();
				AdmController admController=loader.getController();
				admController.admStage=admStage;
				Scene scene=new Scene(admRoot);
				admStage.setScene(scene);
				stage.close();
				admStage.show();			
				CallAlert.callAlert("화면전환 성공: 메인화면으로 전환되었습니다.");
			}catch(Exception e) {
				CallAlert.callAlert("화면전환 오류: 화면전환에 문제가 있습니다.");
				e.printStackTrace();
			}
		}else { //회원로그인
			isException = customerDAO.loginSelect(lTxtId.getText().toString(), lTxtPw.getText().toString());
			if(isException==true) {
				CallAlert.callAlert("로그인 성공: "+lTxtId.getText()+" 님 환영합니다.");	
				try {
					Stage clStage=new Stage();	
					FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/customer.fxml"));
					Parent clRoot=loader.load();
					CustController custController=loader.getController();
					custController.clStage=clStage;
					Scene scene=new Scene(clRoot);
					scene.getStylesheets().add(getClass().getResource("../application/cal.css").toString());
					clStage.setScene(scene);
					clStage.show();	
					stage.close(); 
				}catch(Exception e) {
					CallAlert.callAlert("화면전환 오류: ====화면전환에 문제가 있습니다.");
					e.printStackTrace();
				}
			}else if(isException==false) {
				CallAlert.callAlert("로그인 실패: 실패입니다");
				lTxtId.clear();
				lTxtPw.clear();
				return;
			}	
		} 
	}//handlerBtnLoginAction

}//end of controller

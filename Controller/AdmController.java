package Controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.javafx.geom.Rectangle;

import Model.AttendanceData;
import Model.CustomerData;
import Model.ProgramData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdmController implements Initializable{
	@FXML private Button btnTimeCheck;
	@FXML private Button aBtnLogout;
	@FXML private Button aBtnNewCust;
	
	@FXML private Button btnHome;
	@FXML private Button btnProgram;
	@FXML private Button btnCustomer;
	@FXML private Button btnAtt;	
	@FXML private Button btnSales;	
	@FXML private Pane aPaneHome;
	@FXML private Pane aPaneProgram;
	@FXML private Pane aPaneCust;
	@FXML private Pane aPaneAtt;
	@FXML private Pane aPaneSales;
	@FXML private TextField aTxtGroup;
	@FXML private TextField aTxtCode;
	@FXML private TextField aTxtTO;
	@FXML private TextField aTxtTuition;
	@FXML private TextField aTxtInstructor;
	@FXML private Button aBtnRegist;
	@FXML private ComboBox<String> aCmbSelect;
	@FXML private ComboBox<String> aCmbClassName;
	@FXML private ComboBox<String> aCmbTarget;
	@FXML private TableView<CustomerData> tblCustView; //회원 정보
	@FXML private TableView<AttendanceData> tblAttView; //회원 출석 정보
	@FXML private TableView<ProgramData> aTblProgram; //프로그램 정보 
	
	private int selectCustomerIndex;
	private int count=0;
	private boolean timcCheckFlag;
	public Stage admStage;
	public Stage stage;
	private AdministratorDAO administratorDAO;
	private CustomerData customerData;
	private ProgramData program;
	ObservableList<CustomerData> custList= FXCollections.observableArrayList(); //회원 정보 List
	ObservableList<AttendanceData> attendanceList= FXCollections.observableArrayList(); //회원 출석 정보 List
	ObservableList<ProgramData> programList= FXCollections.observableArrayList(); //프로그램 정보 List
	
	ObservableList<String> CmbSelectList= FXCollections.observableArrayList();
	ObservableList<String> aCmbClassNameList= FXCollections.observableArrayList();
	ObservableList<String> aCmbTargetList= FXCollections.observableArrayList();
	ArrayList<CustomerData> custArrayList;
	ArrayList<ProgramData> programArrayList;
	ArrayList<AttendanceData> attendanceArrayList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		menuBtnAction(); // 화면전환
		btnTimeCheck.setOnAction(e-> handlerBtnTimeCheck() ); // 출석체크용 열기
		aBtnLogout.setOnAction(e-> handleraBtnLogout() ); // 로그인화면 보여주기
		// aPaneCust 회원 정보 화면▼
		settblCustomer(); // 회원정보 테이블
		settblAttendence(); // 회원 출석정보 테이블
		aBtnNewCust.setOnAction(e-> handleraBtnNewCust() ); // 오프라인 회원가입창
		// aPaneProgram 프로그램 정보 화면▼
		settblProgram(); // 프로그램 정보 테이블
		setComboBoxSetting(); // 콤보박스 프로그램 구분, 프로그램 이름, 대상
		aBtnRegist.setOnAction(e-> handlerABtnRegist()); // 프로그램 정보 입력버튼
		// ----------------------------------------------------------------------------------------
		administratorDAO = new AdministratorDAO(); // database object
	}
	
	//-------------------------------------------------------------------------------------------------------------------------------------------------
	// aPaneProgram 프로그램 정보 화면▼
	private void handlerABtnRegist() { // 프로그램 정보 입력버튼
		String selection= aCmbSelect.getSelectionModel().getSelectedItem().toString();
		String classname= aCmbClassName.getSelectionModel().getSelectedItem().toString();
		String group= aTxtGroup.getText().toString();
		int code= Integer.parseInt(aTxtCode.getText().toString());
		int customerno= Integer.parseInt(aTxtInstructor.getText().toString());
		int to= Integer.parseInt(aTxtTO.getText().toString());
		String target= aCmbTarget.getSelectionModel().getSelectedItem().toString();
		int tuition = Integer.parseInt(aTxtTuition.getText().toString());
		
		ProgramData program= new ProgramData(selection, classname, group, code, customerno, to, target, tuition);		
		int count= administratorDAO.insertProgramData(selection, classname, group, code, customerno, to, target, tuition);
		if(count!=0) {
			//CallAlert.callAlert("입력성공: 데이터베이스에 입력되었습니다.");
			programList.add(program);
		}else {
			CallAlert.callAlert("입력실패: 실패");	
		}
	}
	private void handleraBtnNewCust() { //오프라인 회원가입창
		try {
			Stage offjoinStage=new Stage();	
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/offlinejoin.fxml"));
			Parent offJoinRoot;
			offJoinRoot = loader.load();
			OfflineJoinController offlineJoinController=loader.getController();
			offlineJoinController.offJoinStage=offjoinStage;
			Scene scene=new Scene(offJoinRoot);
			offjoinStage.setScene(scene);
			offjoinStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void handlerBtnTimeCheck() { //출석체크용 열기
		try {
			Stage timeStage= new Stage(StageStyle.UTILITY);
			timeStage.initModality(Modality.WINDOW_MODAL);
			timeStage.initOwner(admStage);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/timer.fxml"));
			Parent timeRoot=loader.load();
			Scene scene=new Scene(timeRoot);
			//scene.getStylesheets().add(getClass().getResource("../application/cal.css").toString());
			timeStage.setScene(scene);
			timeStage.show();
			
			Label jTxtTime= (Label) timeRoot.lookup("#jTxtTime");
			TextField tTxtCustNo= (TextField) timeRoot.lookup("#tTxtCustNo");
			Button tBtnCheck= (Button) timeRoot.lookup("#tBtnCheck");
			
			//여기에 타이머 기능 넣으면 된다.
			Task<Void> task = new Task<Void>() {

		         @Override
		         protected Void call() throws Exception {
		            try {
		               count = 0;
		               while (true) {
		                  count++;
		                  SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		                  String strDate = sdf.format(new Date());
		                  Platform.runLater(() -> {
		                	  jTxtTime.setText(strDate);
		                  });
		                  Thread.sleep(1000);
		               }
		            } catch (InterruptedException e) {
		               Platform.runLater(() -> {
		                  CallAlert.callAlert("타이머문제 : 타이머를 확인하세요");
		               });
		            }
		            return null;
		         }
		      };
		      Thread thread = new Thread(task);
		      thread.setDaemon(true);
		      thread.start();
			
			
			tBtnCheck.setOnAction(new EventHandler<ActionEvent>() { // 체크버튼을 누르면 DB에 출석시간이 등록됨.
				
				@Override
				public void handle(ActionEvent event) { 
					// 출석시간 찍고 출석되었는지 없는 회원번호인지 알림창을준다. << 미완성
					timcCheckFlag= administratorDAO.insertTimeCheck(Integer.parseInt(tTxtCustNo.getText().toString()));
					if(timcCheckFlag==true) {					
						CallAlert.callAlert("출석완료: 출석되었습니다.");
					}else if(timcCheckFlag==false){
						CallAlert.callAlert("출석실패: 출석실패입니다. 회원번호를 확인해주세요.");		
						tTxtCustNo.clear();
					}
				}
			});
			//CallAlert.callAlert("화면전환 성공: 메인화면으로 전환되었습니다.");
		}catch(Exception e) {
			CallAlert.callAlert("화면전환 오류: 화면전환에 문제가 있습니다.");
			e.printStackTrace();
		} 
	}
	private void handleraBtnLogout() { //로그아웃 버튼 클릭시
		Parent root;
		try {
			Stage stage= new Stage();
			FXMLLoader loader= new FXMLLoader(getClass().getResource("../View/login.fxml"));
			root = loader.load();
			RootController rootController=loader.getController();
			rootController.stage=stage;
			Scene scene= new Scene(root);
			stage.setScene(scene);
			admStage.close();
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void settblCustomer() { // 회원정보 테이블
		TableColumn tcCustomerno= tblCustView.getColumns().get(0);
		tcCustomerno.setCellValueFactory(new PropertyValueFactory<>("customerno"));
		tcCustomerno.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcId= tblCustView.getColumns().get(1);
		tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tcId.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcPw= tblCustView.getColumns().get(2);
		tcPw.setCellValueFactory(new PropertyValueFactory<>("pw"));
		tcPw.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcName= tblCustView.getColumns().get(3);
		tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tcName.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcGender= tblCustView.getColumns().get(4);
		tcGender.setCellValueFactory(new PropertyValueFactory<>("gender")); 
		tcGender.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcBirth= tblCustView.getColumns().get(5);
		tcBirth.setCellValueFactory(new PropertyValueFactory<>("birth"));
		tcBirth.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcAge= tblCustView.getColumns().get(6);
		tcAge.setCellValueFactory(new PropertyValueFactory<>("age"));
		tcAge.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcClassfication= tblCustView.getColumns().get(7);
		tcClassfication.setCellValueFactory(new PropertyValueFactory<>("classfication"));
		tcClassfication.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcPhone= tblCustView.getColumns().get(8);
		tcPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
		tcPhone.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcAddress= tblCustView.getColumns().get(9);
		tcAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		tcAddress.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcPicture= tblCustView.getColumns().get(10);
		tcPicture.setCellValueFactory(new PropertyValueFactory<>("picture"));
		tcPicture.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcJoindate= tblCustView.getColumns().get(10);
		tcJoindate.setCellValueFactory(new PropertyValueFactory<>("joindate"));
		tcJoindate.setStyle("-fx-alignment: CENTER");

		tblCustView.setItems(custList);
		
		custArrayList= CustomerDAO.getCustomerTotalData();
		for(CustomerData customer : custArrayList) {
			custList.add(customer);
		}
	}
	private void settblAttendence() { // 회원 출석정보 테이블
		TableColumn tcCustomerno= tblAttView.getColumns().get(0);
		tcCustomerno.setCellValueFactory(new PropertyValueFactory<>("customerno"));
		tcCustomerno.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcCode= tblAttView.getColumns().get(1);
		tcCode.setCellValueFactory(new PropertyValueFactory<>("code"));
		tcCode.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcName= tblAttView.getColumns().get(2);
		tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tcName.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcGender= tblAttView.getColumns().get(3);
		tcGender.setCellValueFactory(new PropertyValueFactory<>("gender")); 
		tcGender.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcAge= tblAttView.getColumns().get(4);
		tcAge.setCellValueFactory(new PropertyValueFactory<>("age"));
		tcAge.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcClassfication= tblAttView.getColumns().get(5);
		tcClassfication.setCellValueFactory(new PropertyValueFactory<>("classfication"));
		tcClassfication.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcPicture= tblAttView.getColumns().get(6);
		tcPicture.setCellValueFactory(new PropertyValueFactory<>("picture"));
		tcPicture.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcAttdate= tblAttView.getColumns().get(7);
		tcAttdate.setCellValueFactory(new PropertyValueFactory<>("attdate"));
		tcAttdate.setStyle("-fx-alignment: CENTER");
		

		tblAttView.setItems(attendanceList);
		
		attendanceArrayList= AdministratorDAO.getAttendanceData();
		for(AttendanceData attendanceData : attendanceArrayList) {
			attendanceList.add(attendanceData);
		}
	}
	private void settblProgram() { // 프로그램 정보 테이블(관리자)
		TableColumn tcSelection= aTblProgram.getColumns().get(0);
		tcSelection.setCellValueFactory(new PropertyValueFactory<>("selection"));
		tcSelection.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcClassname= aTblProgram.getColumns().get(1);
		tcClassname.setCellValueFactory(new PropertyValueFactory<>("classname"));
		tcClassname.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcGroup= aTblProgram.getColumns().get(2);
		tcGroup.setCellValueFactory(new PropertyValueFactory<>("group"));
		tcGroup.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcCode= aTblProgram.getColumns().get(3);
		tcCode.setCellValueFactory(new PropertyValueFactory<>("code"));
		tcCode.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcCustomerno= aTblProgram.getColumns().get(4);
		tcCustomerno.setCellValueFactory(new PropertyValueFactory<>("customerno"));
		tcCustomerno.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcTo= aTblProgram.getColumns().get(5);
		tcTo.setCellValueFactory(new PropertyValueFactory<>("to"));
		tcTo.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcTarget= aTblProgram.getColumns().get(6);
		tcTarget.setCellValueFactory(new PropertyValueFactory<>("target")); 
		tcTarget.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcTuition= aTblProgram.getColumns().get(7);
		tcTuition.setCellValueFactory(new PropertyValueFactory<>("tuition"));
		tcTuition.setStyle("-fx-alignment: CENTER");
		
		aTblProgram.setItems(programList);
		
		programArrayList= AdministratorDAO.getProgramTotalData();
		for(ProgramData program : programArrayList) {
			programList.add(program);
		}
	}
	private void menuBtnAction() { // 화면전환
		/*aPaneHome.toFront();
		btnHome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				aPaneHome.toFront();
			}
		});*/
		btnProgram.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				aPaneProgram.toFront();
			}
		});
		btnCustomer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				aPaneCust.toFront();
			}
		});
		btnAtt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				aPaneAtt.toFront();
			}
		});
		btnSales.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				aPaneSales.toFront();
			}
		});
	}
	private void setComboBoxSetting() { // 콤보박스 프로그램 구분, 프로그램 이름, 대상
		CmbSelectList.addAll("[새벽수영]","[오전수영]","[저녁수영]");
		aCmbClassNameList.addAll("월수금 6:00-6:50","월수금 7:00-7:50","월수금 6:00-6:50",
												   "화목 6:00-6:50","화목 7:00-7:50","화목 6:00-6:50");
		aCmbTargetList.addAll("성인","청소년");
		
		aCmbSelect.setItems(CmbSelectList);
		aCmbClassName.setItems(aCmbClassNameList);
		aCmbTarget.setItems(aCmbTargetList);
	}	
	private void handlerTableViewAction(MouseEvent e) { //  활성화시 처리함수
		int count= CustomerDAO.deleteCustomerData(customerData.getCustomerno());
		if(count!=0) {
			attendanceArrayList.remove(selectCustomerIndex);
			attendanceList.remove(customerData);
			
			CallAlert.callAlert("삭제완료: "+customerData.getCustomerno()+"의 정보가 삭제되었습니다.");
		}else {
			return;
		}
	}
}


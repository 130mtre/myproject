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
	@FXML private TableView<CustomerData> tblCustView; //ȸ�� ����
	@FXML private TableView<AttendanceData> tblAttView; //ȸ�� �⼮ ����
	@FXML private TableView<ProgramData> aTblProgram; //���α׷� ���� 
	
	private int selectCustomerIndex;
	private int count=0;
	private boolean timcCheckFlag;
	public Stage admStage;
	public Stage stage;
	private AdministratorDAO administratorDAO;
	private CustomerData customerData;
	private ProgramData program;
	ObservableList<CustomerData> custList= FXCollections.observableArrayList(); //ȸ�� ���� List
	ObservableList<AttendanceData> attendanceList= FXCollections.observableArrayList(); //ȸ�� �⼮ ���� List
	ObservableList<ProgramData> programList= FXCollections.observableArrayList(); //���α׷� ���� List
	
	ObservableList<String> CmbSelectList= FXCollections.observableArrayList();
	ObservableList<String> aCmbClassNameList= FXCollections.observableArrayList();
	ObservableList<String> aCmbTargetList= FXCollections.observableArrayList();
	ArrayList<CustomerData> custArrayList;
	ArrayList<ProgramData> programArrayList;
	ArrayList<AttendanceData> attendanceArrayList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		menuBtnAction(); // ȭ����ȯ
		btnTimeCheck.setOnAction(e-> handlerBtnTimeCheck() ); // �⼮üũ�� ����
		aBtnLogout.setOnAction(e-> handleraBtnLogout() ); // �α���ȭ�� �����ֱ�
		// aPaneCust ȸ�� ���� ȭ���
		settblCustomer(); // ȸ������ ���̺�
		settblAttendence(); // ȸ�� �⼮���� ���̺�
		aBtnNewCust.setOnAction(e-> handleraBtnNewCust() ); // �������� ȸ������â
		// aPaneProgram ���α׷� ���� ȭ���
		settblProgram(); // ���α׷� ���� ���̺�
		setComboBoxSetting(); // �޺��ڽ� ���α׷� ����, ���α׷� �̸�, ���
		aBtnRegist.setOnAction(e-> handlerABtnRegist()); // ���α׷� ���� �Է¹�ư
		// ----------------------------------------------------------------------------------------
		administratorDAO = new AdministratorDAO(); // database object
	}
	
	//-------------------------------------------------------------------------------------------------------------------------------------------------
	// aPaneProgram ���α׷� ���� ȭ���
	private void handlerABtnRegist() { // ���α׷� ���� �Է¹�ư
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
			//CallAlert.callAlert("�Է¼���: �����ͺ��̽��� �ԷµǾ����ϴ�.");
			programList.add(program);
		}else {
			CallAlert.callAlert("�Է½���: ����");	
		}
	}
	private void handleraBtnNewCust() { //�������� ȸ������â
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
	private void handlerBtnTimeCheck() { //�⼮üũ�� ����
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
			
			//���⿡ Ÿ�̸� ��� ������ �ȴ�.
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
		                  CallAlert.callAlert("Ÿ�̸ӹ��� : Ÿ�̸Ӹ� Ȯ���ϼ���");
		               });
		            }
		            return null;
		         }
		      };
		      Thread thread = new Thread(task);
		      thread.setDaemon(true);
		      thread.start();
			
			
			tBtnCheck.setOnAction(new EventHandler<ActionEvent>() { // üũ��ư�� ������ DB�� �⼮�ð��� ��ϵ�.
				
				@Override
				public void handle(ActionEvent event) { 
					// �⼮�ð� ��� �⼮�Ǿ����� ���� ȸ����ȣ���� �˸�â���ش�. << �̿ϼ�
					timcCheckFlag= administratorDAO.insertTimeCheck(Integer.parseInt(tTxtCustNo.getText().toString()));
					if(timcCheckFlag==true) {					
						CallAlert.callAlert("�⼮�Ϸ�: �⼮�Ǿ����ϴ�.");
					}else if(timcCheckFlag==false){
						CallAlert.callAlert("�⼮����: �⼮�����Դϴ�. ȸ����ȣ�� Ȯ�����ּ���.");		
						tTxtCustNo.clear();
					}
				}
			});
			//CallAlert.callAlert("ȭ����ȯ ����: ����ȭ������ ��ȯ�Ǿ����ϴ�.");
		}catch(Exception e) {
			CallAlert.callAlert("ȭ����ȯ ����: ȭ����ȯ�� ������ �ֽ��ϴ�.");
			e.printStackTrace();
		} 
	}
	private void handleraBtnLogout() { //�α׾ƿ� ��ư Ŭ����
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
	private void settblCustomer() { // ȸ������ ���̺�
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
	private void settblAttendence() { // ȸ�� �⼮���� ���̺�
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
	private void settblProgram() { // ���α׷� ���� ���̺�(������)
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
	private void menuBtnAction() { // ȭ����ȯ
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
	private void setComboBoxSetting() { // �޺��ڽ� ���α׷� ����, ���α׷� �̸�, ���
		CmbSelectList.addAll("[��������]","[��������]","[�������]");
		aCmbClassNameList.addAll("������ 6:00-6:50","������ 7:00-7:50","������ 6:00-6:50",
												   "ȭ�� 6:00-6:50","ȭ�� 7:00-7:50","ȭ�� 6:00-6:50");
		aCmbTargetList.addAll("����","û�ҳ�");
		
		aCmbSelect.setItems(CmbSelectList);
		aCmbClassName.setItems(aCmbClassNameList);
		aCmbTarget.setItems(aCmbTargetList);
	}	
	private void handlerTableViewAction(MouseEvent e) { //  Ȱ��ȭ�� ó���Լ�
		int count= CustomerDAO.deleteCustomerData(customerData.getCustomerno());
		if(count!=0) {
			attendanceArrayList.remove(selectCustomerIndex);
			attendanceList.remove(customerData);
			
			CallAlert.callAlert("�����Ϸ�: "+customerData.getCustomerno()+"�� ������ �����Ǿ����ϴ�.");
		}else {
			return;
		}
	}
}


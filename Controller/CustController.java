package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import Model.CustomerData;
import Model.ProgramData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CustController implements Initializable{  // ȸ�� ������
	// �޷� ��¥ǥ�� Label (6X7=42)
	@FXML private Label lb01; @FXML private Label lb11; @FXML private Label lb21; @FXML private Label lb31;
	@FXML private Label lb41; @FXML private Label lb51; @FXML private Label lb61;
	@FXML private Label lb02; @FXML private Label lb12; @FXML private Label lb22; @FXML private Label lb32;
	@FXML private Label lb42; @FXML private Label lb52; @FXML private Label lb62;
	@FXML private Label lb03; @FXML private Label lb13; @FXML private Label lb23; @FXML private Label lb33;
	@FXML private Label lb43; @FXML private Label lb53; @FXML private Label lb63;
	@FXML private Label lb04; @FXML private Label lb14; @FXML private Label lb24; @FXML private Label lb34;
	@FXML private Label lb44; @FXML private Label lb54; @FXML private Label lb64;
	@FXML private Label lb05; @FXML private Label lb15; @FXML private Label lb25; @FXML private Label lb35;
	@FXML private Label lb45; @FXML private Label lb55; @FXML private Label lb65;
	@FXML private Label lb06; @FXML private Label lb16; @FXML private Label lb26; @FXML private Label lb36;
	@FXML private Label lb46; @FXML private Label lb56; @FXML private Label lb66;
	
	@FXML private Label lbMonth; // �� ǥ��
	@FXML private Button btn011; // �޸�ĭ1
	@FXML private Button btn012; // �޸�ĭ2
	@FXML private Button btn013; // �޸�ĭ3
	
	@FXML private Button btnOk; // �޸𾲱�â(test) ok
	@FXML private Button btnCancel; // �޸𾲱�â(test) cancel
	@FXML private TextArea txtAreaTest; // �޸𾲴� TextArea
	
	@FXML Button custBtn1; //Home
	@FXML Button custBtn2; //My Page
	@FXML Button custBtn3; //My Class
	@FXML Button custBtn4; //������û
	@FXML Button custBtn5; // ��ٱ��� 
	@FXML Button custBtnLogout; // �α׾ƿ�
	
	@FXML Pane custPane1; //Home
	@FXML Pane custPane2; //My Page
	@FXML Pane custPane3; //My Class
	@FXML Pane custPane4; //������û
	
	@FXML TableView<ProgramData> cTblProgram;
	@FXML TableView<ProgramData> cTblSelect; 
	ObservableList<ProgramData> programList= FXCollections.observableArrayList();
	ArrayList<ProgramData> programArrayList;
	
	public Stage clStage;
	public Stage joinStage;
	Stage testStage;
	public  ArrayList<CustomerData> custArrayList;
	public ObservableList<CustomerData> t1ListData= FXCollections.observableArrayList();
	private CustomerDAO customerDAO;	
	Label[] laArray= new Label[42];
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		calendarAction(); //�޷�
		menuBtnAction();	// Pane ȭ����ȯ
		setTblCustProgram(); // ���α׷� ���� ���̺�(ȸ��)
		custBtnLogout.setOnAction(e-> handlercustBtnLogout() ); //�α׾ƿ� ��ư Ŭ����
	}//end of initialize

	/*private void menu1Action() {
		btn011.setText("������ 6����");	
		btn012.setOnAction(new EventHandler<ActionEvent>() {	
			Parent testRoot;
			@Override
			public void handle(ActionEvent event) {
				if(!btn012.isWrapText()) {
					testStage= new Stage();	
				}else if(btn011.isWrapText()) {
					btn012.setDisable(true);	
				}else {
					btn012.setStyle("-fx-background-color: yellow");
					try {
						testStage= new Stage();
						testRoot = FXMLLoader.load(getClass().getResource("../View/test.fxml"));
						Scene testScene= new Scene(testRoot);
						testStage.setScene(testScene);
						testStage.show();
					
						btnOk.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								btn012.setText(txtAreaTest.getText());
								testStage.close();
								btn012.setStyle("-fx-background-color: rgbs(0,0,0,0);");
							}
						});
						btnCancel.setOnAction(e-> {
						testStage.close(); 
						btn012.setStyle("-fx-background-color: rgbs(0,0,0,0);");
						});		
					} catch (IOException e) { }
				}//end of else
			}//end of handle
		});
	}*/

	private void menuBtnAction() { // Pane ȭ����ȯ
		custPane1.toFront();
		custBtn1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				custPane1.toFront();
			}
		});
		custBtn2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				custPane2.toFront();
			}
		});
		custBtn3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				custPane3.toFront();
			}
		});
		custBtn4.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				custPane4.toFront();
			}
		});
	}
	private void setTblCustProgram() { // ���α׷� ���� ���̺�(ȸ��)
		TableColumn tcSelection= cTblProgram.getColumns().get(0);
		tcSelection.setCellValueFactory(new PropertyValueFactory<>("selection"));
		tcSelection.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcClassname= cTblProgram.getColumns().get(1);
		tcClassname.setCellValueFactory(new PropertyValueFactory<>("classname"));
		tcClassname.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcGroup= cTblProgram.getColumns().get(2);
		tcGroup.setCellValueFactory(new PropertyValueFactory<>("group"));
		tcGroup.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcCode= cTblProgram.getColumns().get(3);
		tcCode.setCellValueFactory(new PropertyValueFactory<>("code"));
		tcCode.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcTo= cTblProgram.getColumns().get(4);
		tcTo.setCellValueFactory(new PropertyValueFactory<>("to"));
		tcTo.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcTarget= cTblProgram.getColumns().get(5);
		tcTarget.setCellValueFactory(new PropertyValueFactory<>("target")); 
		tcTarget.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcTuition= cTblProgram.getColumns().get(6);
		tcTuition.setCellValueFactory(new PropertyValueFactory<>("tuition"));
		tcTuition.setStyle("-fx-alignment: CENTER");
		
		cTblProgram.setItems(programList);
		
		programArrayList= AdministratorDAO.getProgramTotalData();
		for(ProgramData program : programArrayList) {
			programList.add(program);
		}
	}
	private void setTblSelectCustProgram() { // ȸ���� ��� ��ư�� Ŭ���ϸ� �� ���̺� ����. <-- ���� �����ȵ�
		TableColumn tcSelection= cTblProgram.getColumns().get(0);
		tcSelection.setCellValueFactory(new PropertyValueFactory<>("selection"));
		tcSelection.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcClassname= cTblProgram.getColumns().get(1);
		tcClassname.setCellValueFactory(new PropertyValueFactory<>("classname"));
		tcClassname.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcGroup= cTblProgram.getColumns().get(2);
		tcGroup.setCellValueFactory(new PropertyValueFactory<>("group"));
		tcGroup.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcCode= cTblProgram.getColumns().get(3);
		tcCode.setCellValueFactory(new PropertyValueFactory<>("code"));
		tcCode.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcCustomerno= cTblProgram.getColumns().get(4);
		tcCustomerno.setCellValueFactory(new PropertyValueFactory<>("customerno"));
		tcCustomerno.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcTo= cTblProgram.getColumns().get(5);
		tcTo.setCellValueFactory(new PropertyValueFactory<>("to"));
		tcTo.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcTarget= cTblProgram.getColumns().get(6);
		tcTarget.setCellValueFactory(new PropertyValueFactory<>("target")); 
		tcTarget.setStyle("-fx-alignment: CENTER");
		
		TableColumn tcTuition= cTblProgram.getColumns().get(7);
		tcTuition.setCellValueFactory(new PropertyValueFactory<>("tuition"));
		tcTuition.setStyle("-fx-alignment: CENTER");
		
		ProgramData selectItem= cTblProgram.getSelectionModel().getSelectedItem();
	}
	private void calendarAction() { //�޷�
		laArray= new Label[] {lb01, lb11, lb21, lb31, lb41, lb51, lb61,
				lb02, lb12, lb22, lb32, lb42, lb52, lb62,
				lb03, lb13, lb23, lb33, lb43, lb53, lb63,
				lb04, lb14, lb24, lb34, lb44, lb54, lb64,
				lb05, lb15, lb25, lb35, lb45, lb55, lb65,
				lb06, lb16, lb26, lb36, lb46, lb56, lb66};
		LocalDateTime ldt=LocalDateTime.now();		
		Calendar cal=Calendar.getInstance();
		int year=ldt.getYear();
		int month = ldt.getMonthValue();
		int day=ldt.getDayOfMonth();
		cal.set(year,month-1,1);		
		int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);			
		int firstday=0;
		int lastDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		switch(dayOfWeek) {
		case 1 : firstday=0; break;
		case 2 : firstday=1; break;
		case 3 : firstday=2; break;
		case 4 : firstday=3; break;
		case 5 : firstday=4; break;
		case 6 : firstday=5; break;
		case 7 : firstday=6; break;
		}	
		int j=1;
		for(int i=firstday;i<=lastDay;i++) {			
			laArray[i].setText(""+j);
			j++;
		}
	}
	private void handlercustBtnLogout() { //�α׾ƿ� ��ư Ŭ����
		Parent root;
		try {
			Stage stage= new Stage();
			FXMLLoader loader= new FXMLLoader(getClass().getResource("../View/login.fxml"));
			root = loader.load();
			RootController rootController=loader.getController();
			rootController.stage=stage;
			Scene scene= new Scene(root);
			stage.setScene(scene);
			clStage.close();
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}//end of controller

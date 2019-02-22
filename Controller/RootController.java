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
		maintimer(); //����ȭ�� �ð�����
		btnFindIDPW.setOnAction(event-> handlerBtnFindIDPW() ); //���̵�,��й�ȣ ã��
		btnJoin.setOnAction(event-> handlerBtnJoinAction() ); //ȸ������ ��ư Ŭ����
		btnLogin.setOnAction(event-> handlerBtnLoginAction() ); //�α��� ��ư Ŭ����
		customerDAO = new CustomerDAO(); // database object
	}//end of initialize
	
	private void maintimer() { //����ȭ�� �ð�����
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
	private void handlerBtnFindIDPW() { //���̵�,��й�ȣ ã��
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
			
			fidBtnCheck.setOnAction(new EventHandler<ActionEvent>() { //���̵�ã��
				
				@Override
				public void handle(ActionEvent event) {
					if (fidTxtCustNo.getText().toString().equals("") || fIDBirth.getValue().toString().equals("") || fidTxtCustPhone.getText().toString().equals("")) { //�ؽ�Ʈ�ʵ� ��ĭ�� ���â
						CallAlert.callAlert("���: ��ĭ�� ��� ä���ּ���.");	 
						return ;
					}
					findStage.close();
					
					isException= customerDAO.findIDSelect(fidTxtCustNo.getText().toString(), fIDBirth.getValue().toString(), fidTxtCustPhone.getText().toString());
					if(isException==true) {
						CallAlert.callAlert("���̵� ã�⼺��: �����Դϴ�.");
					}else if(isException==false){
						CallAlert.callAlert("���̵� ã�����: �����Դϴ�.");			
						System.out.println(fIDBirth.getValue().toString());
					}
				}
			});
			fpwBtnCheck.setOnAction(new EventHandler<ActionEvent>() { //��й�ȣã��				
				@Override
				public void handle(ActionEvent event) {
					if (fpwTxtUserName.getText().toString().equals("") || fPWBirth.getValue().toString().equals("") || fpwTxtPhone.getText().toString().equals("")) { //�ؽ�Ʈ�ʵ� ��ĭ�� ���â
						CallAlert.callAlert("���: ��ĭ�� ��� ä���ּ���.");	 
						return ;
					}
					findStage.close();
					
					isException= customerDAO.findPWSelect(fpwTxtUserName.getText().toString(), fPWBirth.getValue().toString(), fpwTxtPhone.getText().toString());
					if(isException==true) {
						CallAlert.callAlert("��й�ȣ ã�⼺��: �����Դϴ�.");
					}else if(isException==false){
						CallAlert.callAlert("��й�ȣ ã�����: �����Դϴ�.");			
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void handlerBtnJoinAction() {  //ȸ������ ��ư Ŭ���� 
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
	private void handlerBtnLoginAction() { // �α��� ��ư Ŭ����
		if (lTxtId.getText().toString().equals("") || lTxtPw.getText().toString().equals("")) { //�α��� ��ĭ�� ���â
			lAlert.setVisible(true);
			//CallAlert.callAlert("���̵� �Ǵ� ��й�ȣ�� �Է����ּ���.");		// <-- ��� Pane Visible
			return;
		}else {
			lAlert.setVisible(false);
		}
		//������ �α���
		if(lTxtId.getText().equals("root")&&lTxtPw.getText().equals("1111")) { 
			CallAlert.callAlert("�����ڷα��� ����: "+lTxtId.getText()+" �� ȯ���մϴ�.");					
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
				CallAlert.callAlert("ȭ����ȯ ����: ����ȭ������ ��ȯ�Ǿ����ϴ�.");
			}catch(Exception e) {
				CallAlert.callAlert("ȭ����ȯ ����: ȭ����ȯ�� ������ �ֽ��ϴ�.");
				e.printStackTrace();
			}
		}else { //ȸ���α���
			isException = customerDAO.loginSelect(lTxtId.getText().toString(), lTxtPw.getText().toString());
			if(isException==true) {
				CallAlert.callAlert("�α��� ����: "+lTxtId.getText()+" �� ȯ���մϴ�.");	
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
					CallAlert.callAlert("ȭ����ȯ ����: ====ȭ����ȯ�� ������ �ֽ��ϴ�.");
					e.printStackTrace();
				}
			}else if(isException==false) {
				CallAlert.callAlert("�α��� ����: �����Դϴ�");
				lTxtId.clear();
				lTxtPw.clear();
				return;
			}	
		} 
	}//handlerBtnLoginAction

}//end of controller

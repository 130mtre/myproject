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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OfflineJoinController implements Initializable{
	@FXML private TextField offjName;
	@FXML private TextField offjAddr;
	@FXML private DatePicker offjBirth;
	@FXML private ToggleGroup offGroup;
	@FXML private TextField offjPhone;
	@FXML private TextField offjAddr2;
	@FXML private ImageView offjImageView;
	@FXML private Button offjBtnImage;
	@FXML private Button offjBtnJoin;
	@FXML private Button offjBtnCancel;
	
	public Stage offJoinStage;
	//public Stage admStage;
	
	private CustomerDAO customerDAO;
	private AdministratorDAO administratorDAO;
	private CustomerData customerData;
	private boolean idcheckFlag= true;
	public boolean custNoFlag= false;
	ArrayList<CustomerData> custArrayList;
	ObservableList<CustomerData> custList;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		offinputDecimalFormat(offjPhone); // ��ȭ��ȣ ���ڸ� �Է¹ޱ�
		offjBtnJoin.setOnAction(e-> handlerJBtnJoinAction() ); //���Թ�ư Ŭ���� �̺�Ʈó��
		offjBtnCancel.setOnAction(e-> offJoinStage.close() ); //��ҹ�ư�� ������ ������ȭ������ ���ư�
		administratorDAO = new AdministratorDAO();
	}
	//----------------------------------------------------------------------------------------------------------------------------
	private void offinputDecimalFormat(TextField offjPhone) {// ��ȭ��ȣ ���ڸ� �Է¹ޱ�
		// ���ڸ� �Է�(������ �Է¹���)
		DecimalFormat format = new DecimalFormat("###########");
		// ���� �Է½� ���� ���� �̺�Ʈ ó��
		offjPhone.setTextFormatter(new TextFormatter<>(event -> {  
		//�Է¹��� ������ ������ �̺�Ʈ�� ������.
		if (event.getControlNewText().isEmpty()) {return event; }
		//������ �м��� ���� ��ġ�� ������.
			ParsePosition parsePosition = new ParsePosition(0);
			//�Է¹��� ����� �м���ġ�� �������������� format ����� ��ġ���� �м���.
			Object object = format.parse(event.getControlNewText(), parsePosition); 
		//���ϰ��� null �̰ų�,�Է��ѱ��̿� �����м� ��ġ���� ���������(�� �м������������� ����)�ų�,�Է��ѱ��̰� 4�̸�(3�ڸ��� �Ѿ����� ����.) �̸� null ������.
		if (object == null || parsePosition.getIndex()<event.getControlNewText().length() || event.getControlNewText().length() == 12) {
		     return null;    
		}else {
		     return event;    
		}   
		}));
	}
	private void handlerJBtnJoinAction() { // ���Թ�ư Ŭ���� �̺�Ʈó�� �ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
		 // �Էµ��� ���� ������ ���� ���� ���.
		if(offjName.getText().toString().equals("") || offjAddr.getText().toString().equals("") || offjAddr2.getText().toString().equals("")||
				offjBirth.getValue().toString().equals("") || offjPhone.getText().toString().equals("") || 
				offGroup.getSelectedToggle().getUserData().toString().equals("")) {
			CallAlert.callAlert("�Է¿���: �Էµ������� ������ �ֽ��ϴ�.");
			return;
		}
		try {
			String name= offjName.getText().toString();
			String gender= offGroup.getSelectedToggle().getUserData().toString();
			String birth= offjBirth.getValue().toString();
			String phone= offjPhone.getText().toString();
			String address= offjAddr.getText().toString()+" "+offjAddr2.getText().toString();
	
			int count=AdministratorDAO.insertOffCustomerData(name, gender, birth, phone, address);
			
			if(count!=0) {
				CallAlert.callAlert("off�Է¼���: �����ͺ��̽��� �ԷµǾ����ϴ�.");
				custArrayList= CustomerDAO.getCustomerTotalData();
				for(CustomerData customerData : custArrayList) {
					custList.add(customerData);
				}
				offJoinStage.close();
			}		
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}	
	}
}

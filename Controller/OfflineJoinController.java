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
		offinputDecimalFormat(offjPhone); // 전화번호 숫자만 입력받기
		offjBtnJoin.setOnAction(e-> handlerJBtnJoinAction() ); //가입버튼 클릭시 이벤트처리
		offjBtnCancel.setOnAction(e-> offJoinStage.close() ); //취소버튼을 누르면 관리자화면으로 돌아감
		administratorDAO = new AdministratorDAO();
	}
	//----------------------------------------------------------------------------------------------------------------------------
	private void offinputDecimalFormat(TextField offjPhone) {// 전화번호 숫자만 입력받기
		// 숫자만 입력(정수만 입력받음)
		DecimalFormat format = new DecimalFormat("###########");
		// 점수 입력시 길이 제한 이벤트 처리
		offjPhone.setTextFormatter(new TextFormatter<>(event -> {  
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
	}
	private void handlerJBtnJoinAction() { // 가입버튼 클릭시 이벤트처리 ★★★★★★★★★★★★★★★★★
		 // 입력되지 않은 정보에 따른 오류 출력.
		if(offjName.getText().toString().equals("") || offjAddr.getText().toString().equals("") || offjAddr2.getText().toString().equals("")||
				offjBirth.getValue().toString().equals("") || offjPhone.getText().toString().equals("") || 
				offGroup.getSelectedToggle().getUserData().toString().equals("")) {
			CallAlert.callAlert("입력오류: 입력되지않은 정보가 있습니다.");
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
				CallAlert.callAlert("off입력성공: 데이터베이스에 입력되었습니다.");
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

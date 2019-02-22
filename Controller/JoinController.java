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
		//inputDecimalFormat(jPhone); // ��ȭ��ȣ ���ڸ� �Է¹ޱ�
		jBtnIdCheck.setOnAction(e-> handlerjBtnIdCheck()); //���̵� �ߺ�üũ
		jCustomerCheck.setOnMouseClicked(e-> handlerCBtnCheck() ); // ȸ����ȣ ��ȸâ
		jBtnImage.setOnAction(e-> handlet1BtnImageAction() ); //�̹������ ��ư�� ������ ����â�� ������.
		jBtnJoin.setOnAction(e-> handlerJBtnJoinAction() ); //���Թ�ư Ŭ���� �̺�Ʈó��
		jBtnCancel.setOnAction(e-> joinStage.close() ); //��ҹ�ư�� ������ �α���ȭ������ ���ư�
		customerDAO = new CustomerDAO();
	}
	//-------------------------------------------------------------------------------------------------------------------------------------
	private void handlerjBtnIdCheck() {//���̵� �ߺ�üũ
		if(jTxtId.getText().isEmpty()) {
			CallAlert.callAlert("���: ���̵� �Է����ּ���.");
			return;
		}
		idcheckFlag= customerDAO.custIDCheck(jTxtId.getText().toString()); //��
		if(idcheckFlag==true) {	// �ߺ��� ���̵� �ִ°��
			jTxtId.clear();	
			return;
		}else if(idcheckFlag==false){	// �ߺ��� ���̵� ����
		}
	}
	private void inputDecimalFormat(TextField jPhone) {// ��ȭ��ȣ ���ڸ� �Է¹ޱ�
		// ���ڸ� �Է�(������ �Է¹���)
		DecimalFormat format = new DecimalFormat("###########");
		// ���� �Է½� ���� ���� �̺�Ʈ ó��
		jPhone.setTextFormatter(new TextFormatter<>(event -> {  
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
	}//end of inputDecimalFormat
	private void handlerCBtnCheck() { // ȸ����ȣ ��ȸâ
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
					if (cTxtName.getText().trim().toString().equals("") || cBirth.getValue().toString().equals("") || cTxtPhone.getText().trim().toString().equals("")) { //�ؽ�Ʈ�ʵ� ��ĭ�� ���â
						CallAlert.callAlert("���: ��ĭ�� ��� ä���ּ���.");	 
						return ;
					}
					checkStage.close();

					//��
					custNoFlag= customerDAO.custNoSelect(cTxtName.getText().trim().toString(), cBirth.getValue().toString(), cTxtPhone.getText().trim().toString()); 
					if(custNoFlag==true) {
						CallAlert.callAlert("ȸ����ȣ ã�⼺��: �����Դϴ�.");
						// ȸ����ȣã�� ������ jName�ؽ�Ʈ�ʵ忡 �ڵ� �Է�
						jcustomerNo.setText(String.valueOf(CustomerDAO.customerno));
						// ȸ����ȣã�� ������ �̸�,�������,����ó�� �� �ؽ�Ʈ�ʵ忡 �ڵ��Է�
						jName.setText(cTxtName.getText().toString());
						LocalDate cBirthDate = LocalDate.parse(cBirth.getValue().toString());
						jBirth.setValue(cBirthDate);
						jPhone.setText(cTxtPhone.getText().toString());
						
						// ����ȸ���� �̹� �ִ� ������ ���ι��� �ʴ´�.
						jAddr.setDisable(true);
						jAddr2.setDisable(true);
						fFemale.setDisable(true);
						fMale.setDisable(true);
						
					}else if(custNoFlag==false){
						System.out.println(cBirth.getValue().toString());
						CallAlert.callAlert("ȸ����ȣ ã�����: �����Դϴ�.");			
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	private void handlet1BtnImageAction() { //�̹������ ��ư�� ������ ����â�� ������.
		FileChooser fileChooser= new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Image File","*.png","*.jpg"));
		selectFile= fileChooser.showOpenDialog(joinStage);
		String localURL=null;
		if(selectFile!=null) {
			try {
				localURL= selectFile.toURI().toURL().toString();
			} catch (MalformedURLException e) {		}
		}
		// �� false: indicates whether the image is being loaded in the background
		jImageView.setImage(new Image(localURL,false)); // <-- getClass �� �� ����. images������ ����ִ°� �ƴϴϱ�! 
		fileName= selectFile.getName(); // <-- ���õ� ���ϸ��� �ش�. fileName�� �ݵ�� �̹��������� ���������� ���� �����Ѵ�.
	}
	private void handlerJBtnJoinAction() { // ���Թ�ư Ŭ���� �̺�Ʈó�� �ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
		imageSave(); //��Ÿ: �̹��� C:/images/student12345678912_���õ����ϸ����� �����Ѵ�.
		 // �Էµ��� ���� ������ ���� ���� ���.
		/*if(jTxtId.getText().toString().equals("") || jTxtPw.getText().toString().equals("") || jTxtPwConfirm.getText().toString().equals("")||
				jAddr.getText().toString().equals("") || jBirth.getValue().toString().equals("") || jPhone.getText().toString().equals("") || 
				jAddr2.getText().toString().equals("")  || group.getSelectedToggle().getUserData().toString().equals("")) {
			CallAlert.callAlert("�Է¿���: �Էµ������� ������ �ֽ��ϴ�.");
			return;
		}*/
		if(idcheckFlag==false) {
			try {
				customerData = new CustomerData();
				//���Խ��� ���� ���ڿ� ���ڸ� �Է�.
				if(!jTxtPw.getText().toString().matches("\\w*")){
					CallAlert.callAlert("��й�ȣ ��Ģ ����: ��й�ȣ�� ���ڿ� ���ڸ� �Է°����մϴ�.");
					jTxtPw.setText(null);
					jTxtPw.requestFocus();
					return;
				}	
				String id= jTxtId.getText().toString();
				String pw= jTxtPw.getText().toString();
				String name= jName.getText().toString();
				String gender= "����"; //group.getSelectedToggle().getUserData().toString();
				String birth= jBirth.getValue().toString();	
				String phone= jPhone.getText().toString();
				String address= "��õ������ ������ �۵��� 123-1"; //jAddr.getText().toString()+" "+jAddr2.getText().toString();
				String picture= jImageView.getImage().toString();
				
				
				int count=CustomerDAO.insertCustomerData(id, pw, name, gender, birth, phone, address, picture);
				
				if(count!=0) {
					CallAlert.callAlert("�Է¼���: �����ͺ��̽��� �ԷµǾ����ϴ�.");
					joinStage.close();
				}		
			}catch(Exception e) {
				e.printStackTrace();
				return;
			}	
		}else if(idcheckFlag==true) {
			CallAlert.callAlert("���: ���̵� �ߺ�Ȯ���� ���ּ���.");
			return;
		}//end of else if
	}
	private void imageSave() { //��Ÿ: �̹��� C:/images/student12345678912_���õ����ϸ����� �����Ѵ�.	
		if(!imageDirectory.exists()) {
			imageDirectory.mkdir(); //���丮�� ������ �ȵǾ� ������ ������ �����.
		}
		// ���õ� �̹����� c://images/�� ���õ� �̹��������� �����Ѵ�.
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
			CallAlert.callAlert("�̹������� ����: C/images/�������� ���� ���˹ٶ�");
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

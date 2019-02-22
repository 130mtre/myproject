package Model;

public class ProgramData {
	private String selection;
	private String classname;
	private String group;
	private int code;
	private int customerno;
	private int to;
	private String target;
	private int tuition;
	
	
	public ProgramData(String selection, String classname, String group, int code, int to, String target, int tuition) {
		super();
		this.selection = selection;
		this.classname = classname;
		this.group = group;
		this.code = code;
		this.to = to;
		this.target = target;
		this.tuition = tuition;
	}
	public ProgramData(String selection, String classname, String group, int code, int customerno, int to, String target,
			int tuition) {
		super();
		this.selection = selection;
		this.classname = classname;
		this.group = group;
		this.code = code;
		this.customerno = customerno;
		this.to = to;
		this.target = target;
		this.tuition = tuition;
	}
	public String getSelection() {
		return selection;
	}
	public void setSelection(String selection) {
		this.selection = selection;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public int getTuition() {
		return tuition;
	}
	public void setTuition(int tuition) {
		this.tuition = tuition;
	}
	public int getCustomerno() {
		return customerno;
	}
	public void setCustomerno(int customerno) {
		this.customerno = customerno;
	}
	
	
}

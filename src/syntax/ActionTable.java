package syntax;

public class ActionTable {//记录Action动作
	private int state;//当前状态
	private String input;//输入
	private String[] action;//状态转移：规约或移入
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String[] getAction() {
		return action;
	}
	public void setAction(String[] action) {
		this.action = action;
	}

}

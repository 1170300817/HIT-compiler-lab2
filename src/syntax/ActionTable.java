package syntax;

public class ActionTable {//��¼Action����
	private int state;//��ǰ״̬
	private String input;//����
	private String[] action;//״̬ת�ƣ���Լ������
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

package syntax;

public class GotoTable {//״̬ת�ƺ���
	private int state;	//��ǰ״̬
	private String input;//����
	private int nextState;//��һ״̬
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
	public int getNextState() {
		return nextState;
	}
	public void setNextState(int nextState) {
		this.nextState = nextState;
	}

}

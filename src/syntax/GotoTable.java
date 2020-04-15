package syntax;

public class GotoTable {//状态转移函数
	private int state;	//当前状态
	private String input;//输入
	private int nextState;//下一状态
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

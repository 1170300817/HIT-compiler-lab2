package syntax;

//����ʽ��
public class SLRFormula {
	private String beforeString;//���ս��
	private String[] nextString;//��׺������ʽ
	private int flag;//��ʶ������λ��
	public String getBeforeString() {
		return beforeString;
	}
	public void setBeforeString(String beforeString) {
		this.beforeString = beforeString;
	}
	public String[] getNextString() {
		return nextString;
	}
	public void setNextString(String[] nextString) {
		this.nextString = nextString;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	

}

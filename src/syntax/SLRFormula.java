package syntax;

//产生式集
public class SLRFormula {
	private String beforeString;//非终结符
	private String[] nextString;//后缀，产生式
	private int flag;//标识·所在位置
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

package syntax;

//FIRST集的存入 如FIRST(S)=(+,*)

public class FirstTable {
	
	String name;//非终结符
	String[] value;//first集
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getValue() {
		return value;
	}
	public void setValue(String[] value) {
		this.value = value;
	}
	

}

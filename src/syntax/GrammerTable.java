package syntax;

//对文法规则表的内容读入到grammerTable中，之后直接调，
//用于后面first，follow，和slr分析表的计算做准备。
public class GrammerTable {
	
	
	private String name;//文法前面的非终结符
	private String[] value;//产生式
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

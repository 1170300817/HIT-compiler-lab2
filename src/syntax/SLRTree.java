package syntax;

public class SLRTree {//SLR分析后的结果的树状图信息
	
	private String name;//父节点名
	private String[] childId;//子节点
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getChildId() {
		return childId;
	}
	public void setChildId(String[] childId) {
		this.childId = childId;
	}

}

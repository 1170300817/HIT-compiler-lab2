package syntax;

public class SLRTree {//SLR������Ľ������״ͼ��Ϣ
	
	private String name;//���ڵ���
	private String[] childId;//�ӽڵ�
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

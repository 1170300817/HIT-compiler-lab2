package syntax;

//���ķ����������ݶ��뵽grammerTable�У�֮��ֱ�ӵ���
//���ں���first��follow����slr������ļ�����׼����
public class GrammerTable {
	
	
	private String name;//�ķ�ǰ��ķ��ս��
	private String[] value;//����ʽ
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

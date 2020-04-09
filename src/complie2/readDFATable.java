package complie2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import Entity.DFATable;
import Entity.DFATableState;
import Entity.grammerTable;

public class readDFATable {
		public static void main(String[] args) throws Exception {
			new readDFATable().addDFA();
		}
	//�������ķ�����

	  /**
	   * 
	   * ��ȡExcel�����ݣ���һά����洢����һ���и��е�ֵ����ά����洢���Ƕ��ٸ���
	   * 
	   * @param file ��ȡ���ݵ�ԴExcel
	   * @param ignoreRows ��ȡ���ݺ��Ե�������������ͷ����Ҫ���� ���Ե�����Ϊ1
	   * @return ������Excel�����ݵ�����
	   * @throws FileNotFoundException
	   * @throws IOException
	   * 
	   */

	  public static String[][] getData(File file, int ignoreRows)
	      throws FileNotFoundException, IOException {

	    BufferedReader bf = new BufferedReader(new FileReader(file));
	    String str;
	    int row_num = 0;
	    int col_num = 0;
	    String[][] tmpArray = new String[100][100];
	    while ((str = bf.readLine()) != null) {
	      // System.out.println(str);
	      // String[] s = str.split(",");
	      String[] s = str.trim().split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1); // ˫�����ڵĶ��Ų��ָ�
	                                                                                      // ˫������Ķ��Ž��зָ�
	      for (int i = 0; i < s.length; i++) {
	        if (s[i].startsWith("\"") && s[i].endsWith("\"")) {
	          s[i] = s[i].substring(1, s[i].length() - 1);//ȥ��ͷβ
	          s[i] = s[i].replaceAll("\"\"", "\"");//��ԭ����Ϊ��
	        }
	      }
	      // String pattern = "^\\\"";

	      tmpArray[row_num] = s;
	      col_num = s.length;
	      // System.out.println(s[1]);
	      row_num++;
	    }
	    // System.out.println(row_num);
	    // System.out.println(col_num);
	    String[][] returnArray = new String[row_num][col_num];
	    for (int i = 0; i < row_num; i++) {
	      for (int j = 0; j < col_num; j++) {
	        returnArray[i][j] = tmpArray[i][j];
	        // System.out.println(tmpArray[i][j]);
	      }
	    }
	    bf.close();
	    return returnArray;
	  }

	  /**
	   * ȥ���ַ����ұߵĿո�
	   * 
	   * @param str Ҫ������ַ���
	   * @return �������ַ���
	   */
	  public static String rightTrim(String str) {
	    if (str == null) {
	      return "";
	    }
	    int length = str.length();
	    for (int i = length - 1; i >= 0; i--) {
	      if (str.charAt(i) != 0x20) {
	        break;
	      }
	      length--;
	    }
	    return str.substring(0, length);
	  }

	  public DFATable[] addDFA() throws Exception {
	    File file = new File("new1.txt");
	    String[][] result = getData(file, 0);
	    int row_num = result.length;
	    DFATable dfaCellList[] = new DFATable[663];
	    int x = 0;
	    // ��result�����е�ÿһ��Ԫ�أ�Ҳ����DFA���ļ��е�ÿһ�����ӣ���һ��DFACell���󣬴���dfaCellList
	    for (int i = 1; i < row_num; i++) {
	      for (int j = 1; j < result[i].length; j++) {//���ڼ�һ
	        dfaCellList[x] = new DFATable();
	        dfaCellList[x].setState(Integer.parseInt(result[i][0]));
	        String[] strArray = null;
	        strArray = result[0][j].split(" ");
	        dfaCellList[x].setInput(strArray);
	        dfaCellList[x].setNextState(Integer.parseInt(result[i][j]));
	        x = x + 1;
	      }
	    }
	    System.out.println(x);
	    for (int i = 0; i < x; i++) {//x
	      if (dfaCellList[i].getState() == 1) {
	        dfaCellList[i].setType("��ʶ��");
	      }
	      if (dfaCellList[i].getState() >= 2 && dfaCellList[i].getState() <= 7) {
	        dfaCellList[i].setType("����");
	      }
	      if (dfaCellList[i].getState() >= 8 && dfaCellList[i].getState() <= 11) {
	        dfaCellList[i].setType("ע��");
	      }
	      if (dfaCellList[i].getState() >= 12 && dfaCellList[i].getState() <= 18
	          || dfaCellList[i].getState() >= 20 && dfaCellList[i].getState() <= 28) {
	        dfaCellList[i].setType("�����");
	      }
	      if (dfaCellList[i].getState() == 29 || dfaCellList[i].getState() == 19) {
	        dfaCellList[i].setType("���");
	      }
	    }
	    return dfaCellList;
	  }

	  public String[][] showDFA(File file) throws Exception {
	    String[][] result = getData(file, 0);
	    return result;
	  }

	  public DFATableState[] showDFAState() throws Exception {
	    File file = new File("new2.txt");
	    String[][] result = getData(file, 0);
	    int rowLength = result.length;
	    DFATableState state[] = new DFATableState[39];
	    for (int i = 0; i < rowLength; i++) {
	      state[i] = new DFATableState();
	      state[i].setState(Integer.parseInt(result[i][0]));
	      state[i].setFinish(result[i][1].equals("1") ? true : false);
	      state[i].setType(result[i][2]);
	    }
	    return state;
	  }
     
     
     public grammerTable[] Wenfa() throws Exception{
    	 File file = new File("fuzhi.txt");
         String[][] result = getData(file, 0);
         
         Map<String,List> map = new HashMap<String,List>();
        
         int rowLength = result.length;
         for(int i=0;i<rowLength;i++) {
        	 List list = java.util.Arrays.asList(result[i]);
        	 map.put(result[i][0],list);
         }
         grammerTable[] w=new grammerTable[36];
         int i=0;
         Set<String> get = map.keySet(); 
         for (String test:get) {
        	 String[] e=(String[]) map.get(test).toArray();
	          for(int j=1;j<e.length;j++){
	        	  if(!e[j].equals(""))
	        	  {
	        		  w[i]=new grammerTable();
		        	  w[i].setName(test);
		        	  
		        	  String[] d=e[j].split(" ");
	        		//  System.out.println("e "+ j+"buΪ��  ");
	        		  w[i].setValue(d);
		        	//  System.out.println("i=  "+i);
		        	  i++;
	        	  }
	          }
	          
         }
		return w;
     }

}
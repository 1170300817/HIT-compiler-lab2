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
	//新增了文法功能

	  /**
	   * 
	   * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
	   * 
	   * @param file 读取数据的源Excel
	   * @param ignoreRows 读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
	   * @return 读出的Excel中数据的内容
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
	      String[] s = str.trim().split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1); // 双引号内的逗号不分割
	                                                                                      // 双引号外的逗号进行分割
	      for (int i = 0; i < s.length; i++) {
	        if (s[i].startsWith("\"") && s[i].endsWith("\"")) {
	          s[i] = s[i].substring(1, s[i].length() - 1);//去掉头尾
	          s[i] = s[i].replaceAll("\"\"", "\"");//还原“”为“
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
	   * 去掉字符串右边的空格
	   * 
	   * @param str 要处理的字符串
	   * @return 处理后的字符串
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
	    // 对result数组中的每一个元素（也就是DFA表文件中的每一个格子）建一个DFACell对象，存入dfaCellList
	    for (int i = 1; i < row_num; i++) {
	      for (int j = 1; j < result[i].length; j++) {//存在减一
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
	        dfaCellList[i].setType("标识符");
	      }
	      if (dfaCellList[i].getState() >= 2 && dfaCellList[i].getState() <= 7) {
	        dfaCellList[i].setType("常数");
	      }
	      if (dfaCellList[i].getState() >= 8 && dfaCellList[i].getState() <= 11) {
	        dfaCellList[i].setType("注释");
	      }
	      if (dfaCellList[i].getState() >= 12 && dfaCellList[i].getState() <= 18
	          || dfaCellList[i].getState() >= 20 && dfaCellList[i].getState() <= 28) {
	        dfaCellList[i].setType("运算符");
	      }
	      if (dfaCellList[i].getState() == 29 || dfaCellList[i].getState() == 19) {
	        dfaCellList[i].setType("界符");
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
	        		//  System.out.println("e "+ j+"bu为空  ");
	        		  w[i].setValue(d);
		        	//  System.out.println("i=  "+i);
		        	  i++;
	        	  }
	          }
	          
         }
		return w;
     }

}
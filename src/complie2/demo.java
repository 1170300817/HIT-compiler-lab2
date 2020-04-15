package complie2;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.RowSorter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JLabel;

import lexer.DFATable;
import lexer.DFATableState;
import lexer.readDFATable;
import syntax.ActionTable;
import syntax.GotoTable;
import syntax.SLRFormula;
import syntax.SLRTree;
import syntax.GrammerTable;

import java.awt.Font;

public class demo {

	private JFrame frame;
	private JTextArea textArea;//测试输入框
	private DefaultTableModel tokenListTbModel;//词法分析token表
	private DefaultTableModel errorListTbModel;//词法分析错误表
	private DefaultTableModel grammererrorListTbModel;//语法分析错误表
	private DefaultTableModel syntaxListTbModel;//
	private String filepath2;		//选择文件输
	private ArrayList<ActionTable> slrTable;//
	private DefaultTreeModel treeModel;//
	
	// 运算符 operaters
	// String[] operaters={
	// ">",">=","<","<=","==","!=","|","&","||","&&","!","^","+","-","*","/","%","++","--","+=","-=","*=","%="};
	// 界符 divideLine
	// String[] divideLines={
	// ",","=",";","[","]","(",")","{",";","}",".","\"","\'"};
	// 关键字 keywords
	String[] keywords = { "char", "long", "short", "float", "double", "const",
			"boolean", "void", "null", "false", "true",  "int", "do",
			"while", "if", "else", "for", "then", "break", "continue", "class",
			"static", "final",  "return", "signed", "struct",
			"goto", "switch", "case", "default",
			"extern", "sizeof", "typedef", "proc","integer","record","real","call","and","or","not"};
	String[] tokens = { "char", "long", "short", "float", "double", "const",
			"boolean", "void", "null", "false", "true",  "int", "do",
			"while", "if", "else", "for", "then", "break", "continue", "class",
			"static", "final", "return", "signed", "struct",
			 "goto", "switch", "case", "default",
			"extern",  "sizeof", "typedef",   "proc","integer","record","real","call","and","or","not",">", ">=",
			"<", "<=", "==", "!=", "|", "&", "||", "&&", "!", "^", "+", "-",
			"*", "/", "%", "++", "--", "+=", "-=", "*=", "/=", ",", "=", ";",
			"[", "]", "(", ")", "{", "}", ".", "\"", "'"

	};

	/**
	 * Launch the application.
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					demo window = new demo();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws Exception
	 */
	public demo() throws Exception {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws Exception
	 */
	private void initialize() throws Exception {
		frame = new JFrame();//背景面板
		frame.setBounds(100, 100, 1323, 860);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		frame.setLocation((screensize.width - frameSize.width) / 2,
				(screensize.height - frameSize.height) / 2);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();//词法分析内面板1，多余
		panel.setBackground(new Color(211, 211, 211));
		panel.setBounds(0, 0, 1307, 815);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JPanel panel_1 = new JPanel();//语法分析内面板2
		panel_1.setBackground(new Color(102, 153, 204));
		panel_1.setBounds(0, 0, 1308, 819);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		final JPanel panel_2 = new JPanel();//词法分析右侧面板
		panel_2.setBackground(new Color(176, 196, 222));
		panel_2.setBounds(303, 13, 988, 785);
		panel_1.add(panel_2);
		panel_2.setLayout(null);
		
		final JPanel panel_4 = new JPanel();//语法分析右侧面板
		panel_4.setBackground(new Color(176, 196, 222));
		panel_4.setBounds(303, 13, 988, 785);
		panel_1.add(panel_4);
		panel_4.setLayout(null);
		panel_4.setVisible(false);
		

		JPanel panel_3 = new JPanel();//语法树面板
		panel_3.setBackground(new Color(230, 230, 250));
		panel_3.setBounds(697, 70, 277, 702);
		panel_4.add(panel_3);
		panel_3.setLayout(null);
		//创建树对象
		final JTree tree = new JTree();
		tree.setModel(treeModel);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setRowHeight(20);
		JScrollPane scrollPane = new JScrollPane();//语法树可滚动
		scrollPane.setViewportView(tree);
		panel_3.add(scrollPane);
		scrollPane.setBounds(0, 5, 275,697);
		
/**************开始*************************************************/
	        
/*************结束*************************************************/
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
		textArea.setBackground(new Color(220, 220, 220));
		JScrollPane textAreaSP = new JScrollPane();//文件输入区域可滚动
		textAreaSP.setBounds(14, 87, 275, 309);
		panel_1.add(textAreaSP);
		textAreaSP.setViewportView(textArea);

		//选择文件按钮
		final JButton btnNewButton_2 = new JButton("选择文件");
		btnNewButton_2.setFont(new Font("宋体", Font.BOLD, 15));
		btnNewButton_2.setBackground(new Color(169, 169, 169));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); // 设置选择器
				chooser.setMultiSelectionEnabled(true); // 设为多选
				int returnVal = chooser.showOpenDialog(btnNewButton_2);
				if (returnVal == JFileChooser.APPROVE_OPTION) { // 如果符合文件类型
					String filepath = chooser.getSelectedFile()
							.getAbsolutePath(); // 获取绝对路径
					System.out.println(filepath);
					filepath2 = filepath.replaceAll("\\\\", "/");
					File file = new File(filepath2);
					textArea.setText(txt2String(file));
				}
			}
		});
		btnNewButton_2.setBounds(14, 30, 107, 34);
		panel_1.add(btnNewButton_2);

		JButton btnNewButton_1 = new JButton("测试");
		btnNewButton_1.setFont(new Font("宋体", Font.BOLD, 15));
		btnNewButton_1.setBackground(new Color(169, 169, 169));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = textArea.getText();
				String str2 = str.replaceAll("\r|\n", ""); // 这里建议用空格代替，去掉换行符空格
				try {
					ArrayList<String[]> a=lexicalAnalysis(str2);
			        ArrayList<SLRTree> slrTreeArray= slrTest(a,slrTable);
			        /**************开始构建树*************************************************/
			        treeModel= getTreeModel(slrTreeArray,slrTreeArray.size()-1);
					tree.setModel(treeModel);					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(171, 30, 92, 34);
		panel_1.add(btnNewButton_1);
		
		 
		
		// 种别码表格
		tokenListTbModel = new DefaultTableModel(new Object[][] {},
				new String[] { "字符串", "类别", "种别码", "value" });
		RowSorter<DefaultTableModel> token_sorter = new TableRowSorter<DefaultTableModel>(tokenListTbModel);
	
		// first集表格
		GrammerTable[] grammerTable = new readDFATable().Wenfa();
		// 词法分析出错表格
		errorListTbModel = new DefaultTableModel(new Object[][] {},
				new String[] { "出错符号", "出错地方", "出错原因" });
		RowSorter<DefaultTableModel> error_sorter = new TableRowSorter<DefaultTableModel>(errorListTbModel);
		
		// 语法分析出错表格
		grammererrorListTbModel = new DefaultTableModel(new Object[][] {},
				new String[] { "出错符号", "出错地方", "出错原因" });
		RowSorter<DefaultTableModel> SLRerror_sorter = new TableRowSorter<DefaultTableModel>(grammererrorListTbModel);
		
		// 语法分析表格
		syntaxListTbModel = new DefaultTableModel(new Object[][] {},
						new String[] { "规约语句"});

		RowSorter<DefaultTableModel> syntax_sorter = new TableRowSorter<DefaultTableModel>(
						syntaxListTbModel);
		//token表
		JTable tokenListTb = new JTable();
		tokenListTb.setBackground(new Color(230, 230, 250));
		tokenListTb.setFillsViewportHeight(true);
		tokenListTb.setModel(tokenListTbModel);
		tokenListTb.setRowSorter(token_sorter);
		JScrollPane tokenSP = new JScrollPane();
		tokenSP.setBounds(14, 98, 487, 594);
		panel_2.add(tokenSP);
		tokenSP.setViewportView(tokenListTb);
		
		JLabel lblToken = new JLabel("TOKEN TABLE");
		lblToken.setBounds(125, 40, 254, 34);
		panel_2.add(lblToken);
		lblToken.setFont(new Font("宋体", Font.BOLD, 40));
		
		//error表
		JTable errorListTb = new JTable();
		errorListTb.setBackground(new Color(230, 230, 250));
		errorListTb.setFillsViewportHeight(true);
		errorListTb.setModel(errorListTbModel);
		errorListTb.setRowSorter(error_sorter);
		JScrollPane errorSP = new JScrollPane();
		errorSP.setBounds(571, 94, 403, 598);
		panel_2.add(errorSP);
		errorSP.setViewportView(errorListTb);

		JLabel lblErrorTable = new JLabel("ERROR TABLE");
		lblErrorTable.setBounds(676, 37, 248, 41);
		panel_2.add(lblErrorTable);
		lblErrorTable.setFont(new Font("宋体", Font.BOLD, 40));

		//语法分析表，规约语句
		JTable syntaxListTb = new JTable();
		syntaxListTb.setBackground(new Color(230, 230, 250));
		syntaxListTb.setFillsViewportHeight(true);
		syntaxListTb.setModel(syntaxListTbModel);
		syntaxListTb.setRowSorter(syntax_sorter);
		JScrollPane syntaxSP = new JScrollPane();
		syntaxSP.setBounds(46, 72, 611, 314);
		panel_4.add(syntaxSP);
		syntaxSP.setViewportView(syntaxListTb);
		
		JLabel lblAnalysisTable = new JLabel("Analysis TABLE");
		lblAnalysisTable.setBounds(198, 19, 320, 40);
		panel_4.add(lblAnalysisTable);
		lblAnalysisTable.setFont(new Font("宋体", Font.BOLD, 40));
		
		//文法error表
		JTable grammererrorListTb = new JTable();
		grammererrorListTb.setBackground(new Color(230, 230, 250));
		grammererrorListTb.setFillsViewportHeight(true);
		grammererrorListTb.setModel(grammererrorListTbModel);
		grammererrorListTb.setRowSorter(SLRerror_sorter);
		JScrollPane grammererrorSP = new JScrollPane();
		grammererrorSP.setBounds(46, 447, 611, 325);
		panel_4.add(grammererrorSP);
		grammererrorSP.setViewportView(grammererrorListTb);
		
		JLabel lblErrorTable_1 = new JLabel("ERROR TABLE");
		lblErrorTable_1.setFont(new Font("宋体", Font.BOLD, 40));
		lblErrorTable_1.setBounds(219, 395, 254, 40);
		panel_4.add(lblErrorTable_1);

		/**
		 * 面板切换的方法（写在内部类里）
		 * 
		 * @author Administrator
		 *
		 */
		class ControlPanel {
			public void choose(int type) {
				switch (type) {
				case 0:
					panel_4.setVisible(false);
					panel_2.setVisible(true);
					break;
				case 1:
					panel_2.setVisible(false);
					panel_4.setVisible(true);
					break;
				}
			}
		}
		
		JButton button = new JButton("词法分析");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 切换面板
				ControlPanel mcp = new ControlPanel();
				mcp.choose(0);
			}
		});
		
		button.setBackground(new Color(169, 169, 169));
		button.setBounds(73, 423, 150, 40);
		panel_1.add(button);

		JButton button_1 = new JButton("语法分析");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 切换面板
				ControlPanel mcp = new ControlPanel();
				mcp.choose(1);
			}
		});
		button_1.setBackground(new Color(169, 169, 169));
		button_1.setBounds(73, 502, 150, 40);
		panel_1.add(button_1);
			
		JButton btnDfa = new JButton("DFA转换表");
		btnDfa.setBounds(73, 585, 150, 40);
		panel_1.add(btnDfa);
		btnDfa.setBackground(new Color(169, 169, 169));
		btnDfa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DFAPanel window = null;
				try {
					window = new DFAPanel();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				window.frame.setVisible(true);
			}
		});
		
		JButton btnSlr = new JButton("SLR分析表");
		btnSlr.setBounds(73, 672, 150, 40);
		panel_1.add(btnSlr);
		btnSlr.setBackground(new Color(169, 169, 169));
		btnSlr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SLRPanel window = null;
				try {
					window = new SLRPanel();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				window.frame.setVisible(true);
			}
		});
		
		slrTable=getSLRTable(grammerTable);
	}

	// 读文件
	public static String txt2String(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				result.append(s + System.lineSeparator());
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * 词法分析开始*************************************************
	 * 词法分析专用分析器
	 */
	public ArrayList<String[]> lexicalAnalysis(String str) throws Exception {
		while (tokenListTbModel.getRowCount() > 0) {
			tokenListTbModel.removeRow(0);
		}
		while (errorListTbModel.getRowCount() > 0) {
			errorListTbModel.removeRow(0);
		}
		ArrayList<String[]> slrInput2=new ArrayList<String[]>();
		// 将字符串转化为字符数组
		char[] strline = str.toCharArray();
		String currentString = "";
		int currentState = 0; // 当前状态
		int lastState = 0; // 上一状态
		DFATable dfa[] = new readDFATable().addDFA();
		DFATableState DFAstate[] = new readDFATable().showDFAState();

		for (int i = 0; i < strline.length; i++) {
			currentString = currentString.concat(String.valueOf(strline[i]));
			lastState = currentState;

			if (currentString == " " || strline[i] == ' ') {
				currentString = currentString.replaceAll(" ", "");
				if (isKeyword(currentString)) {
					tokenListTbModel.addRow(new Object[] { currentString,
							"关键字", tokenID(currentString), "   " });
					slrInput2.add(new String[]{currentString,"关键字"});
					
				} else {
					tokenListTbModel.addRow(new Object[] {currentString,
							findTypeByState(lastState, DFAstate),
							tokenID(currentString),
							findTypeByState(lastState, DFAstate).equals("注释")
									|| findTypeByState(lastState, DFAstate)
											.equals("运算符")
									|| findTypeByState(lastState, DFAstate)
											.equals("界符") ? "   "
									: currentString });
					slrInput2.add(new String[]{currentString,findTypeByState(lastState, DFAstate)});
				}
				currentString = "";
				currentState = 0;
				continue;
			}
			currentState = stateChange(strline[i], currentState, dfa);
			if (currentState == -1 || currentState == -2){// 现在这个字符在线路上混不下去了
				currentString = currentString.substring(0,
						currentString.length() - 1);
				// 判断当前状态是否为终止状态 如不是 报错
				if (!isFinishByState(lastState, DFAstate)) {
					errorListTbModel.addRow(new Object[] { currentString,
							"第" + i + "字符", "非法字符" });
					tokenListTbModel.addRow(new Object[] { currentString,
							"非法字符", "无", "   " });
				} else {
					if (isKeyword(currentString)) {
						tokenListTbModel.addRow(new Object[] { currentString,
								"关键字", tokenID(currentString), "   " });
						slrInput2.add(new String[]{currentString,"关键字"});
					} else {
						tokenListTbModel.addRow(new Object[] {
								currentString,
								findTypeByState(lastState, DFAstate),
								tokenID(currentString),
								findTypeByState(lastState, DFAstate).equals(
										"注释")
										|| findTypeByState(lastState, DFAstate)
												.equals("运算符")
										|| findTypeByState(lastState, DFAstate)
												.equals("界符") ? "   "
										: currentString });
				
						slrInput2.add(new String[]{currentString,findTypeByState(lastState, DFAstate)});
					}
				}
				if (currentState == -2) {
					
					tokenListTbModel.addRow(new Object[] { strline[i], "非法字符",
							"无", "   " });
					errorListTbModel.addRow(new Object[] { strline[i],
							"第" + i + "字符", "非法字符" });
					i++;
					// errorListTbModel.addRow(new Object[] {"/*", "注释未封闭"});
				}
				currentString = "";
				currentState = 0;
				i--;
			}
			// 最后一个符号
			if (i == strline.length - 1) {
				if (!isFinishByState(lastState, DFAstate)) {
			
					errorListTbModel.addRow(new Object[] { currentString,
							"第" + i + "字符", "非法字符" });
					tokenListTbModel.addRow(new Object[] { currentString,
							"非法字符", "无", "   " });
				} else {
					if (isKeyword(currentString)) {
						tokenListTbModel.addRow(new Object[] { currentString,
								"关键字", tokenID(currentString), "   " });
						slrInput2.add(new String[]{currentString,"关键字"});
					} else {
						tokenListTbModel.addRow(new Object[] {
								currentString,
								findTypeByState(currentState, DFAstate),
								tokenID(currentString),
								findTypeByState(currentState, DFAstate).equals(
										"注释")
										|| findTypeByState(currentState,
												DFAstate).equals("运算符")
										|| findTypeByState(currentState,
												DFAstate).equals("界符") ? "   "
										: currentString });
						slrInput2.add(new String[]{currentString,findTypeByState(currentState, DFAstate)});
					}
				}
				if (currentState == -2) {
					tokenListTbModel.addRow(new Object[] { strline[i], "非法字符",
							"无", "   " });
					errorListTbModel.addRow(new Object[] { strline[i],
							"第" + i + "字符", "非法字符" });
					i++;
				}
			}

		}
		return slrInput2;
	}
	
	/**
	 * 语法分析开始*************************************************
	 */
	public ArrayList<String[]> lexicalAnalysis2(String str) throws Exception {
		ArrayList<String[]> slrInput2=new ArrayList<String[]>();
		char[] strline = str.toCharArray();
		String currentString = "";
		int currentState = 0; // 当前状态
		int lastState = 0; // 上一状态
		DFATable dfa[] = new readDFATable().addDFA();
		DFATableState DFAstate[] = new readDFATable().showDFAState();
		for (int i = 0; i < strline.length; i++) {
			currentString = currentString.concat(String.valueOf(strline[i]));
			lastState = currentState;

			if (currentString == " " || strline[i] == ' ') {
				currentString = currentString.replaceAll(" ", "");
				if (isKeyword(currentString)) {
					slrInput2.add(new String[]{currentString,"关键字"});
					
				} else {
					slrInput2.add(new String[]{currentString,findTypeByState(lastState, DFAstate)});
				}
				currentString = "";
				currentState = 0;
				continue;
			}
			currentState = stateChange(strline[i], currentState, dfa);
			if (currentState == -1 || currentState == -2){// 现在这个字符在线路上混不下去了
				currentString = currentString.substring(0,
						currentString.length() - 1);
				// 判断当前状态是否为终止状态 如不是 报错
				if (!isFinishByState(lastState, DFAstate)) {
					//在这里error表中应添加
					//
					//
				} else {
					if (isKeyword(currentString)) {
						slrInput2.add(new String[]{currentString,"关键字"});
					} else {
						slrInput2.add(new String[]{currentString,findTypeByState(lastState, DFAstate)});
					}
				}
				if (currentState == -2) {
					i++;
				}
				currentString = "";
				currentState = 0;
				i--;
			}
			// 最后一个符号
			if (i == strline.length - 1) {
				if (!isFinishByState(lastState, DFAstate)) {
				} else {
					if (isKeyword(currentString)) {
						slrInput2.add(new String[]{currentString,"关键字"});
					} else {
						slrInput2.add(new String[]{currentString,findTypeByState(currentState, DFAstate)});
					}
				}
				if (currentState == -2) {
					i++;
				}
			}

		}
		return slrInput2;
	}

	public int stateChange(char currentChar, int currentState, DFATable[] dfa) {
		boolean isInput = false;
		for (int i = 0; i < dfa.length; i++) {
			if (isList(dfa[i].getInput(), currentChar)) {
				isInput = true;// 存在该输入
				if (dfa[i].getState() == currentState
						&& dfa[i].getNextState() != -1)// 当前状态一样 输入有 下一状态不为空
														// 依然在当前自动机中
				{
					return dfa[i].getNextState();
				}
			}
		}
		if (isInput == false) {
			return -2;
		}
		// 在状态9 10 待太久没有结束 注释未封闭 报错
		return -1;
	}

	/**
	 * 判断当前串是否在arr列表中
	 * @param arr
	 * @param currentChar
	 * @return
	 */
	public static boolean isList(String[] arr, char currentChar) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(String.valueOf(currentChar))) {
				return true;
			}
		}
		return false;
	}

	// 根据state 返回该状态的类型
	public String findTypeByState(int state, DFATableState[] dfaState) {
		for (int i = 0; i < dfaState.length; i++) {
			if (dfaState[i].getState() == state) {
				return dfaState[i].getType();
			}
		}
		return "error";
	}

	// 根据state 返回该状态的是否为终止状态
	public boolean isFinishByState(int state, DFATableState[] dfaState) {
		for (int i = 0; i < dfaState.length; i++) {
			if (dfaState[i].getState() == state) {
				return dfaState[i].isFinish();
			}
		}
		return true;
	}

	// 判断当前即将要输出的字符串是否为关键字
	public boolean isKeyword(String str) {
		for (int i = 0; i < keywords.length; i++) {
			if (keywords[i].equals(str)) {
				return true;
			}
		}
		return false;
	}

	public int tokenID(String str) {
		boolean flag = false;
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].equals(str)) {
				flag = true;
				return i;
			}
		}
		if (flag == false) {
			tokens = Arrays.copyOf(tokens, tokens.length + 1);
			tokens[tokens.length - 1] = str;
			return tokens.length;
		}
		return -1;
	}

	/**
	 * 文法分析开始*************************************************
	 */

	/**
	 * 获得First集，根据输入的文法表逐个分析得到每个非终结符的First集
	 * 
	 * @para grammerTable为产生式集合
	 * @return first集
	 * @throws Exception
	 */
	public String[][] getFirstGroup(GrammerTable[] grammerTable) throws Exception {
		
		String[][] firstGroup = new String[10][2];
		//String【i】【0】表示非终结符，String【i】【1】表示右侧的first集，利用空格分割
		int x = 0;
		for (int k = 0; k < grammerTable.length; k++) {
			if (!isExitName(firstGroup, grammerTable[k].getName())){// 若该元素不在first集中，则添加进去
				firstGroup[x][0] = grammerTable[k].getName();
				x++;
			}
		}
		//for (int time = 0; time < 3; time++) {//遍历三遍则认为找完了，有待商榷
		boolean skip = false;//是否结束
		while(!skip) {
			skip = true;//是否结束
			for (int k = 0; k < grammerTable.length; k++) {
				int flag = 0;
				for (int i = 0; i < firstGroup.length; i++) {
					if (firstGroup[i][0].equals(grammerTable[k].getName())) {
						flag = i;// 找到当前字符是first集的哪一个
						break;
					}
				}
				String firstValue = grammerTable[k].getValue()[0];
				if (isTerminator(firstValue)){// 第一个是终结符，则直接添加
					if (!isExitValue(firstGroup[flag][1], firstValue)) {
						if (firstGroup[flag][1] == null) {
							firstGroup[flag][1] = firstValue;
							skip = false ;//有更改不结束
						} else{
							String diff = addEx(firstGroup[flag][1],firstValue);
							if(!diff.equals("")) {
								firstGroup[flag][1] += diff;//addEx用空格分隔
								skip = false ;//有更改不结束
							}
						}
					}
				} else {//如果是非终结符，则把该非终结符的first集添加，在这应该判断是否有null
					for (int i = 0; i < firstGroup.length; i++) {
						if (firstGroup[i][0].equals(firstValue)) {
							if (!isExitValue(firstGroup[flag][1],firstGroup[i][1])) {
								if (firstGroup[flag][1] == null) {
									firstGroup[flag][1] = firstGroup[i][1];
									skip = false ;//有更改不结束
								}else{
									String diff = addEx(firstGroup[flag][1],firstGroup[i][1]);
									if(!diff.equals("")) {
										firstGroup[flag][1] += diff;//addEx用空格分隔
										skip = false ;//有更改不结束
									}
								}
							}
						}
					}

				}
			}
		}
		return firstGroup;
	}

	/**
	 * 这个名字是不是已经有了 即first集有了的就不在新加一列了
	 * 
	 * @param first
	 * @param str
	 * @return
	 */
	public boolean isExitName(String[][] first, String str) {
		for (int i = 0; i < first.length; i++) {
			if (first[i][0] == null) {
				continue;
			}
			if (first[i][0].equals(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断当前即将要加入的值是否已存在
	 * 
	 * @param oldFirstValue
	 * @param newFirstValue
	 * @return
	 */
	public boolean isExitValue(String oldFirstValue, String newFirstValue) {
		if (oldFirstValue == null) {
			if (newFirstValue == null) {
				return true;
			}
			return false;
		} else {
			if (newFirstValue == null) {
				return true;
			}
			if (oldFirstValue.equals(newFirstValue)) {
				return true;
			}
			return false;
		}
	}

	/**
	 * 把新的里面旧的没有的字符串添加进旧的 在已有的value中 将新的里面没有的value加入进去
	 * 
	 * @param oldStr
	 * @param newStr
	 * @return a 差异字符
	 */
	public String addEx(String oldStr, String newStr) {
		String[] newFlag = newStr.split(" ");
		String[] oldFlag = oldStr.split(" ");
		String a = "";
		// int[] flag;
		for (int j = 0; j < newFlag.length; j++) {
			boolean flag = false;
			for (int k = 0; k < oldFlag.length; k++) {
				if (newFlag[j].equals(oldFlag[k])) {
					flag = true;
				}
			}
			if (!flag) {
				a += " " + newFlag[j];
			}//返回空格加字符
		}
		return a;
	}

	/**
	 * 判断是否为终结符
	 * 
	 * @param str
	 * @return
	 */
	public boolean isTerminator(String str) {
		char ch = str.toCharArray()[0];
		if (ch >= 'A' && ch <= 'Z') {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获得follow集
	 * 
	 * @throws Exception
	 */
	public String[][] getFollowGroup() throws Exception {
		GrammerTable[] grammerTable = new readDFATable().Wenfa();
		String[][] followGroup = new String[10][2];
		
		int x = 0;
		for (int k = 0; k < grammerTable.length; k++) {
			if (!isExitName(followGroup, grammerTable[k].getName())){// follow集已经存在该元素的first集
				followGroup[x][0] = grammerTable[k].getName();
				if (followGroup[x][0].equals("P")) {//开始符号右侧加入终结符"$"
					followGroup[x][1] = "$";
				}
				x++;
			}
		}
		
		String[][] firstGroup = getFirstGroup(grammerTable);//获得first集，在后面计算follow用
		//for (int time = 0; time < 4; time++){// 重复三次 最好改善为没有改变
		boolean skip = false;
		while(!skip) {
			skip=true;
			for (int k = 0; k < grammerTable.length; k++) {
				// 遍历每一个 对每一个操作 第二部分每遇到一个终结符 判断后面是否为终结符 是则加上 不是判断后面那终结符是否为空
				int y = grammerTable[k].getValue().length;
				for (int i = 0; i < y; i++) {
					if (i == y - 1){// A->aB 最后一个 follow(A)全加入follow（B）中
						if (!isTerminator(grammerTable[k].getValue()[i])) {
							int flag1 = findFlag(followGroup,grammerTable[k].getValue()[i]);// B在follow集的下标
							int flag2 = findFlag(followGroup,grammerTable[k].getName());// A在follow集的下标
		
							// follow(A)全加入follow(B)中
							if (followGroup[flag1][1] == null) {
								followGroup[flag1][1] = followGroup[flag2][1];
								skip = false;
							} else {
								if (followGroup[flag2][1] == null){// A为空
									continue;
								} else {
									String tmp = addEx(followGroup[flag1][1],
											followGroup[flag2][1]);
									if(!tmp.equals("")) {
										followGroup[flag1][1] += tmp;
										skip = false;
									}
								}

							}
						}
						continue;
					}
					//不是最后一个
					if (!isTerminator(grammerTable[k].getValue()[i])){// 当前是非终结符
						int flag1 = findFlag(followGroup,grammerTable[k].getValue()[i]);// B在follow集的下标
						if (isTerminator(grammerTable[k].getValue()[i + 1])){// 后面是终结符  直接将终结符加入到follow（B）后面
							if (followGroup[flag1][1] == null) {
								followGroup[flag1][1] = grammerTable[k].getValue()[i + 1];
								skip = false;
							} else {
								String tmp = addEx(followGroup[flag1][1],
										grammerTable[k].getValue()[i + 1]);
								if (!tmp.equals("")) {
									followGroup[flag1][1] += tmp;
									skip = false;
								}
							}
						} else {// 后面是非终结符 将后面非终结符的first集加入follow集中
							int flag3 = findFlag(firstGroup,grammerTable[k].getValue()[i + 1]);// C在first集的下标
							String[] str = firstGroup[flag3][1].split(" ");//后面非终结符的first集
							boolean isNo = false;//判断first中是否有no
							String newFirst="";
							for (int z = 0; z < str.length; z++){
								if (str[z].equals("no")) {
									isNo = true;
								}
								else{
									newFirst+=" "+str[z];
								}
							}
							if (followGroup[flag1][1] == null) {////把c的first集除空加到x的follow集中
								followGroup[flag1][1] = newFirst;
								skip = false;
							} else {
								if (newFirst==""){// A为空
									continue;
								} else {
									String tmp = addEx(followGroup[flag1][1],newFirst);
									if (!tmp.equals("")) {
										followGroup[flag1][1] += tmp;
										skip = false;
									}
								}
							}
							if (isNo){// 如果有空的 则T->XC,c中有空把c的follow加到x的follow集中并把c的first集除空加到x的follow集中
								//把c的follow加到x的follow集中
								int flag4 = findFlag(followGroup,grammerTable[k].getValue()[i + 1]);
								if (followGroup[flag1][1] == null) {
									followGroup[flag1][1] = followGroup[flag4][1];
									skip = false;
								} else {
									if (followGroup[flag4][1] == null){// A为null
										continue;
									} else {
										String tmp = addEx(followGroup[flag1][1],
												followGroup[flag4][1]);
										if (!tmp.equals("")) {
											followGroup[flag1][1] += tmp;
											skip = false;
										}
									}
								}
							} 
						}
					}
				}
			}
		}
		return followGroup;
	}

	/**
	 * 找value在Group集的下标
	 * 
	 * @param Group
	 * @param value
	 * @return
	 */
	public int findFlag(String[][] Group, String value) {
		for (int j = 0; j < Group.length; j++) {
			if (Group[j][0].equals(value)) {
				return j;
			}
		}
		return -1;
	}

	

	/**
	 * 根据输入 从一个状态到构造另一个新状态
	 * 
	 * @param currentInput 当前输入
	 * @param slrFormulaArray 当前的产生式集合
	 * @return 新的产生式集合
	 */
	public ArrayList<SLRFormula> GOTO(String currentInput,
			ArrayList<SLRFormula> slrFormulaArray) {
		ArrayList<SLRFormula> slrFormulaArray2 = new ArrayList<SLRFormula>();
		//存储新的产生式集
		int count = 0;
		// 遍历产生式集合的每一个产生式 看这个产生式的点后面的是不是与输入相同
		for (int k = 0; k < slrFormulaArray.size(); k++) {
			SLRFormula cur = slrFormulaArray.get(k);
			int flag = cur.getFlag();//获取·的位置
			if (flag < cur.getNextString().length){// 不是规约状态
				if (cur.getNextString()[flag].equals(currentInput)){
					// 相同则加入 flag+1
					SLRFormula slr = new SLRFormula();
					slr.setBeforeString(cur.getBeforeString());
					slr.setFlag(flag + 1);
					slr.setNextString(cur.getNextString());//新的产生式
					slrFormulaArray2.add(count, slr);
					count++;
				}
			}
		}
		//此时应计算项目集闭包
		return slrFormulaArray2;
	}

	/**
	 * 根据已有的状态构造项目闭包
	 * 
	 * @param initFormulaArray
	 *            最开始的项目集闭包
	 * @param nowFormulaArray
	 *            当前的产生式集合
	 * @return 现在的加上添加的产生式集合，返回值与右侧相同
	 */
	// 当前下一个是否是初始状态的beforeString 是则加入
	public ArrayList<SLRFormula> CLOSURE(ArrayList<SLRFormula> initFormulaArray,
			ArrayList<SLRFormula> nowFormulaArray) {
		do {
			int length = nowFormulaArray.size();
			// 遍历当前产生式集合的每一个产生式，看这个产生式的点后面是不是非终结符 是的话找一个一样的
			for (int k = 0; k < length; k++) {
				int flag = nowFormulaArray.get(k).getFlag();// 获取·的位置
				if (flag < nowFormulaArray.get(k).getNextString().length) {// 不是规约状态
					String next = nowFormulaArray.get(k).getNextString()[flag];// 点后面的字符
					if (!isTerminator(next)) {// 不是终结符，则需要寻找等价项目，还得看后面的后面！！！！
						for (int i = 0; i < initFormulaArray.size(); i++) {// 在初始状态找beforeString一样的
							if (initFormulaArray.get(i).getBeforeString().equals(next)) {// 找到相应的加入到产生式集合中
								boolean a = isExitFormula(nowFormulaArray, initFormulaArray.get(i));
								if (!a) {// 若产生式不在项目集中，则加入
									nowFormulaArray.add(length, initFormulaArray.get(i));
								}
							}
						}
					}
				}
			}
			if (length == nowFormulaArray.size()) {
				break;//当没有新的产生式加入时结束
			}
		} while (true);
		return nowFormulaArray;

	}

	/**
	 * 判断当前产生式是否已经在这个状态存在
	 * @param theFormulaArray
	 * @param theFormula
	 * @return
	 */
	public boolean isExitFormula(ArrayList<SLRFormula> theFormulaArray,SLRFormula theFormula){
		
		for(int i=0;i<theFormulaArray.size();i++){
			if(theFormulaArray.get(i).getBeforeString().equals(theFormula.getBeforeString())){
				if(theFormulaArray.get(i).getFlag()==theFormula.getFlag()){
					String[] str1=theFormulaArray.get(i).getNextString();
					String[] str2=theFormula.getNextString();
					StringBuffer sb1 = new StringBuffer();
					StringBuffer sb2 = new StringBuffer();
					for (int y = 0; y < str1.length; y++) {
						sb1.append(str1[y]);
					}
					String s1 = sb1.toString();

					for (int y = 0; y < str2.length; y++) {
						sb2.append(str2[y]);
					}
					String s2 = sb2.toString();
					if (s1.equals(s2)) {
						return true;
					}
					
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断是否存在状态与该状态一模一样 是就不再新建一个状态了
	 * @param allState   已有的所有状态
	 * @param nowFormulaArray 要判断的状态
	 * @return 相同的状态号，没有相同的返回-1
	 */
	public int isExitTheSameState(ArrayList<ArrayList<SLRFormula>> allState,
			ArrayList<SLRFormula> nowFormulaArray) {
		// 对所以状态遍历 找到有相同产生式集合的状态 返回状态序号
		int len = nowFormulaArray.size();
		for (int i = 0; i < allState.size(); i++) {// 第i个状态
			int[] flag = new int[len];
			for (int k = 0; k < len; k++){// 当前产生式集合的第k个产生式
				for (int j = 0; j < allState.get(i).size(); j++) {
					// 状态里的第j个产生式，如何找到就置1
					// 开始下一次循环
					if (allState.get(i).get(j).getBeforeString()
							.equals(nowFormulaArray.get(k).getBeforeString())) {
						if (allState.get(i).get(j).getFlag() == nowFormulaArray
								.get(k).getFlag()) {
							String[] str1 = allState.get(i).get(j)
									.getNextString();
							String[] str2 = nowFormulaArray.get(k)
									.getNextString();
							StringBuffer sb1 = new StringBuffer();
							StringBuffer sb2 = new StringBuffer();
							for (int y = 0; y < str1.length; y++) {
								sb1.append(str1[y]);
							}
							String s1 = sb1.toString();
							for (int y = 0; y < str2.length; y++) {
								sb2.append(str2[y]);
							}
							String s2 = sb2.toString();
							if (s1.equals(s2)) {
								flag[k] = 1;// 第k个已找到
								break;// 跳出继续找下一个
							}
						}
					} else {
						flag[k] = 0;
					}
				}
			}
			boolean f = true;
			for (int x = 0; x < len; x++) {
				if (flag[x] != 1) {
					f = false;
					break;
				}
			}
			if (f&&allState.get(i).size()==len) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 判断两个状态是否一模一样 是就不再新建一个状态了
	 * 
	 * @param allState
	 *            已有的所有状态
	 * @param nowFormulaArray
	 *            要判断的状态
	 * @return 相同的状态号，没有相同的返回-1
	 */
	public boolean isTheSameState(ArrayList<SLRFormula> FormulaArray1,
			ArrayList<SLRFormula> FormulaArray2) {
		// 对所有状态遍历 找到有相同产生式集合的状态 返回状态序号
		int len = FormulaArray2.size();
		int[] flag = new int[len];
			for (int k = 0; k < len; k++){// 当前产生式集合的第k个产生式
				for (int j = 0; j < FormulaArray1.size(); j++) {
					if (FormulaArray1.get(j).getBeforeString()
							.equals(FormulaArray2.get(k).getBeforeString())) {
						if (FormulaArray1.get(j).getFlag() == FormulaArray2
								.get(k).getFlag()) {
							String[] str1 =FormulaArray1.get(j)
									.getNextString();
							String[] str2 = FormulaArray2.get(k)
									.getNextString();
							StringBuffer sb1 = new StringBuffer();
							StringBuffer sb2 = new StringBuffer();
							for (int y = 0; y < str1.length; y++) {
								sb1.append(str1[y]);
							}
							String s1 = sb1.toString();

							for (int y = 0; y < str2.length; y++) {
								sb2.append(str2[y]);
							}
							String s2 = sb2.toString();
							if (s1.equals(s2)) {
								flag[k] = 1;// 第k个已找到
								break;// 跳出继续找下一个
							}
						}
					} else {
						flag[k] = 0;
					}
				}
			}
			boolean f = true;
			for (int x = 0; x < len; x++) {
				if (flag[x] != 1) {
					f = false;
					break;
				}
			}
			if (f&&FormulaArray1.size()==len) {
				return true;
			}
		return false;
	}

	/**
	 * 遇到规约状态 找到规定的表达式
	 * 
	 * @param slrFormula
	 *            当前要判断的为规约状态的产生式
	 * @param grammerTable
	 *            语法表
	 * @return 规约为哪一个语法产生式
	 */
	public int findActionNum(SLRFormula slrFormula, GrammerTable[] grammerTable) {
		for (int i = 0; i < grammerTable.length; i++) {
			if (slrFormula.getBeforeString().equals(grammerTable[i].getName())) {// 开始一样
				boolean flag = true;
				for (int j = 0; j < grammerTable[i].getValue().length; j++) {
					if (!grammerTable[i].getValue()[j].equals(slrFormula
							.getNextString()[j])) {
						flag = false;
						break;
					}
				}
				if (flag) {
					return i;
				}
			}

		}
		return -1;
	}
	
	/**
	 * 根据文法计算所有等价项目集闭包
	 * @param grammerTable
	 * @return
	 * @throws Exception
	 */
	public ArrayList<ActionTable> getSLRTable(GrammerTable[] grammerTable) throws Exception {
		int stateCount = 0;
		// new action表
		ArrayList<ActionTable> actionTable = new ArrayList<ActionTable>();
		ArrayList<ArrayList<SLRFormula>> slrStateArray = new ArrayList<ArrayList<SLRFormula>>();
		// 用来存储全部的等价项目集闭包，以及状态序号
		ArrayList<SLRFormula> slrFormulArray0 = new ArrayList<SLRFormula>();
		// 存储初始的文法
		ArrayList<SLRFormula> slrFormulArray1 = new ArrayList<SLRFormula>();
		// 存储等价的项目集闭包
		String[][] followGroup = getFollowGroup();

		// 初始化 第一个产生式集合
		for (int i = 0; i < grammerTable.length; i++) {
			SLRFormula slrFormula = new SLRFormula();
			slrFormula.setBeforeString(grammerTable[i].getName());
			slrFormula.setNextString(grammerTable[i].getValue());
			slrFormula.setFlag(0);
			slrFormulArray0.add(i, slrFormula);
		}
		slrStateArray.add(stateCount, slrFormulArray0);// 第一个状态
		stateCount++;

		SLRFormula newslrFormula = new SLRFormula();
		newslrFormula.setBeforeString("Z");// 增广式 Z->P
		newslrFormula.setNextString("P".split(" "));
		newslrFormula.setFlag(0);
		slrFormulArray1.add(0, newslrFormula);// 仅有增广式

		CLOSURE(slrStateArray.get(0), slrFormulArray1);// 计算项目集闭包
		slrStateArray.add(stateCount, slrFormulArray1);// 多一个状态
		stateCount++;

		String currentInput = null;// 后续项目集闭包
		// 对每一个状态 针对输入 到下一个状态
		// 初始化状态为1
		for (int i = 1; i <slrStateArray.size(); i++) {// 遍历状态，边遍历，边产生新状态
			//注：此处应为从1开始，因为状态0仅含增广文法Z->P
			for (int j = 0; j < slrStateArray.get(i).size(); j++) {
				//对闭包的产生式遍历，判断其是不是规约状态
				SLRFormula cur = slrStateArray.get(i).get(j);
				int flag = cur.getFlag();// 产生式的标识位
				String cur_str = cur.getBeforeString();//当前产生式的左侧符号
				int local = findFlag(followGroup, cur_str);//找到产生式左侧在follow集中的序号
				if (flag >= cur.getNextString().length) {
					//flag 是最后一个，即是规约状态
					// 把follow中的都规约
					if (cur_str.equals("Z")) {
						//跳转到0号状态
						ActionTable ac = new ActionTable();//加入新动作
						ac.setInput("$");
						ac.setState(i);
						String a = "acc " + 0;
						// 根据当前的式子找到规约式子的序号
						ac.setAction(a.split(" "));
						actionTable.add(ac);
						continue;
					}
					//找到
					String follow[] = followGroup[local][1].split(" ");
					for (int q = 0; q < follow.length; q++) {
						if (follow[q].equals("")) {
							continue;
						}
						ActionTable ac = new ActionTable();
						ac.setInput(follow[q]);
						ac.setState(i);
						int num = findActionNum(cur, grammerTable);
						String a = "r " + num;
						// 根据当前的式子找到规约式子的序号
						ac.setAction(a.split(" "));
						if (!isExitAction(actionTable, ac)) {
							actionTable.add(ac);
						}
					}
					continue;
				}
				// 非规约状态，则构建新的状态，输入为点的下一个字符 这里应有判断是否已输入过 ||之后判断是否有两个一模一样的状态
				currentInput = cur.getNextString()[flag];// 点的下一个字符
				// 是否为空 即 A->no 0
				if (currentInput.equals("no")) {
					// c->no 找到c的follow集 添加规约
					String follow[] = followGroup[local][1].split(" ");
					for (int q = 0; q < follow.length; q++) {
						if (follow[q].equals("")) {
							continue;
						}
						ActionTable act = new ActionTable();
						act.setInput(follow[q]);
						act.setState(i);
						int num = findActionNum(cur, grammerTable);
						String a = "r " + num;
						// 根据当前的式子找到规约式子的序号
						act.setAction(a.split(" "));
						if (!isExitAction(actionTable, act)) {
							actionTable.add(act);
						}
					}
					continue;
				}
				
				// 当前输入不为no，构建产生式的下一个集合，然后添加动作
				ArrayList<SLRFormula> slrFormulaDemo = GOTO(currentInput, slrStateArray.get(i));
				CLOSURE(slrStateArray.get(0), slrFormulaDemo);

				// 这个产生式集合是否已经存在相同的集合了， 是返回状态号 没有返回-1
				int sameState = isExitTheSameState(slrStateArray, slrFormulaDemo);
				if (sameState == -1) {// 判断这个状态是新状态
					slrStateArray.add(stateCount, slrFormulaDemo);// 多一个状态
					stateCount++;
				}
				if (isTerminator(currentInput)) {// 是终结符， 加入action表
					ActionTable act = new ActionTable();
					act.setInput(currentInput);
					act.setState(i);
					String a;
					if (sameState == -1) {
						a = "s " + (stateCount - 1);
					} else {
						a = "s " + sameState;
					}
					act.setAction(a.split(" "));
					if (!isExitAction(actionTable, act)) {
						actionTable.add(act);
					}
				} else {// 为非终结符加入goto表
					ActionTable act = new ActionTable();
					act.setInput(currentInput);
					act.setState(i);
					String a;
					if (sameState == -1) {
						a = "h " + (stateCount - 1);
					} else {
						a = "h " + sameState;
					}
					act.setAction(a.split(" "));
					if (!isExitAction(actionTable, act)) {
						actionTable.add(act);
					}
				}
			}
		}
		actionTable = addError(actionTable, slrStateArray);
		//加入错误处理
		return actionTable;
	}
	
	
	/**
	 * 错误处理程序
	 * @param actionTable
	 * @param slrStateArray
	 * @return
	 */
	public ArrayList<ActionTable> addError(ArrayList<ActionTable> actionTable,ArrayList<ArrayList<SLRFormula>> slrStateArray){
		for(int k=1;k<slrStateArray.size();k++){
			boolean isExit1=false;
			boolean isExit11=false;
			boolean isExit111=false;
			boolean isExit2=false;
			boolean isExit3=false;
			boolean isExit33=false;
			boolean isExit333=false;
			for(int i=0;i<actionTable.size();i++){
				int state = actionTable.get(i).getState();
				String input = actionTable.get(i).getInput();
				if(state==k){
					if(input.equals("+")){
						isExit1=true;
					}
					if(input.equals("*")){
						isExit11=true;
					}
					if(input.equals("$")){
						isExit111=true;
					}
					else if(input.equals(")")){
						isExit2=true;
					}
					if(input.equals("id")||input.equals("digit")||input.equals("(")){
						isExit3=true;
					}
					if(input.equals("digit")){
						isExit33=true;
					}
					if(input.equals("(")){
						isExit333=true;
					}			
				}		
			}
			if(!isExit1){
				ActionTable table=new ActionTable();
				table.setState(k);
				table.setInput("+");
				String a = "e " +1;
				table.setAction(a.split(" "));
				actionTable.add(table);
			}
			if(!isExit11){
				ActionTable table2=new ActionTable();
				table2.setState(k);
				table2.setInput("*");
				String b = "e " +1;
				table2.setAction(b.split(" "));
				actionTable.add(table2);
			}
			if(!isExit111){
				ActionTable table3=new ActionTable();
				table3.setState(k);
				table3.setInput("$");
				String c = "e " +1;
				table3.setAction(c.split(" "));
				actionTable.add(table3);
			}
			if(!isExit2){
				ActionTable table=new ActionTable();
				table.setState(k);
				table.setInput(")");
				String a = "e " +2;
				table.setAction(a.split(" "));
				actionTable.add(table);
			}
			if(!isExit3){
				ActionTable table=new ActionTable();
				table.setState(k);
				table.setInput("id");
				String a = "e " +3;
				table.setAction(a.split(" "));
				actionTable.add(table);
			}
			if(!isExit33){
				ActionTable table2=new ActionTable();
				table2.setState(k);
				table2.setInput("digit");
				String b = "e " +3;
				table2.setAction(b.split(" "));
				actionTable.add(table2);
			}
			if(!isExit333){
				ActionTable table3=new ActionTable();
				table3.setState(k);
				table3.setInput("(");
				String c = "e " +3;
				table3.setAction(c.split(" "));
				actionTable.add(table3);
			}
		}
		return actionTable;
	}
	

	
	/**
	 * 判断是否存在动作，仅有当前状态，下一输入，动作都相等才返回true
	 * @param actionTable
	 * @param action
	 * @return
	 */
	public boolean isExitAction(ArrayList<ActionTable> actionTable,ActionTable action){
		for(int i=0;i<actionTable.size();i++){
			if(actionTable.get(i).getState()==action.getState()
					&&actionTable.get(i).getInput().equals(action.getInput())){
				String[] str1 =actionTable.get(i).getAction();
				StringBuffer sb1 = new StringBuffer();
				for (int y = 0; y < str1.length; y++) {
					sb1.append(str1[y]);
				}
				String s1 = sb1.toString();
				String[] str2 =action.getAction();
				StringBuffer sb2 = new StringBuffer();
				for (int y = 0; y < str2.length; y++) {
					sb2.append(str2[y]);
				}
				String s2 = sb2.toString();
				if(s2.equals(s1)){
					return true;
				}
			}
		}
        return false;
	}
	
	public boolean isExitGoto(ArrayList<GotoTable> gotoTable ,GotoTable g){
		for(int i=0;i<gotoTable.size();i++){
			if(gotoTable.get(i).getState()==g.getState()
					&&gotoTable.get(i).getInput().equals(g.getInput())
					&&g.getNextState()==gotoTable.get(i).getNextState()){
					return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据当前状态和输入在slr分析表找到下一个操作是规约还是移进
	 * @param slrTable
	 * @param state
	 * @param input
	 * @return
	 */
	public String[] findNext (ArrayList<ActionTable> slrTable,int state,String input) {
		String[] a=new String[2];
		if(state==77){//77有两个，极有可能是规约，有可能是移入
			for(int i=slrTable.size()-1;i>=0;i--){
				if(slrTable.get(i).getState()==state&&slrTable.get(i).getInput().equals(input)){
					for(int j=0;j<slrTable.get(i).getAction().length;j++){
						a[j]=slrTable.get(i).getAction()[j];
					}
					return a; 
				}
			}
		}
		for(int i=0;i<slrTable.size();i++){
			if(slrTable.get(i).getState()==state&&slrTable.get(i).getInput().equals(input)){
				for(int j=0;j<slrTable.get(i).getAction().length;j++){
					a[j]=slrTable.get(i).getAction()[j];
				}
				return a; 
			}
		}
		return a;
	}
	
	
	/**
	 * @param input
	 * @param slrTable
	 * @return
	 * @throws Exception
	 */
	public ArrayList<SLRTree> slrTest(ArrayList<String[]> input,ArrayList<ActionTable> slrTable) throws Exception{
		while (syntaxListTbModel.getRowCount() > 0) {
			syntaxListTbModel.removeRow(0);
		}
		while (grammererrorListTbModel.getRowCount() > 0) {
			grammererrorListTbModel.removeRow(0);
		}
		ArrayList<SLRTree> slrTreeArray=new ArrayList<SLRTree>();
		String inputStr=" ";
		//ArrayList<String> output=new ArrayList<String>();
		for (int i = 0; i < input.size(); i++) {
			SLRTree slrTree = new SLRTree();
			slrTree.setName(input.get(i)[0]);
			String a = "-1";
			String[] b = a.split(" ");
			slrTree.setChildId(b);
			slrTreeArray.add(slrTree);
		}
		for (int i = 0; i < input.size(); i++) {
			if (input.get(i)[0].equals("$") || isKeyword(input.get(i)[0])) {
				// call interger real and not or proc record
				inputStr += input.get(i)[0] + " ";
			} else if (input.get(i)[1].equals("关键字") || input.get(i)[1].equals("标识符")) {
				inputStr += "id" + " ";
				SLRTree slrTree2 = new SLRTree();
				slrTree2.setName("id");
				String aa = String.valueOf(i);
				String[] bb = aa.split(" ");
				slrTree2.setChildId(bb);
				slrTreeArray.add(slrTree2);
			} else if (input.get(i)[1].indexOf("数") != -1) {
				inputStr += "digit" + " ";
				SLRTree slrTree2 = new SLRTree();
				slrTree2.setName("digit");
				String aa = String.valueOf(i);
				String[] bb = aa.split(" ");
				slrTree2.setChildId(bb);
				slrTreeArray.add(slrTree2);
			}
			/*
			 * else if(input.get(i)[1].equals("运算符")) { inputStr +="relop"+" "; }
			 */
			else if (input.get(i)[1].equals("注释")) {
			} else {
				inputStr += input.get(i)[0] + " ";
			}
		}
		System.out.println("inputStr是" + inputStr);
		GrammerTable[] grammerTable = new readDFATable().Wenfa();
		// 用于标记树的栈
		Stack<Integer> treeStack = new Stack<Integer>();
		treeStack.push(-1);
		// 状态栈
		Stack<Integer> stateStack = new Stack<Integer>();
		stateStack.push(1);
		// 符号栈
		Stack<String> charStack = new Stack<String>();
		charStack.push("$");
		// 输入栈
		Stack<String> inputStack = new Stack<String>();
		inputStack.push("$");

		String[] inputStr2 = inputStr.split(" ");
		for (int i = inputStr2.length - 1; i > 0; i--) {
			inputStack.push(inputStr2[i]);
		}
		System.out.println();
		/******************** 初始化完成 ***********************/
		System.out.println("测试开始======================");
		int sign = 0;

		do {
			String next[] = findNext(slrTable, stateStack.peek(), inputStack.peek());
			if (next[0] == null) {
				break;
			}
			if (next[0].equals("acc")) {
				System.out.println("成功！！！！！！！！！！！！！！");
				break;
			}
			// s 3移进 输入栈第一个到符号栈最上面 ， 状态栈为下一个状态
			if (next[0].equals("s")) {
				charStack.push(inputStack.peek());
				inputStack.pop();
				treeStack.push(getFatherId(slrTreeArray, sign));
				sign = sign + 1;
				stateStack.push(Integer.parseInt(next[1]));

			}
			// r 3 规约 输入栈第一个到符号栈最上面 ， 状态栈为下一个状态
			else if (next[0].equals("r")) {// 这里要画树

				int num = Integer.parseInt(next[1]);
				String out = "";
				String tree = "";
				if (!grammerTable[num].getValue()[0].equals("no")) {
					int len = grammerTable[num].getValue().length;
					for (int k = 0; k < len; k++) {
						out = charStack.peek() + " " + out;
						tree = tree + treeStack.peek() + " ";
						charStack.pop();
						stateStack.pop();
						treeStack.pop();
					}
				}
				if (tree.equals("")) {
					tree = "-1";
				}
				SLRTree slrTree = new SLRTree();
				slrTree.setName(grammerTable[num].getName());
				String a = tree;
				String[] b = a.split(" ");
				slrTree.setChildId(b);
				slrTreeArray.add(slrTree);

				syntaxListTbModel.addRow(new Object[] { grammerTable[num].getName() + "->" + out });
				charStack.push(grammerTable[num].getName());
				String next2[] = findNext(slrTable, stateStack.peek(), charStack.peek());
				stateStack.push(Integer.parseInt(next2[1]));
				treeStack.push(slrTreeArray.size() - 1);
			} else if (next[0].equals("e")) {
				int num = Integer.parseInt(next[1]);
				System.out.println("出错 " + num);
				if (num == 2) {
					grammererrorListTbModel
							.addRow(new Object[] { "第" + getLine(sign) + "行错误", inputStack.peek(), "不匹配右括号" });
					inputStack.pop();
				} else if (num == 1) {
					grammererrorListTbModel
							.addRow(new Object[] { "第" + getLine(sign) + "行错误", inputStack.peek(), "缺少运算分量" });
					inputStack.push("id");
				} else if (num == 3) {
					grammererrorListTbModel
							.addRow(new Object[] { "第" + getLine(sign) + "行错误", inputStack.peek(), "缺少运算符" });
					System.out.println("第" + getLine(sign) + "行错误" + inputStack.peek() + "缺少运算符");
					inputStack.push("+");
				}
			}
		} while (true);
		return slrTreeArray;
	}
	
	//根据子节点id返回父节点id，若无父节点，则返回其本身id
	public int getFatherId(ArrayList<SLRTree> slrTreeArray,int childId){
		for(int i=0;i<slrTreeArray.size();i++){
			for(int j=0;j<slrTreeArray.get(i).getChildId().length;j++){
				if(slrTreeArray.get(i).getChildId()[j].equals(String.valueOf(childId))){
					return i;
				}
			 }
		 }
		return childId;
	}
	
	/**
	 * 获得树模型，并画出
	 * @param slrTreeArray
	 * @param num
	 * @return
	 */
	public DefaultTreeModel getTreeModel(ArrayList<SLRTree> slrTreeArray,int num){
		String fatherStr=slrTreeArray.get(num).getName();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(fatherStr);
		DefaultTreeModel treeModel1 = new DefaultTreeModel(root);
		for(int j=0;j<slrTreeArray.get(num).getChildId().length;j++){
			int childID=Integer.parseInt(slrTreeArray.get(num).getChildId()[j]);
			if(childID!=-1){
				String childName=slrTreeArray.get(childID).getName();
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childName);
					treeModel1.insertNodeInto(
						getMutableTreeNode(childNode,childID,slrTreeArray),
						root,j);
			}
		}
		return treeModel1;
	}
	
	/**
	 * 获得树结点
	 * @param fathNode
	 * @param num
	 * @param slrTreeArray
	 * @return
	 */
	public MutableTreeNode getMutableTreeNode(MutableTreeNode fathNode,int num,ArrayList<SLRTree> slrTreeArray){

		for(int j=0;j<slrTreeArray.get(num).getChildId().length;j++){
			int childID=Integer.parseInt(slrTreeArray.get(num).getChildId()[j]);
			if(childID!=-1){
				String childName=slrTreeArray.get(childID).getName();
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childName);
				MutableTreeNode childchildNode=getMutableTreeNode(childNode,childID,slrTreeArray);
				fathNode.insert(childchildNode,fathNode.getChildCount());
			}
		}
		return fathNode;
	}
	
	//根据count返回他所在的行数
	public int getLine(int count) throws Exception{
		String str=textArea.getText();
		String[] a=str.split("\n");
		int len=0;
		for(int i=0;i<a.length;i++){
			len+=lexicalAnalysis2(a[i]).size();
			if(len>count){
				return i+1;
			}
		}
		return 0;
	}
}

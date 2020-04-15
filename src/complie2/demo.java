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
	private JTextArea textArea;//���������
	private DefaultTableModel tokenListTbModel;//�ʷ�����token��
	private DefaultTableModel errorListTbModel;//�ʷ����������
	private DefaultTableModel grammererrorListTbModel;//�﷨���������
	private DefaultTableModel syntaxListTbModel;//
	private String filepath2;		//ѡ���ļ���
	private ArrayList<ActionTable> slrTable;//
	private DefaultTreeModel treeModel;//
	
	// ����� operaters
	// String[] operaters={
	// ">",">=","<","<=","==","!=","|","&","||","&&","!","^","+","-","*","/","%","++","--","+=","-=","*=","%="};
	// ��� divideLine
	// String[] divideLines={
	// ",","=",";","[","]","(",")","{",";","}",".","\"","\'"};
	// �ؼ��� keywords
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
		frame = new JFrame();//�������
		frame.setBounds(100, 100, 1323, 860);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		frame.setLocation((screensize.width - frameSize.width) / 2,
				(screensize.height - frameSize.height) / 2);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();//�ʷ����������1������
		panel.setBackground(new Color(211, 211, 211));
		panel.setBounds(0, 0, 1307, 815);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JPanel panel_1 = new JPanel();//�﷨���������2
		panel_1.setBackground(new Color(102, 153, 204));
		panel_1.setBounds(0, 0, 1308, 819);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		final JPanel panel_2 = new JPanel();//�ʷ������Ҳ����
		panel_2.setBackground(new Color(176, 196, 222));
		panel_2.setBounds(303, 13, 988, 785);
		panel_1.add(panel_2);
		panel_2.setLayout(null);
		
		final JPanel panel_4 = new JPanel();//�﷨�����Ҳ����
		panel_4.setBackground(new Color(176, 196, 222));
		panel_4.setBounds(303, 13, 988, 785);
		panel_1.add(panel_4);
		panel_4.setLayout(null);
		panel_4.setVisible(false);
		

		JPanel panel_3 = new JPanel();//�﷨�����
		panel_3.setBackground(new Color(230, 230, 250));
		panel_3.setBounds(697, 70, 277, 702);
		panel_4.add(panel_3);
		panel_3.setLayout(null);
		//����������
		final JTree tree = new JTree();
		tree.setModel(treeModel);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setRowHeight(20);
		JScrollPane scrollPane = new JScrollPane();//�﷨���ɹ���
		scrollPane.setViewportView(tree);
		panel_3.add(scrollPane);
		scrollPane.setBounds(0, 5, 275,697);
		
/**************��ʼ*************************************************/
	        
/*************����*************************************************/
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
		textArea.setBackground(new Color(220, 220, 220));
		JScrollPane textAreaSP = new JScrollPane();//�ļ���������ɹ���
		textAreaSP.setBounds(14, 87, 275, 309);
		panel_1.add(textAreaSP);
		textAreaSP.setViewportView(textArea);

		//ѡ���ļ���ť
		final JButton btnNewButton_2 = new JButton("ѡ���ļ�");
		btnNewButton_2.setFont(new Font("����", Font.BOLD, 15));
		btnNewButton_2.setBackground(new Color(169, 169, 169));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); // ����ѡ����
				chooser.setMultiSelectionEnabled(true); // ��Ϊ��ѡ
				int returnVal = chooser.showOpenDialog(btnNewButton_2);
				if (returnVal == JFileChooser.APPROVE_OPTION) { // ��������ļ�����
					String filepath = chooser.getSelectedFile()
							.getAbsolutePath(); // ��ȡ����·��
					System.out.println(filepath);
					filepath2 = filepath.replaceAll("\\\\", "/");
					File file = new File(filepath2);
					textArea.setText(txt2String(file));
				}
			}
		});
		btnNewButton_2.setBounds(14, 30, 107, 34);
		panel_1.add(btnNewButton_2);

		JButton btnNewButton_1 = new JButton("����");
		btnNewButton_1.setFont(new Font("����", Font.BOLD, 15));
		btnNewButton_1.setBackground(new Color(169, 169, 169));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = textArea.getText();
				String str2 = str.replaceAll("\r|\n", ""); // ���ｨ���ÿո���棬ȥ�����з��ո�
				try {
					ArrayList<String[]> a=lexicalAnalysis(str2);
			        ArrayList<SLRTree> slrTreeArray= slrTest(a,slrTable);
			        /**************��ʼ������*************************************************/
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
		
		 
		
		// �ֱ�����
		tokenListTbModel = new DefaultTableModel(new Object[][] {},
				new String[] { "�ַ���", "���", "�ֱ���", "value" });
		RowSorter<DefaultTableModel> token_sorter = new TableRowSorter<DefaultTableModel>(tokenListTbModel);
	
		// first�����
		GrammerTable[] grammerTable = new readDFATable().Wenfa();
		// �ʷ�����������
		errorListTbModel = new DefaultTableModel(new Object[][] {},
				new String[] { "�������", "����ط�", "����ԭ��" });
		RowSorter<DefaultTableModel> error_sorter = new TableRowSorter<DefaultTableModel>(errorListTbModel);
		
		// �﷨����������
		grammererrorListTbModel = new DefaultTableModel(new Object[][] {},
				new String[] { "�������", "����ط�", "����ԭ��" });
		RowSorter<DefaultTableModel> SLRerror_sorter = new TableRowSorter<DefaultTableModel>(grammererrorListTbModel);
		
		// �﷨�������
		syntaxListTbModel = new DefaultTableModel(new Object[][] {},
						new String[] { "��Լ���"});

		RowSorter<DefaultTableModel> syntax_sorter = new TableRowSorter<DefaultTableModel>(
						syntaxListTbModel);
		//token��
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
		lblToken.setFont(new Font("����", Font.BOLD, 40));
		
		//error��
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
		lblErrorTable.setFont(new Font("����", Font.BOLD, 40));

		//�﷨��������Լ���
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
		lblAnalysisTable.setFont(new Font("����", Font.BOLD, 40));
		
		//�ķ�error��
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
		lblErrorTable_1.setFont(new Font("����", Font.BOLD, 40));
		lblErrorTable_1.setBounds(219, 395, 254, 40);
		panel_4.add(lblErrorTable_1);

		/**
		 * ����л��ķ�����д���ڲ����
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
		
		JButton button = new JButton("�ʷ�����");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// �л����
				ControlPanel mcp = new ControlPanel();
				mcp.choose(0);
			}
		});
		
		button.setBackground(new Color(169, 169, 169));
		button.setBounds(73, 423, 150, 40);
		panel_1.add(button);

		JButton button_1 = new JButton("�﷨����");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// �л����
				ControlPanel mcp = new ControlPanel();
				mcp.choose(1);
			}
		});
		button_1.setBackground(new Color(169, 169, 169));
		button_1.setBounds(73, 502, 150, 40);
		panel_1.add(button_1);
			
		JButton btnDfa = new JButton("DFAת����");
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
		
		JButton btnSlr = new JButton("SLR������");
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

	// ���ļ�
	public static String txt2String(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// ����һ��BufferedReader������ȡ�ļ�
			String s = null;
			while ((s = br.readLine()) != null) {// ʹ��readLine������һ�ζ�һ��
				result.append(s + System.lineSeparator());
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * �ʷ�������ʼ*************************************************
	 * �ʷ�����ר�÷�����
	 */
	public ArrayList<String[]> lexicalAnalysis(String str) throws Exception {
		while (tokenListTbModel.getRowCount() > 0) {
			tokenListTbModel.removeRow(0);
		}
		while (errorListTbModel.getRowCount() > 0) {
			errorListTbModel.removeRow(0);
		}
		ArrayList<String[]> slrInput2=new ArrayList<String[]>();
		// ���ַ���ת��Ϊ�ַ�����
		char[] strline = str.toCharArray();
		String currentString = "";
		int currentState = 0; // ��ǰ״̬
		int lastState = 0; // ��һ״̬
		DFATable dfa[] = new readDFATable().addDFA();
		DFATableState DFAstate[] = new readDFATable().showDFAState();

		for (int i = 0; i < strline.length; i++) {
			currentString = currentString.concat(String.valueOf(strline[i]));
			lastState = currentState;

			if (currentString == " " || strline[i] == ' ') {
				currentString = currentString.replaceAll(" ", "");
				if (isKeyword(currentString)) {
					tokenListTbModel.addRow(new Object[] { currentString,
							"�ؼ���", tokenID(currentString), "   " });
					slrInput2.add(new String[]{currentString,"�ؼ���"});
					
				} else {
					tokenListTbModel.addRow(new Object[] {currentString,
							findTypeByState(lastState, DFAstate),
							tokenID(currentString),
							findTypeByState(lastState, DFAstate).equals("ע��")
									|| findTypeByState(lastState, DFAstate)
											.equals("�����")
									|| findTypeByState(lastState, DFAstate)
											.equals("���") ? "   "
									: currentString });
					slrInput2.add(new String[]{currentString,findTypeByState(lastState, DFAstate)});
				}
				currentString = "";
				currentState = 0;
				continue;
			}
			currentState = stateChange(strline[i], currentState, dfa);
			if (currentState == -1 || currentState == -2){// ��������ַ�����·�ϻ첻��ȥ��
				currentString = currentString.substring(0,
						currentString.length() - 1);
				// �жϵ�ǰ״̬�Ƿ�Ϊ��ֹ״̬ �粻�� ����
				if (!isFinishByState(lastState, DFAstate)) {
					errorListTbModel.addRow(new Object[] { currentString,
							"��" + i + "�ַ�", "�Ƿ��ַ�" });
					tokenListTbModel.addRow(new Object[] { currentString,
							"�Ƿ��ַ�", "��", "   " });
				} else {
					if (isKeyword(currentString)) {
						tokenListTbModel.addRow(new Object[] { currentString,
								"�ؼ���", tokenID(currentString), "   " });
						slrInput2.add(new String[]{currentString,"�ؼ���"});
					} else {
						tokenListTbModel.addRow(new Object[] {
								currentString,
								findTypeByState(lastState, DFAstate),
								tokenID(currentString),
								findTypeByState(lastState, DFAstate).equals(
										"ע��")
										|| findTypeByState(lastState, DFAstate)
												.equals("�����")
										|| findTypeByState(lastState, DFAstate)
												.equals("���") ? "   "
										: currentString });
				
						slrInput2.add(new String[]{currentString,findTypeByState(lastState, DFAstate)});
					}
				}
				if (currentState == -2) {
					
					tokenListTbModel.addRow(new Object[] { strline[i], "�Ƿ��ַ�",
							"��", "   " });
					errorListTbModel.addRow(new Object[] { strline[i],
							"��" + i + "�ַ�", "�Ƿ��ַ�" });
					i++;
					// errorListTbModel.addRow(new Object[] {"/*", "ע��δ���"});
				}
				currentString = "";
				currentState = 0;
				i--;
			}
			// ���һ������
			if (i == strline.length - 1) {
				if (!isFinishByState(lastState, DFAstate)) {
			
					errorListTbModel.addRow(new Object[] { currentString,
							"��" + i + "�ַ�", "�Ƿ��ַ�" });
					tokenListTbModel.addRow(new Object[] { currentString,
							"�Ƿ��ַ�", "��", "   " });
				} else {
					if (isKeyword(currentString)) {
						tokenListTbModel.addRow(new Object[] { currentString,
								"�ؼ���", tokenID(currentString), "   " });
						slrInput2.add(new String[]{currentString,"�ؼ���"});
					} else {
						tokenListTbModel.addRow(new Object[] {
								currentString,
								findTypeByState(currentState, DFAstate),
								tokenID(currentString),
								findTypeByState(currentState, DFAstate).equals(
										"ע��")
										|| findTypeByState(currentState,
												DFAstate).equals("�����")
										|| findTypeByState(currentState,
												DFAstate).equals("���") ? "   "
										: currentString });
						slrInput2.add(new String[]{currentString,findTypeByState(currentState, DFAstate)});
					}
				}
				if (currentState == -2) {
					tokenListTbModel.addRow(new Object[] { strline[i], "�Ƿ��ַ�",
							"��", "   " });
					errorListTbModel.addRow(new Object[] { strline[i],
							"��" + i + "�ַ�", "�Ƿ��ַ�" });
					i++;
				}
			}

		}
		return slrInput2;
	}
	
	/**
	 * �﷨������ʼ*************************************************
	 */
	public ArrayList<String[]> lexicalAnalysis2(String str) throws Exception {
		ArrayList<String[]> slrInput2=new ArrayList<String[]>();
		char[] strline = str.toCharArray();
		String currentString = "";
		int currentState = 0; // ��ǰ״̬
		int lastState = 0; // ��һ״̬
		DFATable dfa[] = new readDFATable().addDFA();
		DFATableState DFAstate[] = new readDFATable().showDFAState();
		for (int i = 0; i < strline.length; i++) {
			currentString = currentString.concat(String.valueOf(strline[i]));
			lastState = currentState;

			if (currentString == " " || strline[i] == ' ') {
				currentString = currentString.replaceAll(" ", "");
				if (isKeyword(currentString)) {
					slrInput2.add(new String[]{currentString,"�ؼ���"});
					
				} else {
					slrInput2.add(new String[]{currentString,findTypeByState(lastState, DFAstate)});
				}
				currentString = "";
				currentState = 0;
				continue;
			}
			currentState = stateChange(strline[i], currentState, dfa);
			if (currentState == -1 || currentState == -2){// ��������ַ�����·�ϻ첻��ȥ��
				currentString = currentString.substring(0,
						currentString.length() - 1);
				// �жϵ�ǰ״̬�Ƿ�Ϊ��ֹ״̬ �粻�� ����
				if (!isFinishByState(lastState, DFAstate)) {
					//������error����Ӧ���
					//
					//
				} else {
					if (isKeyword(currentString)) {
						slrInput2.add(new String[]{currentString,"�ؼ���"});
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
			// ���һ������
			if (i == strline.length - 1) {
				if (!isFinishByState(lastState, DFAstate)) {
				} else {
					if (isKeyword(currentString)) {
						slrInput2.add(new String[]{currentString,"�ؼ���"});
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
				isInput = true;// ���ڸ�����
				if (dfa[i].getState() == currentState
						&& dfa[i].getNextState() != -1)// ��ǰ״̬һ�� ������ ��һ״̬��Ϊ��
														// ��Ȼ�ڵ�ǰ�Զ�����
				{
					return dfa[i].getNextState();
				}
			}
		}
		if (isInput == false) {
			return -2;
		}
		// ��״̬9 10 ��̫��û�н��� ע��δ��� ����
		return -1;
	}

	/**
	 * �жϵ�ǰ���Ƿ���arr�б���
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

	// ����state ���ظ�״̬������
	public String findTypeByState(int state, DFATableState[] dfaState) {
		for (int i = 0; i < dfaState.length; i++) {
			if (dfaState[i].getState() == state) {
				return dfaState[i].getType();
			}
		}
		return "error";
	}

	// ����state ���ظ�״̬���Ƿ�Ϊ��ֹ״̬
	public boolean isFinishByState(int state, DFATableState[] dfaState) {
		for (int i = 0; i < dfaState.length; i++) {
			if (dfaState[i].getState() == state) {
				return dfaState[i].isFinish();
			}
		}
		return true;
	}

	// �жϵ�ǰ����Ҫ������ַ����Ƿ�Ϊ�ؼ���
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
	 * �ķ�������ʼ*************************************************
	 */

	/**
	 * ���First��������������ķ�����������õ�ÿ�����ս����First��
	 * 
	 * @para grammerTableΪ����ʽ����
	 * @return first��
	 * @throws Exception
	 */
	public String[][] getFirstGroup(GrammerTable[] grammerTable) throws Exception {
		
		String[][] firstGroup = new String[10][2];
		//String��i����0����ʾ���ս����String��i����1����ʾ�Ҳ��first�������ÿո�ָ�
		int x = 0;
		for (int k = 0; k < grammerTable.length; k++) {
			if (!isExitName(firstGroup, grammerTable[k].getName())){// ����Ԫ�ز���first���У�����ӽ�ȥ
				firstGroup[x][0] = grammerTable[k].getName();
				x++;
			}
		}
		//for (int time = 0; time < 3; time++) {//������������Ϊ�����ˣ��д���ȶ
		boolean skip = false;//�Ƿ����
		while(!skip) {
			skip = true;//�Ƿ����
			for (int k = 0; k < grammerTable.length; k++) {
				int flag = 0;
				for (int i = 0; i < firstGroup.length; i++) {
					if (firstGroup[i][0].equals(grammerTable[k].getName())) {
						flag = i;// �ҵ���ǰ�ַ���first������һ��
						break;
					}
				}
				String firstValue = grammerTable[k].getValue()[0];
				if (isTerminator(firstValue)){// ��һ�����ս������ֱ�����
					if (!isExitValue(firstGroup[flag][1], firstValue)) {
						if (firstGroup[flag][1] == null) {
							firstGroup[flag][1] = firstValue;
							skip = false ;//�и��Ĳ�����
						} else{
							String diff = addEx(firstGroup[flag][1],firstValue);
							if(!diff.equals("")) {
								firstGroup[flag][1] += diff;//addEx�ÿո�ָ�
								skip = false ;//�и��Ĳ�����
							}
						}
					}
				} else {//����Ƿ��ս������Ѹ÷��ս����first����ӣ�����Ӧ���ж��Ƿ���null
					for (int i = 0; i < firstGroup.length; i++) {
						if (firstGroup[i][0].equals(firstValue)) {
							if (!isExitValue(firstGroup[flag][1],firstGroup[i][1])) {
								if (firstGroup[flag][1] == null) {
									firstGroup[flag][1] = firstGroup[i][1];
									skip = false ;//�и��Ĳ�����
								}else{
									String diff = addEx(firstGroup[flag][1],firstGroup[i][1]);
									if(!diff.equals("")) {
										firstGroup[flag][1] += diff;//addEx�ÿո�ָ�
										skip = false ;//�и��Ĳ�����
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
	 * ��������ǲ����Ѿ����� ��first�����˵ľͲ����¼�һ����
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
	 * �жϵ�ǰ����Ҫ�����ֵ�Ƿ��Ѵ���
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
	 * ���µ�����ɵ�û�е��ַ�����ӽ��ɵ� �����е�value�� ���µ�����û�е�value�����ȥ
	 * 
	 * @param oldStr
	 * @param newStr
	 * @return a �����ַ�
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
			}//���ؿո���ַ�
		}
		return a;
	}

	/**
	 * �ж��Ƿ�Ϊ�ս��
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
	 * ���follow��
	 * 
	 * @throws Exception
	 */
	public String[][] getFollowGroup() throws Exception {
		GrammerTable[] grammerTable = new readDFATable().Wenfa();
		String[][] followGroup = new String[10][2];
		
		int x = 0;
		for (int k = 0; k < grammerTable.length; k++) {
			if (!isExitName(followGroup, grammerTable[k].getName())){// follow���Ѿ����ڸ�Ԫ�ص�first��
				followGroup[x][0] = grammerTable[k].getName();
				if (followGroup[x][0].equals("P")) {//��ʼ�����Ҳ�����ս��"$"
					followGroup[x][1] = "$";
				}
				x++;
			}
		}
		
		String[][] firstGroup = getFirstGroup(grammerTable);//���first�����ں������follow��
		//for (int time = 0; time < 4; time++){// �ظ����� ��ø���Ϊû�иı�
		boolean skip = false;
		while(!skip) {
			skip=true;
			for (int k = 0; k < grammerTable.length; k++) {
				// ����ÿһ�� ��ÿһ������ �ڶ�����ÿ����һ���ս�� �жϺ����Ƿ�Ϊ�ս�� ������� �����жϺ������ս���Ƿ�Ϊ��
				int y = grammerTable[k].getValue().length;
				for (int i = 0; i < y; i++) {
					if (i == y - 1){// A->aB ���һ�� follow(A)ȫ����follow��B����
						if (!isTerminator(grammerTable[k].getValue()[i])) {
							int flag1 = findFlag(followGroup,grammerTable[k].getValue()[i]);// B��follow�����±�
							int flag2 = findFlag(followGroup,grammerTable[k].getName());// A��follow�����±�
		
							// follow(A)ȫ����follow(B)��
							if (followGroup[flag1][1] == null) {
								followGroup[flag1][1] = followGroup[flag2][1];
								skip = false;
							} else {
								if (followGroup[flag2][1] == null){// AΪ��
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
					//�������һ��
					if (!isTerminator(grammerTable[k].getValue()[i])){// ��ǰ�Ƿ��ս��
						int flag1 = findFlag(followGroup,grammerTable[k].getValue()[i]);// B��follow�����±�
						if (isTerminator(grammerTable[k].getValue()[i + 1])){// �������ս��  ֱ�ӽ��ս�����뵽follow��B������
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
						} else {// �����Ƿ��ս�� ��������ս����first������follow����
							int flag3 = findFlag(firstGroup,grammerTable[k].getValue()[i + 1]);// C��first�����±�
							String[] str = firstGroup[flag3][1].split(" ");//������ս����first��
							boolean isNo = false;//�ж�first���Ƿ���no
							String newFirst="";
							for (int z = 0; z < str.length; z++){
								if (str[z].equals("no")) {
									isNo = true;
								}
								else{
									newFirst+=" "+str[z];
								}
							}
							if (followGroup[flag1][1] == null) {////��c��first�����ռӵ�x��follow����
								followGroup[flag1][1] = newFirst;
								skip = false;
							} else {
								if (newFirst==""){// AΪ��
									continue;
								} else {
									String tmp = addEx(followGroup[flag1][1],newFirst);
									if (!tmp.equals("")) {
										followGroup[flag1][1] += tmp;
										skip = false;
									}
								}
							}
							if (isNo){// ����пյ� ��T->XC,c���пհ�c��follow�ӵ�x��follow���в���c��first�����ռӵ�x��follow����
								//��c��follow�ӵ�x��follow����
								int flag4 = findFlag(followGroup,grammerTable[k].getValue()[i + 1]);
								if (followGroup[flag1][1] == null) {
									followGroup[flag1][1] = followGroup[flag4][1];
									skip = false;
								} else {
									if (followGroup[flag4][1] == null){// AΪnull
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
	 * ��value��Group�����±�
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
	 * �������� ��һ��״̬��������һ����״̬
	 * 
	 * @param currentInput ��ǰ����
	 * @param slrFormulaArray ��ǰ�Ĳ���ʽ����
	 * @return �µĲ���ʽ����
	 */
	public ArrayList<SLRFormula> GOTO(String currentInput,
			ArrayList<SLRFormula> slrFormulaArray) {
		ArrayList<SLRFormula> slrFormulaArray2 = new ArrayList<SLRFormula>();
		//�洢�µĲ���ʽ��
		int count = 0;
		// ��������ʽ���ϵ�ÿһ������ʽ ���������ʽ�ĵ������ǲ�����������ͬ
		for (int k = 0; k < slrFormulaArray.size(); k++) {
			SLRFormula cur = slrFormulaArray.get(k);
			int flag = cur.getFlag();//��ȡ����λ��
			if (flag < cur.getNextString().length){// ���ǹ�Լ״̬
				if (cur.getNextString()[flag].equals(currentInput)){
					// ��ͬ����� flag+1
					SLRFormula slr = new SLRFormula();
					slr.setBeforeString(cur.getBeforeString());
					slr.setFlag(flag + 1);
					slr.setNextString(cur.getNextString());//�µĲ���ʽ
					slrFormulaArray2.add(count, slr);
					count++;
				}
			}
		}
		//��ʱӦ������Ŀ���հ�
		return slrFormulaArray2;
	}

	/**
	 * �������е�״̬������Ŀ�հ�
	 * 
	 * @param initFormulaArray
	 *            �ʼ����Ŀ���հ�
	 * @param nowFormulaArray
	 *            ��ǰ�Ĳ���ʽ����
	 * @return ���ڵļ�����ӵĲ���ʽ���ϣ�����ֵ���Ҳ���ͬ
	 */
	// ��ǰ��һ���Ƿ��ǳ�ʼ״̬��beforeString �������
	public ArrayList<SLRFormula> CLOSURE(ArrayList<SLRFormula> initFormulaArray,
			ArrayList<SLRFormula> nowFormulaArray) {
		do {
			int length = nowFormulaArray.size();
			// ������ǰ����ʽ���ϵ�ÿһ������ʽ�����������ʽ�ĵ�����ǲ��Ƿ��ս�� �ǵĻ���һ��һ����
			for (int k = 0; k < length; k++) {
				int flag = nowFormulaArray.get(k).getFlag();// ��ȡ����λ��
				if (flag < nowFormulaArray.get(k).getNextString().length) {// ���ǹ�Լ״̬
					String next = nowFormulaArray.get(k).getNextString()[flag];// �������ַ�
					if (!isTerminator(next)) {// �����ս��������ҪѰ�ҵȼ���Ŀ�����ÿ�����ĺ��棡������
						for (int i = 0; i < initFormulaArray.size(); i++) {// �ڳ�ʼ״̬��beforeStringһ����
							if (initFormulaArray.get(i).getBeforeString().equals(next)) {// �ҵ���Ӧ�ļ��뵽����ʽ������
								boolean a = isExitFormula(nowFormulaArray, initFormulaArray.get(i));
								if (!a) {// ������ʽ������Ŀ���У������
									nowFormulaArray.add(length, initFormulaArray.get(i));
								}
							}
						}
					}
				}
			}
			if (length == nowFormulaArray.size()) {
				break;//��û���µĲ���ʽ����ʱ����
			}
		} while (true);
		return nowFormulaArray;

	}

	/**
	 * �жϵ�ǰ����ʽ�Ƿ��Ѿ������״̬����
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
	 * �ж��Ƿ����״̬���״̬һģһ�� �ǾͲ����½�һ��״̬��
	 * @param allState   ���е�����״̬
	 * @param nowFormulaArray Ҫ�жϵ�״̬
	 * @return ��ͬ��״̬�ţ�û����ͬ�ķ���-1
	 */
	public int isExitTheSameState(ArrayList<ArrayList<SLRFormula>> allState,
			ArrayList<SLRFormula> nowFormulaArray) {
		// ������״̬���� �ҵ�����ͬ����ʽ���ϵ�״̬ ����״̬���
		int len = nowFormulaArray.size();
		for (int i = 0; i < allState.size(); i++) {// ��i��״̬
			int[] flag = new int[len];
			for (int k = 0; k < len; k++){// ��ǰ����ʽ���ϵĵ�k������ʽ
				for (int j = 0; j < allState.get(i).size(); j++) {
					// ״̬��ĵ�j������ʽ������ҵ�����1
					// ��ʼ��һ��ѭ��
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
								flag[k] = 1;// ��k�����ҵ�
								break;// ������������һ��
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
	 * �ж�����״̬�Ƿ�һģһ�� �ǾͲ����½�һ��״̬��
	 * 
	 * @param allState
	 *            ���е�����״̬
	 * @param nowFormulaArray
	 *            Ҫ�жϵ�״̬
	 * @return ��ͬ��״̬�ţ�û����ͬ�ķ���-1
	 */
	public boolean isTheSameState(ArrayList<SLRFormula> FormulaArray1,
			ArrayList<SLRFormula> FormulaArray2) {
		// ������״̬���� �ҵ�����ͬ����ʽ���ϵ�״̬ ����״̬���
		int len = FormulaArray2.size();
		int[] flag = new int[len];
			for (int k = 0; k < len; k++){// ��ǰ����ʽ���ϵĵ�k������ʽ
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
								flag[k] = 1;// ��k�����ҵ�
								break;// ������������һ��
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
	 * ������Լ״̬ �ҵ��涨�ı��ʽ
	 * 
	 * @param slrFormula
	 *            ��ǰҪ�жϵ�Ϊ��Լ״̬�Ĳ���ʽ
	 * @param grammerTable
	 *            �﷨��
	 * @return ��ԼΪ��һ���﷨����ʽ
	 */
	public int findActionNum(SLRFormula slrFormula, GrammerTable[] grammerTable) {
		for (int i = 0; i < grammerTable.length; i++) {
			if (slrFormula.getBeforeString().equals(grammerTable[i].getName())) {// ��ʼһ��
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
	 * �����ķ��������еȼ���Ŀ���հ�
	 * @param grammerTable
	 * @return
	 * @throws Exception
	 */
	public ArrayList<ActionTable> getSLRTable(GrammerTable[] grammerTable) throws Exception {
		int stateCount = 0;
		// new action��
		ArrayList<ActionTable> actionTable = new ArrayList<ActionTable>();
		ArrayList<ArrayList<SLRFormula>> slrStateArray = new ArrayList<ArrayList<SLRFormula>>();
		// �����洢ȫ���ĵȼ���Ŀ���հ����Լ�״̬���
		ArrayList<SLRFormula> slrFormulArray0 = new ArrayList<SLRFormula>();
		// �洢��ʼ���ķ�
		ArrayList<SLRFormula> slrFormulArray1 = new ArrayList<SLRFormula>();
		// �洢�ȼ۵���Ŀ���հ�
		String[][] followGroup = getFollowGroup();

		// ��ʼ�� ��һ������ʽ����
		for (int i = 0; i < grammerTable.length; i++) {
			SLRFormula slrFormula = new SLRFormula();
			slrFormula.setBeforeString(grammerTable[i].getName());
			slrFormula.setNextString(grammerTable[i].getValue());
			slrFormula.setFlag(0);
			slrFormulArray0.add(i, slrFormula);
		}
		slrStateArray.add(stateCount, slrFormulArray0);// ��һ��״̬
		stateCount++;

		SLRFormula newslrFormula = new SLRFormula();
		newslrFormula.setBeforeString("Z");// ����ʽ Z->P
		newslrFormula.setNextString("P".split(" "));
		newslrFormula.setFlag(0);
		slrFormulArray1.add(0, newslrFormula);// ��������ʽ

		CLOSURE(slrStateArray.get(0), slrFormulArray1);// ������Ŀ���հ�
		slrStateArray.add(stateCount, slrFormulArray1);// ��һ��״̬
		stateCount++;

		String currentInput = null;// ������Ŀ���հ�
		// ��ÿһ��״̬ ������� ����һ��״̬
		// ��ʼ��״̬Ϊ1
		for (int i = 1; i <slrStateArray.size(); i++) {// ����״̬���߱������߲�����״̬
			//ע���˴�ӦΪ��1��ʼ����Ϊ״̬0���������ķ�Z->P
			for (int j = 0; j < slrStateArray.get(i).size(); j++) {
				//�Ահ��Ĳ���ʽ�������ж����ǲ��ǹ�Լ״̬
				SLRFormula cur = slrStateArray.get(i).get(j);
				int flag = cur.getFlag();// ����ʽ�ı�ʶλ
				String cur_str = cur.getBeforeString();//��ǰ����ʽ��������
				int local = findFlag(followGroup, cur_str);//�ҵ�����ʽ�����follow���е����
				if (flag >= cur.getNextString().length) {
					//flag �����һ�������ǹ�Լ״̬
					// ��follow�еĶ���Լ
					if (cur_str.equals("Z")) {
						//��ת��0��״̬
						ActionTable ac = new ActionTable();//�����¶���
						ac.setInput("$");
						ac.setState(i);
						String a = "acc " + 0;
						// ���ݵ�ǰ��ʽ���ҵ���Լʽ�ӵ����
						ac.setAction(a.split(" "));
						actionTable.add(ac);
						continue;
					}
					//�ҵ�
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
						// ���ݵ�ǰ��ʽ���ҵ���Լʽ�ӵ����
						ac.setAction(a.split(" "));
						if (!isExitAction(actionTable, ac)) {
							actionTable.add(ac);
						}
					}
					continue;
				}
				// �ǹ�Լ״̬���򹹽��µ�״̬������Ϊ�����һ���ַ� ����Ӧ���ж��Ƿ�������� ||֮���ж��Ƿ�������һģһ����״̬
				currentInput = cur.getNextString()[flag];// �����һ���ַ�
				// �Ƿ�Ϊ�� �� A->no 0
				if (currentInput.equals("no")) {
					// c->no �ҵ�c��follow�� ��ӹ�Լ
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
						// ���ݵ�ǰ��ʽ���ҵ���Լʽ�ӵ����
						act.setAction(a.split(" "));
						if (!isExitAction(actionTable, act)) {
							actionTable.add(act);
						}
					}
					continue;
				}
				
				// ��ǰ���벻Ϊno����������ʽ����һ�����ϣ�Ȼ����Ӷ���
				ArrayList<SLRFormula> slrFormulaDemo = GOTO(currentInput, slrStateArray.get(i));
				CLOSURE(slrStateArray.get(0), slrFormulaDemo);

				// �������ʽ�����Ƿ��Ѿ�������ͬ�ļ����ˣ� �Ƿ���״̬�� û�з���-1
				int sameState = isExitTheSameState(slrStateArray, slrFormulaDemo);
				if (sameState == -1) {// �ж����״̬����״̬
					slrStateArray.add(stateCount, slrFormulaDemo);// ��һ��״̬
					stateCount++;
				}
				if (isTerminator(currentInput)) {// ���ս���� ����action��
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
				} else {// Ϊ���ս������goto��
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
		//���������
		return actionTable;
	}
	
	
	/**
	 * ���������
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
	 * �ж��Ƿ���ڶ��������е�ǰ״̬����һ���룬��������Ȳŷ���true
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
	 * ���ݵ�ǰ״̬��������slr�������ҵ���һ�������ǹ�Լ�����ƽ�
	 * @param slrTable
	 * @param state
	 * @param input
	 * @return
	 */
	public String[] findNext (ArrayList<ActionTable> slrTable,int state,String input) {
		String[] a=new String[2];
		if(state==77){//77�����������п����ǹ�Լ���п���������
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
			} else if (input.get(i)[1].equals("�ؼ���") || input.get(i)[1].equals("��ʶ��")) {
				inputStr += "id" + " ";
				SLRTree slrTree2 = new SLRTree();
				slrTree2.setName("id");
				String aa = String.valueOf(i);
				String[] bb = aa.split(" ");
				slrTree2.setChildId(bb);
				slrTreeArray.add(slrTree2);
			} else if (input.get(i)[1].indexOf("��") != -1) {
				inputStr += "digit" + " ";
				SLRTree slrTree2 = new SLRTree();
				slrTree2.setName("digit");
				String aa = String.valueOf(i);
				String[] bb = aa.split(" ");
				slrTree2.setChildId(bb);
				slrTreeArray.add(slrTree2);
			}
			/*
			 * else if(input.get(i)[1].equals("�����")) { inputStr +="relop"+" "; }
			 */
			else if (input.get(i)[1].equals("ע��")) {
			} else {
				inputStr += input.get(i)[0] + " ";
			}
		}
		System.out.println("inputStr��" + inputStr);
		GrammerTable[] grammerTable = new readDFATable().Wenfa();
		// ���ڱ������ջ
		Stack<Integer> treeStack = new Stack<Integer>();
		treeStack.push(-1);
		// ״̬ջ
		Stack<Integer> stateStack = new Stack<Integer>();
		stateStack.push(1);
		// ����ջ
		Stack<String> charStack = new Stack<String>();
		charStack.push("$");
		// ����ջ
		Stack<String> inputStack = new Stack<String>();
		inputStack.push("$");

		String[] inputStr2 = inputStr.split(" ");
		for (int i = inputStr2.length - 1; i > 0; i--) {
			inputStack.push(inputStr2[i]);
		}
		System.out.println();
		/******************** ��ʼ����� ***********************/
		System.out.println("���Կ�ʼ======================");
		int sign = 0;

		do {
			String next[] = findNext(slrTable, stateStack.peek(), inputStack.peek());
			if (next[0] == null) {
				break;
			}
			if (next[0].equals("acc")) {
				System.out.println("�ɹ�����������������������������");
				break;
			}
			// s 3�ƽ� ����ջ��һ��������ջ������ �� ״̬ջΪ��һ��״̬
			if (next[0].equals("s")) {
				charStack.push(inputStack.peek());
				inputStack.pop();
				treeStack.push(getFatherId(slrTreeArray, sign));
				sign = sign + 1;
				stateStack.push(Integer.parseInt(next[1]));

			}
			// r 3 ��Լ ����ջ��һ��������ջ������ �� ״̬ջΪ��һ��״̬
			else if (next[0].equals("r")) {// ����Ҫ����

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
				System.out.println("���� " + num);
				if (num == 2) {
					grammererrorListTbModel
							.addRow(new Object[] { "��" + getLine(sign) + "�д���", inputStack.peek(), "��ƥ��������" });
					inputStack.pop();
				} else if (num == 1) {
					grammererrorListTbModel
							.addRow(new Object[] { "��" + getLine(sign) + "�д���", inputStack.peek(), "ȱ���������" });
					inputStack.push("id");
				} else if (num == 3) {
					grammererrorListTbModel
							.addRow(new Object[] { "��" + getLine(sign) + "�д���", inputStack.peek(), "ȱ�������" });
					System.out.println("��" + getLine(sign) + "�д���" + inputStack.peek() + "ȱ�������");
					inputStack.push("+");
				}
			}
		} while (true);
		return slrTreeArray;
	}
	
	//�����ӽڵ�id���ظ��ڵ�id�����޸��ڵ㣬�򷵻��䱾��id
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
	 * �����ģ�ͣ�������
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
	 * ��������
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
	
	//����count���������ڵ�����
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

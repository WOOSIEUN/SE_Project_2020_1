import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class MainFrame extends JFrame implements ActionListener {
	private static final int WIDTH = 900;
	private static final int HEIGHT = 500;
	private int ROW;
	private static final int COLUMN = 7;

	// ���̺�
	DefaultTableModel scheduleTableModel;
	JTable scheduleTable;
	JScrollPane scheduleScrollPane;
	// ������ �˻��� �ʿ��� �ؽ�Ʈ�ʵ�
	JTextField searchField;
	//��ư
	JButton search;
	JButton insert;
	JButton modify;
	JButton delete;
	JButton viewDetails;
	//DB �۾��� �ʿ��� ��ü
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public MainFrame(String ID, String Name, boolean isMaster) {
		// ������ ����
		super("Moyeora Scheduler");
		setSize(WIDTH, HEIGHT);
		setLocation(960, 500);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		//ȯ�� �˾�
		JOptionPane.showMessageDialog(null, Name + " (" + ID + ") �� ȯ���մϴ�!");

		// ���̺��� ���� panel ����
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1, 2));

		// ���̺� ����� - ����� ������ �ҷ��� �迭 �����
		String tableHeader[] = { "ID", "���� �з�", "�ۼ���", "���� �ð�", "���� �ð�", "���� ����", "���� ����" };

		// ���̺� �����
		scheduleTableModel = new DefaultTableModel(null, tableHeader);
		scheduleTable = new JTable(scheduleTableModel);
		scheduleScrollPane = new JScrollPane(scheduleTable);
		tablePanel.add(scheduleScrollPane);
		add(tablePanel, BorderLayout.CENTER);

		//DB���� ������ �о����. ���Ŀ� ������ ������ ���̰� ����
		printTable();

		// JTable���� �÷����� Ŭ��������, ������ �� ����
		scheduleTable.setAutoCreateRowSorter(true);
		TableRowSorter tableSorter = new TableRowSorter(scheduleTableModel);
		scheduleTable.setRowSorter(tableSorter);

		// Table�� �� ũ�� ����
		scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(1);
		scheduleTable.getColumnModel().getColumn(1).setPreferredWidth(1);

		// �˻� �г� ����
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));

		// ������ ���Կ� �ʿ��� Text Field�� �ȳ������� ���� �� ����.
		JLabel searchLabel = new JLabel("   �˻�   ");
		searchPanel.add(searchLabel);		
		searchField = new JTextField("(yyyymmdd) or (yyyymm)");
		searchPanel.add(searchField);
		add(searchPanel, BorderLayout.NORTH);
		search = new JButton("�˻�");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("�˻�")) {
					//�˻���Ʈ �ʿ�. printTable�̿�. ���ڷ� ��¥�� �Ѱ���.
				} else {
					JOptionPane.showMessageDialog(null, "����ġ ���� ���� �߻�. �����ڿ��� �����ϼ���.");
				}
				//db���� table �ٽ� �о����
			}
		});
		searchPanel.add(search);

		// ��ư �߰�
		JPanel buttonPanel = new JPanel();
		insert = new JButton("���� �߰�");
		insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("���� �߰�")) {
					//���� �߰� �Լ� �ʿ�
				} else {
					JOptionPane.showMessageDialog(null, "����ġ ���� ���� �߻�. �����ڿ��� �����ϼ���.");
				}
				//db���� table �ٽ� �о����
			}
		});
		buttonPanel.add(insert);
		modify = new JButton("���� ����");
		modify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("���� ����")) {
					//���� ���� �Լ� �ʿ�
				} else {
					JOptionPane.showMessageDialog(null, "����ġ ���� ���� �߻�. �����ڿ��� �����ϼ���.");
				}
				//db���� table �ٽ� �о����
			}
		});
		buttonPanel.add(modify);
		delete = new JButton("���� ����");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("���� ����")) {
					//���� ���� �Լ� �ʿ�.
				} else {
					JOptionPane.showMessageDialog(null, "����ġ ���� ���� �߻�. �����ڿ��� �����ϼ���.");
				}
				//db���� ������ ���� & table���� ������ �����ؼ� db���� table �ٽ� �о���� �ʵ��� ��.
			}
		});
		buttonPanel.add(delete);
		viewDetails = new JButton("�� ���� ��ȸ");
		viewDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("�� ���� ��ȸ")) {
					int getID = getSelectedID(scheduleTable.getSelectedRow());
					if (getID == -1)
						JOptionPane.showMessageDialog(null, "�� ���⸦ ���ϴ� ���� �������ּ���.");
					else {
						//scheduleDetails viewDetails = new scheduleDetails(getID);
						//������ �ۼ� ���� ���̴� Ŭ������ �ʿ��ؼ� �ش� �κ��� ���� ���� �ۼ��Ͻ� �Ŀ� ����� �߰��ϰڽ��ϴ�.
					}
				} else {
					JOptionPane.showMessageDialog(null, "����ġ ���� ���� �߻�. �����ڿ��� �����ϼ���.");
				}
				//db���� ������ ���� & table���� ������ �����ؼ� db���� table �ٽ� �о���� �ʵ��� ��.
			}
		});
		buttonPanel.add(viewDetails);
		// dataMangementPanel ���������� SOUTH�� �߰�
		add(buttonPanel, BorderLayout.SOUTH);

		// �޴��� �߰�
		JMenu systemMenu = new JMenu("Menu");
		JMenuItem tempMenuItem = new JMenuItem("temp");
		systemMenu.add(tempMenuItem);
		tempMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("temp")) {
					//�޴�
				}
			}
		});			
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(systemMenu);
		setJMenuBar(menuBar);
		setVisible(true);
	}

	//-------------------------Table�� ������ DB���� �о���� �Լ�------------------------------------
	private void printTable() {
		String sort;
		String sql = "SELECT * FROM Schedule";     
		try{
			conn = DB.getMySQLConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			//���� ���̺� �߰�
			while(rs.next()){
				if(rs.getInt("sort") == 0) {
					sort = "����";
				} else {
					sort = "����";            		 
				}            	 
				scheduleTableModel.addRow(new Object[]{rs.getString("idSchedule"), sort, rs.getString("author"), rs.getString("start_time"), rs.getString("end_time"), rs.getString("title"), rs.getString("description")});
			}
			//����ó��
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				//��ü ����
				rs.close(); 
				pstmt.close(); 
				conn.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}		
	}
	

	private int getSelectedID (int row) {
			if (row < 0) return -1;
			else return (int) scheduleTable.getValueAt(row, 0);
	}

	public int stringToInt(String string) {
		return Integer.parseInt(string);
	}

	public String intToString(int integer) {
		return String.valueOf(integer);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
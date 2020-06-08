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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
	// ������ �˻�
	JTextField targetStartField, targetEndField;
	String targetStart, targetEnd;	
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
	
	String ID;

	public MainFrame(String ID, String Name, boolean isMaster) {
		// ������ ����
		super("Moyeora Scheduler");
		setSize(WIDTH, HEIGHT);
		setLocation(960, 500);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.ID = ID;

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
		targetStart = targetToString(null, true, true);
		targetEnd = targetToString(null, true, false);
		printTable(targetStart, targetEnd);

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
		JLabel searchStartLabel = new JLabel("   ���� ��¥   ");
		searchPanel.add(searchStartLabel);
		targetStartField = new JTextField("(yyyy-mm-dd)");
		searchPanel.add(targetStartField);
		JLabel searchEndLabel = new JLabel("   ���� ��¥   ");
		searchPanel.add(searchEndLabel);
		targetEndField = new JTextField("(yyyy-mm-dd)");
		searchPanel.add(targetEndField);		
		add(searchPanel, BorderLayout.NORTH);
		search = new JButton("�˻�");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("�˻�")) {
					targetStart = targetStartField.getText();
					targetEnd = targetEndField.getText();
					//���Ŀ� ���� ������ ��ȯ.
					targetStart = targetToString(targetStart, false, true);
					targetEnd = targetToString(targetEnd, false, false);
					//DB���� �о����
					printTable(targetStart, targetEnd);
				} else {
					JOptionPane.showMessageDialog(null, "����ġ ���� ���� �߻�. �����ڿ��� �����ϼ���.");
				}
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
					ScheduleFrame SF = new ScheduleFrame(ID, isMaster, true, -1, null);
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
					int row = scheduleTable.getSelectedRow();
					int idSchedule = Integer.valueOf((String) scheduleTable.getValueAt(row, 0));
					String[] valueSet = new String[5];
					valueSet[0] = (String)scheduleTable.getValueAt(row,  5);
					valueSet[1] = (String)scheduleTable.getValueAt(row,  3);
					valueSet[2] = (String)scheduleTable.getValueAt(row,  4);
					valueSet[3] = (String)scheduleTable.getValueAt(row,  6);
					valueSet[4] = (String)scheduleTable.getValueAt(row,  1);
					ScheduleFrame SF = new ScheduleFrame(ID, isMaster, false, idSchedule, valueSet);
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
	private void printTable(String start, String end) {
		String sort;
		String sql = "SELECT * FROM Schedule where start_time >= '"+ start +"' AND end_time <= '"+ end +"' AND (sort != 2 OR author = '"+ this.ID +"')";     
		try{
			conn = DB.getMySQLConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			//���� ���̺� �߰�
			scheduleTableModel.setNumRows(0);
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
	
	private String targetToString(String target, boolean isToday, boolean isStart) {
		String targetString = null;
		Date date = new Date();	
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
		
	    //isToday�� true�̸� ������ ������ ����ϱ⸦ ����.
	    //isToday�� false�̸� �˻��� ��¥�� ������ ����ϱ⸦ ����.
		if (!isToday) {
			try {
		        date = dateFormat.parse(target);
		    } catch (ParseException parseE) {
		    	JOptionPane.showMessageDialog(null, "�ùٸ� ��¥ ������ �Է����ּ���.\nYYYY-MM-DD");
		    	parseE.printStackTrace();
		    	return null;
		    }
		}
		//date�� String �������� ��ȯ
		targetString = dateFormat.format(date);
		
		//isStart�̸� ������ return�ؾ���.
		//!isStart�̸� ���� return�ؾ���.
		if(isStart) {
			targetString = targetString + " 00:00:00";
		} else {
			targetString = targetString + " 23:59:59";
		}		
		return targetString;
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
public class MainFrame {

}

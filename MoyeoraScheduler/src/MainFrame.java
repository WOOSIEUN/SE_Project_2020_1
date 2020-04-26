import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

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
	private static final int COLUMN = 6;

		// ���̺�
	DefaultTableModel scheduleTableModel;
	JTable scheduleTable;
	JScrollPane scheduleScrollPane;
	// ������ �˻��� �ʿ��� �ؽ�Ʈ�ʵ�
	JTextField searchField;

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
		String tableHeader[] = { "ID", "���� �з�", "���� �ð�", "���� �ð�", "���� ����", "���� ����" };
		String scheduleContents[][] = null;
		ROW = 1; //***���Ŀ� ���� ����***
					//DB�� ���� �׽�Ʈ �Ŀ� ����.
		scheduleContents = new String[ROW][COLUMN];

		// ���̺� �����
		scheduleTableModel = new DefaultTableModel(scheduleContents, tableHeader);
		scheduleTable = new JTable(scheduleTableModel);
		scheduleScrollPane = new JScrollPane(scheduleTable);
		tablePanel.add(scheduleScrollPane);
		add(tablePanel, BorderLayout.CENTER);

		// JTable���� �÷����� Ŭ��������, ������ �� ����
		scheduleTable.setAutoCreateRowSorter(true);
		TableRowSorter personalAccountTableSorter = new TableRowSorter(scheduleTableModel);
		scheduleTable.setRowSorter(personalAccountTableSorter);

		// Table�� �� ũ�� ����
		scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(1);

		// �˻� �г� ����
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));

		// ������ ���Կ� �ʿ��� Text Field�� �ȳ������� ���� �� ����.
		JLabel searchLabel = new JLabel("   �˻�   ");
		searchPanel.add(searchLabel);		
		searchField = new JTextField("(yyyymmdd) or (yyyymm)");
		searchPanel.add(searchField);
		add(searchPanel, BorderLayout.NORTH);
		JButton search = new JButton("�˻�");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("�˻�")) {
					//���� �߰� �Լ� �ʿ�
				} else {
					JOptionPane.showMessageDialog(null, "����ġ ���� ���� �߻�. �����ڿ��� �����ϼ���.");
				}
				//db���� table �ٽ� �о����
			}
		});
		searchPanel.add(search);
	
		// ��ư �߰�
		JPanel buttonPanel = new JPanel();
		JButton insert = new JButton("���� �߰�");
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
		JButton modify = new JButton("���� ����");
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
		JButton delete = new JButton("���� ����");
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
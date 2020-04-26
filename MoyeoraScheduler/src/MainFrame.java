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

		// 테이블
	DefaultTableModel scheduleTableModel;
	JTable scheduleTable;
	JScrollPane scheduleScrollPane;
	// 데이터 검색에 필요한 텍스트필드
	JTextField searchField;

	public MainFrame(String ID, String Name, boolean isMaster) {
		// 프레임 설정
		super("Moyeora Scheduler");
		setSize(WIDTH, HEIGHT);
		setLocation(960, 500);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//환영 팝업
		JOptionPane.showMessageDialog(null, Name + " (" + ID + ") 님 환영합니다!");

		// 테이블을 붙일 panel 생성
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1, 2));

		// 테이블 만들기 - 헤더와 데이터 불러올 배열 만들기
		String tableHeader[] = { "ID", "일정 분류", "시작 시간", "종료 시간", "일정 제목", "일정 내용" };
		String scheduleContents[][] = null;
		ROW = 1; //***이후에 수정 예정***
					//DB와 연동 테스트 후에 수정.
		scheduleContents = new String[ROW][COLUMN];

		// 테이블 만들기
		scheduleTableModel = new DefaultTableModel(scheduleContents, tableHeader);
		scheduleTable = new JTable(scheduleTableModel);
		scheduleScrollPane = new JScrollPane(scheduleTable);
		tablePanel.add(scheduleScrollPane);
		add(tablePanel, BorderLayout.CENTER);

		// JTable에서 컬럼명을 클릭했을때, 데이터 값 정렬
		scheduleTable.setAutoCreateRowSorter(true);
		TableRowSorter personalAccountTableSorter = new TableRowSorter(scheduleTableModel);
		scheduleTable.setRowSorter(personalAccountTableSorter);

		// Table의 셀 크기 조절
		scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(1);

		// 검색 패널 생성
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));

		// 데이터 삽입에 필요한 Text Field와 안내문구를 넣을 라벨 생성.
		JLabel searchLabel = new JLabel("   검색   ");
		searchPanel.add(searchLabel);		
		searchField = new JTextField("(yyyymmdd) or (yyyymm)");
		searchPanel.add(searchField);
		add(searchPanel, BorderLayout.NORTH);
		JButton search = new JButton("검색");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("검색")) {
					//일정 추가 함수 필요
				} else {
					JOptionPane.showMessageDialog(null, "예상치 못한 에러 발생. 관리자에게 문의하세요.");
				}
				//db에서 table 다시 읽어오기
			}
		});
		searchPanel.add(search);
	
		// 버튼 추가
		JPanel buttonPanel = new JPanel();
		JButton insert = new JButton("일정 추가");
		insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("일정 추가")) {
					//일정 추가 함수 필요
				} else {
					JOptionPane.showMessageDialog(null, "예상치 못한 에러 발생. 관리자에게 문의하세요.");
				}
				//db에서 table 다시 읽어오기
			}
		});
		buttonPanel.add(insert);
		JButton modify = new JButton("일정 수정");
		modify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("일정 수정")) {
					//일정 수정 함수 필요
				} else {
					JOptionPane.showMessageDialog(null, "예상치 못한 에러 발생. 관리자에게 문의하세요.");
				}
				//db에서 table 다시 읽어오기
			}
		});
		buttonPanel.add(modify);
		JButton delete = new JButton("일정 삭제");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("일정 삭제")) {
					//일정 삭제 함수 필요.
				} else {
					JOptionPane.showMessageDialog(null, "예상치 못한 에러 발생. 관리자에게 문의하세요.");
				}
				//db에서 데이터 삭제 & table에서 데이터 삭제해서 db에서 table 다시 읽어오지 않도록 함.
			}
		});
		buttonPanel.add(delete);		
		// dataMangementPanel 메인프레임 SOUTH에 추가
		add(buttonPanel, BorderLayout.SOUTH);
		
		// 메뉴바 추가
		JMenu systemMenu = new JMenu("Menu");
		JMenuItem tempMenuItem = new JMenuItem("temp");
		systemMenu.add(tempMenuItem);
		tempMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("temp")) {
					//메뉴
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
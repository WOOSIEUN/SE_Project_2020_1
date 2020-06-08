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

	// 테이블
	DefaultTableModel scheduleTableModel;
	JTable scheduleTable;
	JScrollPane scheduleScrollPane;
	// 데이터 검색
	JTextField targetStartField, targetEndField;
	String targetStart, targetEnd;	
	//버튼
	JButton search;
	JButton insert;
	JButton modify;
	JButton delete;
	JButton viewDetails;
	//DB 작업에 필요한 객체
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	String ID;

	public MainFrame(String ID, String Name, boolean isMaster) {
		// 프레임 설정
		super("Moyeora Scheduler");
		setSize(WIDTH, HEIGHT);
		setLocation(960, 500);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.ID = ID;

		//환영 팝업
		JOptionPane.showMessageDialog(null, Name + " (" + ID + ") 님 환영합니다!");

		// 테이블을 붙일 panel 생성
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1, 2));

		// 테이블 만들기 - 헤더와 데이터 불러올 배열 만들기
		String tableHeader[] = { "ID", "일정 분류", "작성자", "시작 시간", "종료 시간", "일정 제목", "일정 내용" };

		// 테이블 만들기
		scheduleTableModel = new DefaultTableModel(null, tableHeader);
		scheduleTable = new JTable(scheduleTableModel);
		scheduleScrollPane = new JScrollPane(scheduleTable);
		tablePanel.add(scheduleScrollPane);
		add(tablePanel, BorderLayout.CENTER);

		//DB에서 데이터 읽어오기. 이후에 오늘의 일정만 보이게 수정
		targetStart = targetToString(null, true, true);
		targetEnd = targetToString(null, true, false);
		printTable(targetStart, targetEnd);

		// JTable에서 컬럼명을 클릭했을때, 데이터 값 정렬
		scheduleTable.setAutoCreateRowSorter(true);
		TableRowSorter tableSorter = new TableRowSorter(scheduleTableModel);
		scheduleTable.setRowSorter(tableSorter);

		// Table의 셀 크기 조절
		scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(1);
		scheduleTable.getColumnModel().getColumn(1).setPreferredWidth(1);

		// 검색 패널 생성
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));

		// 데이터 삽입에 필요한 Text Field와 안내문구를 넣을 라벨 생성.
		JLabel searchLabel = new JLabel("   검색   ");
		searchPanel.add(searchLabel);		
		JLabel searchStartLabel = new JLabel("   시작 날짜   ");
		searchPanel.add(searchStartLabel);
		targetStartField = new JTextField("(yyyy-mm-dd)");
		searchPanel.add(targetStartField);
		JLabel searchEndLabel = new JLabel("   종료 날짜   ");
		searchPanel.add(searchEndLabel);
		targetEndField = new JTextField("(yyyy-mm-dd)");
		searchPanel.add(targetEndField);		
		add(searchPanel, BorderLayout.NORTH);
		search = new JButton("검색");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("검색")) {
					targetStart = targetStartField.getText();
					targetEnd = targetEndField.getText();
					//형식에 맞춰 데이터 변환.
					targetStart = targetToString(targetStart, false, true);
					targetEnd = targetToString(targetEnd, false, false);
					//DB에서 읽어오기
					printTable(targetStart, targetEnd);
				} else {
					JOptionPane.showMessageDialog(null, "예상치 못한 에러 발생. 관리자에게 문의하세요.");
				}
			}
		});
		searchPanel.add(search);

		// 버튼 추가
		JPanel buttonPanel = new JPanel();
		insert = new JButton("일정 추가");
		insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("일정 추가")) {
					ScheduleFrame SF = new ScheduleFrame(ID, isMaster, true, -1, null);
				} else {
					JOptionPane.showMessageDialog(null, "예상치 못한 에러 발생. 관리자에게 문의하세요.");
				}
				//db에서 table 다시 읽어오기
			}
		});
		buttonPanel.add(insert);
		modify = new JButton("일정 수정");
		modify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("일정 수정")) {
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
					JOptionPane.showMessageDialog(null, "예상치 못한 에러 발생. 관리자에게 문의하세요.");
				}
				//db에서 table 다시 읽어오기
			}
		});
		buttonPanel.add(modify);
		delete = new JButton("일정 삭제");
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
		viewDetails = new JButton("상세 일정 조회");
		viewDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("상세 일정 조회")) {
					int getID = getSelectedID(scheduleTable.getSelectedRow());
					if (getID == -1)
						JOptionPane.showMessageDialog(null, "상세 보기를 원하는 열을 선택해주세요.");
					else {
						//scheduleDetails viewDetails = new scheduleDetails(getID);
						//스케쥴 작성 수정 쓰이는 클래스가 필요해서 해당 부분을 맡은 분이 작성하신 후에 기능을 추가하겠습니다.
					}
				} else {
					JOptionPane.showMessageDialog(null, "예상치 못한 에러 발생. 관리자에게 문의하세요.");
				}
			}
		});
		buttonPanel.add(viewDetails);
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

	//-------------------------Table의 내용을 DB에서 읽어오는 함수------------------------------------
	private void printTable(String start, String end) {
		String sort;
		String sql = "SELECT * FROM Schedule where start_time >= '"+ start +"' AND end_time <= '"+ end +"' AND (sort != 2 OR author = '"+ this.ID +"')";     
		try{
			conn = DB.getMySQLConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			//값을 테이블에 추가
			scheduleTableModel.setNumRows(0);
			while(rs.next()){
				if(rs.getInt("sort") == 0) {
					sort = "공동";
				} else {
					sort = "개인";            		 
				}            	 
				scheduleTableModel.addRow(new Object[]{rs.getString("idSchedule"), sort, rs.getString("author"), rs.getString("start_time"), rs.getString("end_time"), rs.getString("title"), rs.getString("description")});
			}
			//예외처리
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				//객체 해제
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
		
	    //isToday가 true이면 오늘의 일정를 출력하기를 원함.
	    //isToday가 false이면 검색한 날짜의 일정을 출력하기를 원함.
		if (!isToday) {
			try {
		        date = dateFormat.parse(target);
		    } catch (ParseException parseE) {
		    	JOptionPane.showMessageDialog(null, "올바른 날짜 형식을 입력해주세요.\nYYYY-MM-DD");
		    	parseE.printStackTrace();
		    	return null;
		    }
		}
		//date를 String 형식으로 변환
		targetString = dateFormat.format(date);
		
		//isStart이면 시작을 return해야함.
		//!isStart이면 끝을 return해야함.
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

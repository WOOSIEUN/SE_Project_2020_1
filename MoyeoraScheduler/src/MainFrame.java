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
		String tableHeader[] = { "ID", "일정 분류", "작성자", "시작 시간", "종료 시간", "일정 제목"};

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

		//-----------------------버튼 생성------------------------------
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
					ScheduleFrame SF = new ScheduleFrame(ID, isMaster, true, false, -1);
					printTable(targetStart, targetEnd);
				} else {
					JOptionPane.showMessageDialog(null, "예상치 못한 에러 발생. 관리자에게 문의하세요.");
				}				
			}
		});
		buttonPanel.add(insert);

		modify = new JButton("일정 수정");
		modify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("일정 수정")) {
					int getID = getSelectedID(scheduleTable.getSelectedRow());
					if (getID == -1)
						JOptionPane.showMessageDialog(null, "수정하기를 원하는 열을 선택해주세요.");
					else {
						//조건에 따라 수정이 불가능 한 경우.
						if(!ID.equals((String) scheduleTable.getValueAt(scheduleTable.getSelectedRow(), 2)) && !isMaster)
							JOptionPane.showMessageDialog(null, "수정 권한이 없습니다.");
						else {
							ScheduleFrame SF = new ScheduleFrame(ID, isMaster, false, false, getID);
						}			
					}					
				} else {
					JOptionPane.showMessageDialog(null, "예상치 못한 에러 발생. 관리자에게 문의하세요.");
				}
				printTable(targetStart, targetEnd);
			}
		});
		buttonPanel.add(modify);

		delete = new JButton("일정 삭제");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String event = e.getActionCommand();
				if (event.equals("일정 삭제")) {
					int getID = getSelectedID(scheduleTable.getSelectedRow());
					if (getID == -1)
						JOptionPane.showMessageDialog(null, "삭제 하기를 원하는 열을 선택해주세요.");
					else {
						//조건에 따라 삭제가 불가능 한 경우.
						if(!ID.equals((String) scheduleTable.getValueAt(scheduleTable.getSelectedRow(), 2)) && !isMaster)
							JOptionPane.showMessageDialog(null, "삭제 권한이 없습니다.");
						else {
							//테이블에서 데이터 삭제
							scheduleTableModel.removeRow(scheduleTable.getSelectedRow());
							//DB에서 데이터 삭제
							deleteSchedule(getID);
						}				
					}
				} else {
					JOptionPane.showMessageDialog(null, "예상치 못한 에러 발생. 관리자에게 문의하세요.");
				}
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
						ScheduleFrame SF = new ScheduleFrame(ID, isMaster, false, true, getID);
					}
				} else {
					JOptionPane.showMessageDialog(null, "예상치 못한 에러 발생. 관리자에게 문의하세요.");
				}
			}
		});
		buttonPanel.add(viewDetails);

		// dataMangementPanel 메인프레임 SOUTH에 추가
		add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	//-------------------------Table의 내용을 DB에서 읽어오는 함수------------------------------------
	private void printTable(String start, String end) {
		String sort;
		String sql = "SELECT * FROM Schedule where (start_time <= '"+ end +"' AND end_time >= '"+ start +"') AND (sort != 2 OR author = '"+ this.ID +"')";

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
				scheduleTableModel.addRow(new Object[]{rs.getString("idSchedule"), sort, rs.getString("author"), rs.getString("start_time"), rs.getString("end_time"), rs.getString("title")});
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

	//-------------------------DB에서 특정 ID를 가진 값을 삭제하는 함수------------------------------------
	private void deleteSchedule(int ID) {
		String sql = "DELETE FROM Schedule WHERE idSchedule = '" + ID + "'";     
		try{
			conn = DB.getMySQLConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate(sql);
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
		else return Integer.valueOf((String) scheduleTable.getValueAt(row, 0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ScheduleFrame extends JFrame implements ActionListener {
	private static final int WIDTH = 350;
	private static final int HEIGHT = 450;

	String ID;
	int idSchedule;

	JPanel panel = new JPanel();
	JPanel descPanel = new JPanel();

	JLabel space = new JLabel();
	JLabel titleLabel = new JLabel("일정 제목");
	JLabel descriptionLabel = new JLabel("일정 내용");
	JLabel startLabel = new JLabel("시작 시간");
	JLabel endLabel = new JLabel("종료 시간");
	//일정 상세보기에서 일정 종류 확인 및 작성자 확인 라벨
	JLabel authorLabel;
	JLabel sortLabel;

	JTextField title;
	JTextField start;
	JTextField end;
	JTextField description;

	JButton apply = new JButton("확인");

	String[] combo = {"공동", "개인 공개", "개인 비공개"};
	JComboBox<String> pub = new JComboBox<String> (combo);

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	int error;

	public ScheduleFrame(String ID, boolean isMaster, boolean isNew, boolean viewDetails, int idSchedule) {
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(new BorderLayout());
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);

		this.ID = ID;
		this.idSchedule = idSchedule;

		panel.setLayout(new GridLayout(4, 2, 5, 10));
		panel.setSize(350, 30);

		apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				error = ErrorDetect(isMaster);

				if(error == 0) {
					if(isNew) addSchedule();
					else if (!viewDetails) modifySchedule();
					//일정 상세보기가 아닐 경우 그냥 dispose함.
					dispose();
				}

				return;
			}
		});

		window(isMaster, isNew, viewDetails);
		if(isNew)
			this.setTitle("New");
		else {
			if(viewDetails)	this.setTitle("View Details");
			else this.setTitle("Modify");
		}
	}

	public String[] getInfo(int ID, boolean viewDetails) {
		String[] valueSet = new String[5];
		String sql = "SELECT * from Schedule WHERE idSchedule = '" + ID + "'";

		try {
			conn = DB.getMySQLConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);
			rs.next();
			valueSet[0] = rs.getString("start_time");
			valueSet[1] = rs.getString("end_time");
			valueSet[2] = rs.getString("title");
			valueSet[3] = rs.getString("description");
			valueSet[4] = Integer.toString(rs.getInt("sort"));
			if (viewDetails) { //일정 상세 보기 라벨 설정
				//작성자 라벨
				authorLabel = new JLabel("작성자 : " + rs.getString("author"));
				if(rs.getInt("sort") == 0) {
					sortLabel = new JLabel("일정 종류 : 공동 일정");
				} else if (rs.getInt("sort") == 1) {
					sortLabel = new JLabel("일정 종류 : 개인 공개 일정");        		 
				} else {
					sortLabel = new JLabel("일정 종류 : 개인 비공개 일정");    
				}				
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
		return valueSet;
	}

	public void window(boolean isMaster, boolean isNew, boolean viewDetails) {
		if(isNew) {
			title = new JTextField();
			start = new JTextField("YYYY-MM-DD hh:mm");
			end = new JTextField("YYYY-MM-DD hh:mm");
			description = new JTextField();
		}else {
			String valueSet[] = new String[5];
			valueSet = getInfo(idSchedule, viewDetails);
			title = new JTextField(valueSet[2]);
			start = new JTextField(valueSet[0]);
			end = new JTextField(valueSet[1]);
			description = new JTextField(valueSet[3]);
		}
		if(viewDetails) {
			//일정 상세보기일 경우 유저가 텍스트 필드를 수정하지 못하도록 함.
			title.setEditable(false);
			start.setEditable(false);
			end.setEditable(false);
			description.setEditable(false);
		}

		panel.add(titleLabel);
		panel.add(title);
		panel.add(startLabel);
		panel.add(endLabel);
		panel.add(start);
		panel.add(end);
		//일정 상세보기일 경우 작성자와 일정종류를, 수정이나 추가일 경우 스페이스와 콤보박스를 추가함.
		if(viewDetails) {
			panel.add(authorLabel);
			panel.add(sortLabel);
		}
		else {
			panel.add(space);
			panel.add(pub);
		}

		this.add(panel, BorderLayout.NORTH);

		descPanel.setLayout(new BorderLayout());
		descPanel.add(descriptionLabel, BorderLayout.NORTH);
		descPanel.add(description, BorderLayout.CENTER);

		this.add(descPanel, BorderLayout.CENTER);

		this.add(apply, BorderLayout.SOUTH);
	}

	public void addSchedule() {
		String scheTitle = title.getText();
		String scheStart = start.getText();
		String scheEnd = end.getText();
		String scheDescription = description.getText();
		String scheAuthor;
		int scheSort, r;

		if("공동".equals(pub.getSelectedItem().toString())) {
			scheSort = 0;
		}else if("개인 공개".contentEquals(pub.getSelectedItem().toString())) {
			scheSort = 1;
		}else scheSort = 2;

		scheAuthor = ID;

		String sql = "insert into Schedule (start_time, end_time, sort, title, description, author) values ('" + scheStart + "', '" + scheEnd + "', '" + scheSort + "', '" + scheTitle + "', '" + scheDescription + "', '" + scheAuthor + "')";

		try {
			conn = DB.getMySQLConnection();
			pstmt = conn.prepareStatement(sql);
			r = pstmt.executeUpdate();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				pstmt.close();
				conn.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public void modifySchedule() {
		String scheTitle = title.getText();
		String scheStart = start.getText();
		String scheEnd = end.getText();
		String scheDescription = description.getText();
		String scheAuthor;
		int scheSort, r;

		String[] valueSet = new String[5];
		valueSet = getInfo(idSchedule);

		if("공동".equals(pub.getSelectedItem().toString())) {
			scheSort = 0;
		}else if("개인 공개".equals(pub.getSelectedItem().toString())){
			scheSort = 1;
		}else scheSort = 2;

		scheAuthor = ID;

		String sql = "update Schedule set start_time = '" + scheStart + "', end_time = '" + scheEnd + "', title = '" + scheTitle + "', sort = '" + scheSort + "', description = '" + scheDescription + "' where idSchedule = '" + idSchedule + "'";

		try {
			conn = DB.getMySQLConnection();
			pstmt = conn.prepareStatement(sql);
			r= pstmt.executeUpdate();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				pstmt.close();
				conn.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public int ErrorDetect(boolean isMaster) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Date temp = new Date();

		try {
			temp = format.parse(start.getText());
			temp = format.parse(end.getText());
		}catch(ParseException e) {
			JOptionPane.showMessageDialog(null, "날짜 형식이 맞지 않습니다.");
		}finally {
			if(title.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "제목을 입력하세요.");
				return 1;
			}else if(description.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "설명을 입력하세요.");
				return 1;
			}
		}
		return 0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
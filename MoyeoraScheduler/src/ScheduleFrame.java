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
	
	JTextField title;
	JTextField start;
	JTextField end;
	JTextField description;
	
	JButton apply = new JButton("등록");
	
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
					else modifySchedule();
					dispose();
				}

				return;
			}
		});
		
		if(isNew) {
			window(isMaster, true);
			this.setTitle("New");
		}else {
			window(isMaster, false);
			this.setTitle("Modify");
		}
	}
	
	public String[] modifyInfo(int idSchedule) {
		String[] valueSet = new String[5];
		String sql = "select start_time, end_time, sort, title, description frome Schedule where author = " + ID;
		
		try {
			conn = DB.getMySQLConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);
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
		
		try {
			valueSet[0] = rs.getString("start_time");
			valueSet[1] = rs.getString("end_time");
			valueSet[2] = rs.getString("title");
			valueSet[3] = rs.getString("description");
			valueSet[4] = Integer.toString(rs.getInt("sort"));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return valueSet;
	}
	
	public void window(boolean isMaster, boolean isNew) {
		if(isNew) {
			title = new JTextField();
			start = new JTextField("YYYY-MM-DD hh:mm");
			end = new JTextField("YYYY-MM-DD hh:mm");
			description = new JTextField();
		}else {
			String valueSet[] = new String[5];
			valueSet = modifyInfo(idSchedule);
			title = new JTextField(valueSet[2]);
			start = new JTextField(valueSet[0]);
			end = new JTextField(valueSet[1]);
			description = new JTextField(valueSet[3]);
		}
		
		panel.add(titleLabel);
		panel.add(title);
		panel.add(startLabel);
		panel.add(endLabel);
		panel.add(start);
		panel.add(end);
		panel.add(space);
		panel.add(pub);
		
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
		valueSet = modifyInfo(idSchedule);
		
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
		
			if(isMaster == false && "공동".equals(pub.getSelectedItem().toString())) {
				JOptionPane.showMessageDialog(null, "공동 일정은 마스터 유저만 등록 가능합니다.");
				return 1;
			}
		}
		
		return 0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
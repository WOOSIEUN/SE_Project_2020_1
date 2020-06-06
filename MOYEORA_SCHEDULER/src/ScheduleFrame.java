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
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ScheduleFrame extends JFrame implements ActionListener {
	private static final int WIDTH = 350;
	private static final int HEIGHT = 450;

	String ID;
	String[] valueSet;
	
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

	JCheckBox pub = new JCheckBox("일정 공개");
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	int r;
	
	public ScheduleFrame(String ID, boolean isMaster, boolean isNew, int idSchedule, String[] valueSet) {
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(new BorderLayout());
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		
		this.ID = ID;
		this.valueSet = valueSet;
		
		panel.setLayout(new GridLayout(4, 2, 5, 10));
		panel.setSize(350, 30);
		
		apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ErrorDetect();
				if(isNew) addSchedule(isMaster);
				else modifySchedule(isMaster, idSchedule);
				
				dispose();
				
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
	
	public void window(boolean isMaster, boolean isNew) {
		if(isNew) {
			title = new JTextField();
			start = new JTextField("YYYY-MM-DD hh:mm");
			end = new JTextField("YYYY-MM-DD hh:mm");
			description = new JTextField();
		}else {
			title = new JTextField(valueSet[0]);
			start = new JTextField(valueSet[1]);
			end = new JTextField(valueSet[2]);
			description = new JTextField(valueSet[3]);
		}
		if(isMaster) {
			title = new JTextField(valueSet[0]);
			start = new JTextField(valueSet[1]);
			end = new JTextField(valueSet[2]);
			description = new JTextField(valueSet[3]);

			panel.add(titleLabel);
			panel.add(title);
			panel.add(startLabel);
			panel.add(endLabel);
			panel.add(start);
			panel.add(end);
			panel.add(space);
			panel.add(space);
			this.add(panel, BorderLayout.NORTH);
			
			descPanel.setLayout(new BorderLayout());
			descPanel.add(descriptionLabel, BorderLayout.NORTH);
			descPanel.add(description, BorderLayout.CENTER);
			
			this.add(descPanel, BorderLayout.CENTER);
			
			this.add(apply, BorderLayout.SOUTH);
		}else {
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
	}

	public void addSchedule(boolean isMaster) {
		String scheTitle = title.getText();
		String scheStart = start.getText();
		String scheEnd = end.getText();
		String scheDescription = description.getText();
		String scheAuthor;
		int scheSort;
		
		if(isMaster) scheSort = 0;
		else {
			if(pub.isSelected()) scheSort = 1;
			else scheSort = 2;
		}
		
		scheAuthor = ID;
		
		String sql = "INSERT INTO Schedule (start_time, end_time, sort, title, description, author) Values ('" + scheStart + "','" + scheEnd + "'," + scheSort + ",'" + scheTitle + "','" + scheDescription + "','" + scheAuthor + "')";
		
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
	
	public void modifySchedule(boolean isMaster, int scheID) {
		String scheTitle = title.getText();
		String scheStart = start.getText();
		String scheEnd = end.getText();
		String scheDescription = description.getText();
		String scheAuthor;
		int scheSort;
		
		if(isMaster) scheSort = 0;
		else {
			if(pub.isSelected()) scheSort = 1;
			else scheSort = 2;
		}
		
		scheAuthor = ID;
		
		String sql = "UPDATE Schedule SET start_time = '" + scheStart + "', end_time = '" + scheEnd + "', title = '" + scheTitle + "', sort = '" + scheSort + "', description = '" + scheDescription + "' WHERE idSchedule = '" + scheID +"';";
		
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
	
	public void ErrorDetect() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Date temp = new Date();
		try{
			temp = format.parse(start.getText());
			temp = format.parse(end.getText());
		}catch(ParseException e) {
			JOptionPane.showMessageDialog(null, "날짜 형식이 맞지 않습니다.");
		}finally {
		if(title.getText().equals("")) JOptionPane.showMessageDialog(null, "제목을 입력하세요.");
		else if(description.getText().equals("")) JOptionPane.showMessageDialog(null, "설명을 입력하세요.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
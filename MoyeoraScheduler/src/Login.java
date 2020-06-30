import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class Login extends JFrame{

	private MoyeoraScheduler Moyeora;
	//DB 작업에 필요한 객체
	Connection conn = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	public String IDarr[] = new String[20], PasswordArr[] = new String[20], NameArr[] = new String[20];
	public boolean AdminArr[] = new boolean[20];
	public String Id;
	public String Name;
	boolean ism;
	public Login(){
		JPanel p = new JPanel();
		JButton j1 = new JButton("Register");
		JButton j2 = new JButton("Login");
		JButton j3 = new JButton("Admin");
		JTextField t1 = new JTextField();
		JPasswordField t2 = new JPasswordField();
		JTextField t3 = new JTextField();
		JLabel l0 = new JLabel("Welcome To Moyeora Scheduler");
		JLabel l1 = new JLabel("ID");
		JLabel l2 = new JLabel("Password");
		JLabel l3 = new JLabel("Name");
		l0.setBounds(100,50,100,100);//제목
		j1.setBounds(360,250,90,20); //회원가입
		j2.setBounds(360,225,90,20); //로그인
		j3.setBounds(360,275,90,20); //관리자 전용 로그인
		t1.setBounds(350,150,100,15); //아이디칸
		t2.setBounds(350,175,100,15); //비밀번호칸
		t3.setBounds(350,200,100,15); //이름칸
		l1.setBounds(280,150,75,15); //아이디
		l2.setBounds(280,175,75,15); //비밀번호
		l3.setBounds(280,200,75,15); //이름
		add(j1);
		add(j2);
		add(j3);
		add(t1);
		add(t2);
		add(t3);
		add(l1);
		add(l2);
		add(l3);
		setSize(750,500);
		setLayout(null);
		setVisible(true);

		j1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Moyeora.showRegisterFrame();// 회원가입창 띄우기
			}
		});;

		j2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e2) {
				String s1, s3;
				String pw = "";
				char[] s2 = new char[20];
				s1=t1.getText();
				s2=t2.getPassword();
				s3=t3.getText();

				for(char cha : s2) {
					pw += Character.toString(cha);
				}

				if(find(s1,pw,s3)) // DB에 담긴 ID와 Password와 내가 입력한 ID와 Password, 이름이 일치하는지 확인해주는 메소드
				{
					if(ad_find(s1))
						JOptionPane.showMessageDialog(null, "Can't login with this Id!");
					else
					{
						Id = s1;
						Name = s3;
						ism = false;
						Moyeora.showMainFrame(s1, s3, ism);
					}
				}

				else
				{
					JOptionPane.showMessageDialog(null, "Wrong ID or Password or Name");
				}		
			}
		});

		j3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e3) {
				String s1, s3; 
				char[] s2 = new char[20];
				String ad = "";
				s1=(String) t1.getText();
				s2=t2.getPassword();
				s3=t3.getText();

				for(char cha : s2) {
					Character.toString(cha);
					ad += (ad.equals(""))?""+cha+"":""+cha+"";
				}

				if(find(s1,ad,s3))
				{
					if(ad_find(s1))
					{
						Id = s1;
						Name = s3;
						ism = true;
						JOptionPane.showMessageDialog(null, "Hello, Sir!");
						MoyeoraScheduler Moyeora = new MoyeoraScheduler();
						Moyeora.showMainFrame(s1, s3, ism); // 메인 화면 실행
						System.out.println("Id in Login: "+s1);
						dispose();
					}
					else
					{
						JOptionPane.showMessageDialog(null, "This Id is not Admin's!");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Wrong ID or Password or Name");
				}
			}
		});
		setVisible(true);
	}

	public boolean find(String _Id, String _Password, String _Name)
	// For login
	{
		int i;
		Rselect();

		for(i=0; i<20; i++)
		{
			if(IDarr[i]==null)
				break;
			if(PasswordArr[i]==null)
				break;
			if(NameArr[i]==null)
				break;
			// if Id or password is null stop
			if(_Id.equals(IDarr[i]))
				if(_Password.equals(PasswordArr[i]))
					if(_Name.equals(NameArr[i]))
						return true;
		}
		return false;
	}

	public void Rselect()
	// Select Data from Register
	{
		int i=0;		
		String sql = "SELECT * FROM User;";
		try{
			conn = DB.getMySQLConnection();
			System.out.println("Connection Success");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while(rs.next()) {
				IDarr[i] = rs.getString(1);
				PasswordArr[i] = rs.getString(2);
				AdminArr[i] = rs.getBoolean(3);
				NameArr[i] = rs.getString(4);
				i++;
			}

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

	public boolean ad_find(String _Id)
	// confirm administrator id
	{
		boolean isMaster;
		int i=0;
		String sql = "SELECT Master FROM User where Id = "+ "'"+_Id +"'" +";";
		try{
			conn = DB.getMySQLConnection();
			System.out.println("Connection Success");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while(rs.next()) {
				isMaster=rs.getBoolean(1);
				if(isMaster==true)
					return true; // this id is administrator's
			}

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
		return false; // this id isn't administrator's
	}

	//MoyeoraScheduler와 연동
	public void setMain(MoyeoraScheduler Moyeora) {
		this.Moyeora = Moyeora;
	}

}

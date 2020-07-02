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
	//DB �۾��� �ʿ��� ��ü
	Connection conn = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	public String IDarr[] = new String[20], PasswordArr[] = new String[20], NameArr[] = new String[20];
	public boolean AdminArr[] = new boolean[20];
	public String Id;
	public String Name;
	public String MasterCode = "StartWithAdmin";
	boolean ism;
	public Login(){
		JPanel p = new JPanel();
		JButton b1 = new JButton("Register");
		JButton b2 = new JButton("Login");
		JTextField t1 = new JTextField();
		JPasswordField t2 = new JPasswordField();
		JTextField t3 = new JTextField();
		JTextField t4 = new JTextField();
		JLabel l0 = new JLabel("Welcome To Moyeora Scheduler");
		JLabel l1 = new JLabel("ID");
		JLabel l2 = new JLabel("Password");
		JLabel l3 = new JLabel("Name");
		JLabel l4 = new JLabel("MasterCode");
		l0.setBounds(200,50,375,50); //����
		l0.setHorizontalAlignment(JLabel.CENTER);
		l0.setFont(new Font("Ariel",Font.BOLD,20));
		b1.setBounds(360,275,90,20); //ȸ������
		b2.setBounds(360,250,90,20); //�α���
		t1.setBounds(350,150,100,15); //���̵�ĭ
		t2.setBounds(350,175,100,15); //��й�ȣĭ
		t3.setBounds(350,200,100,15); //�̸�ĭ
		t4.setBounds(350,225,100,15); //�������ڵ�ĭ
		l1.setBounds(280,150,75,15); //���̵�
		l2.setBounds(280,175,75,15); //��й�ȣ
		l3.setBounds(280,200,75,15); //�̸�
		l4.setBounds(280,225,75,15); //�������ڵ�
		add(b1);
		add(b2);
		add(t1);
		add(t2);
		add(t3);
		add(t4);
		add(l0);
		add(l1);
		add(l2);
		add(l3);
		add(l4);
		setSize(750,500);
		setLayout(null);
		setVisible(true);

		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Moyeora.showRegisterFrame();// ȸ������â ����
			}
		});;

		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e2) {
				String s1, s3, s4;
				String pw = "";
				char[] s2 = new char[20];
				s1=t1.getText();
				s2=t2.getPassword();
				s3=t3.getText();
				s4=t4.getText();

				for(char cha : s2) {
					pw += Character.toString(cha);
				}

				if(find(s1,pw,s3)) // DB�� ��� ID�� Password�� ���� �Է��� ID�� Password, �̸��� ��ġ�ϴ��� Ȯ�����ִ� �޼ҵ�
				{
					if(MasterCode.equals(s4)) {
						ad_assign(s1);
						ism = true;
						JOptionPane.showMessageDialog(null, "Hello, Sir!");
						Moyeora.showMainFrame(s1, s3, ism);
					}
					else if(s4.equals(""))
					{
						no_assign(s1);
						ism = false;
						JOptionPane.showMessageDialog(null, "Success Login!");
						Moyeora.showMainFrame(s1, s3, ism);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Wrong Master Code");
					}	
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Wrong ID or Password or Name");
				}		
			}
		});
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
				//��ü ����
				rs.close(); 
				pstmt.close(); 
				conn.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public void ad_assign(String _Id)
	// if login with master code, that id becomes administrator id
	{
		String sql = "UPDATE User SET isMaster = '1' WHERE ID = "+ "'"+_Id +"'" +";";
		try{
			conn = DB.getMySQLConnection();
			System.out.println("Connection Success");
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
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
	
	public void no_assign(String _Id)
	// if login with master code, that id becomes administrator id
	{
		String sql = "UPDATE User SET isMaster = '0' WHERE ID = "+ "'"+_Id +"'" +";";
		try{
			conn = DB.getMySQLConnection();
			System.out.println("Connection Success");
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
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

	//MoyeoraScheduler�� ����
	public void setMain(MoyeoraScheduler Moyeora) {
		this.Moyeora = Moyeora;
	}

}
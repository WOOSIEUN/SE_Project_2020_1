import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Login extends JFrame{

	public String Id;
	public Login(){
		DB a = new DB();
		boolean ism = false;
		JPanel p = new JPanel();
		JButton j1 = new JButton("Register");
		JButton j2 = new JButton("Login");
		JTextField t1 = new JTextField();
		JPasswordField t2 = new JPasswordField();
		JLabel l0 = new JLabel("Welcome To Moyeora Scheduler");
		JLabel l1 = new JLabel("ID");
		JLabel l2 = new JLabel("Password");
		l0.setBounds(100,50,100,100);//제목
		j1.setBounds(360,225,90,20); //회원가입
		j2.setBounds(360,200,90,20); //로그인
		t1.setBounds(350,150,100,15); //아이디칸
		t2.setBounds(350,175,100,15); //비밀번호칸
		l1.setBounds(280,150,75,15); //아이디
		l2.setBounds(280,175,75,15); //비밀번호
		add(j1);
		add(j2);
		add(t1);
		add(t2);
		add(l1);
		add(l2);
		setSize(750,500);
		setLayout(null);
		setVisible(true);
		
		j1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Register gui2 = new Register();
				gui2.setVisible(true);
			}
		});;
		
		j2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e2) {
				String s1;
				String pw = "";
				char[] s2 = new char[20];
				s1=t1.getText();
				s2=t2.getPassword();
					
				for(char cha : s2) {
					pw += Character.toString(cha);
				}

				if(a.find(s1,pw)) // DB에 담긴 ID와 Password와 내가 입력한 ID와 Password가 일치하는지 확인해주는 메소드
				{
					Id = s1;
					JOptionPane.showMessageDialog(null, "Sucess Login!");
					MainFrame gui = new MainFrame(s1, pw, ism); //메인 화면으로 이동
					System.out.println("Id in Login: "+s1);
					gui.setVisible(true);
					dispose();
				}

				else
				{
					JOptionPane.showMessageDialog(null, "Wrong ID or Password");
				}		
			}
		});
	}

	public String getId()
	{
		return Id;
	}

	public static void main(String[] args)
	{
		Login gui = new Login();
		gui.setVisible(true);
	}

}

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

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
		JTextField t3 = new JTextField();
		JLabel l0 = new JLabel("Welcome To Moyeora Scheduler");
		JLabel l1 = new JLabel("ID");
		JLabel l2 = new JLabel("Password");
		JLabel l3 = new JLabel("Name");
		l0.setBounds(100,50,100,100);//����
		j1.setBounds(360,250,90,20); //ȸ������
		j2.setBounds(360,225,90,20); //�α���
		t1.setBounds(350,150,100,15); //���̵�ĭ
		t2.setBounds(350,175,100,15); //��й�ȣĭ
		t3.setBounds(350,200,100,15); //�̸�ĭ
		l1.setBounds(280,150,75,15); //���̵�
		l2.setBounds(280,175,75,15); //��й�ȣ
		l3.setBounds(280,200,75,15); //�̸�
		add(j1);
		add(j2);
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
				Register gui2 = new Register();
				gui2.setVisible(true);
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

				if(a.find(s1,pw)) // DB�� ��� ID�� Password�� ���� �Է��� ID�� Password�� ��ġ�ϴ��� Ȯ�����ִ� �޼ҵ�
				{
					Id = s1;
					JOptionPane.showMessageDialog(null, "Success Login!");
					MainFrame gui = new MainFrame(s1, s3, ism); //���� ȭ������ �̵�
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
	
	public String get_Name()
	{
		return Name;
	}

	public static void main(String[] args)
	{
		Login gui = new Login();
		gui.setVisible(true);
	}

}

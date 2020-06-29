import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends JFrame{
	public Register() {
		JPanel p = new JPanel();
		JLabel l1 = new JLabel("ID");
		JLabel l2 = new JLabel("Password");
		JLabel l3 = new JLabel("Password Check");
		JLabel l4 = new JLabel("Name");
		JButton b1 = new JButton("Register");
		JButton b2 = new JButton("Cancel");
		add(l1);
		add(l2);
		add(l3);
		add(l4);
		add(b1);
		add(b2);
		JTextField t1 = new JTextField();
		JTextField t2 = new JTextField();
		JTextField t3 = new JTextField();
		JTextField t4 = new JTextField();
		add(t1);
		add(t2);
		add(t3);
		add(t4);
		l1.setBounds(250,150,100,15); //ID
		l2.setBounds(250,175,100,15); //Password
		l3.setBounds(250,200,100,15); //Password Check
		l4.setBounds(250,225,100,15); //Name
		b1.setBounds(275,250,85,15); //Enroll
		b2.setBounds(375,250,85,15); //Cancel
		t1.setBounds(350,150,100,15);
		t2.setBounds(350,175,100,15);
		t3.setBounds(350,200,100,15);
		t4.setBounds(350,225,100,15);
		DB a = new DB();
		
		setSize(750,500);
		setTitle("Register");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLayout(null);
		setVisible(true);

		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent T) {
				try {
					String s1; //ID
					s1=t1.getText();
					String s2, s3, n; //Password, Password Check & Name
					s2=t2.getText();
					s3=t3.getText();
					n=t4.getText();
					BufferedWriter bos = new BufferedWriter(new FileWriter("ID_List.txt",true));
					if(s3.equals(s2)) { //비밀번호가 제대로 확인됐을 시
						System.out.println("Id: "+ s1 + " " + "Pw: "+s2);
						a.Rinsert(s1,s2,n); //DB에 ID와 Password 삽입
						JOptionPane.showMessageDialog(null, "Success Register");
						dispose(); // Main 화면으로 이동 시 dispose() 삭제 후 새로운 클래스 생성으로 대체
					}
					else { //비밀번호를 잘못 입력했을 시
						JOptionPane.showMessageDialog(null, "Check Password");
					}
				}catch(Exception Main) {
							JOptionPane.showMessageDialog(null, "Failed to Register");
						}
					}
				});
				b2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent T2) {
						JOptionPane.showMessageDialog(null, "Cancelled");
						dispose();
					}
				});
			}
	
		public static void main(String[] args)
		{
			Register gui = new Register();
			gui.setVisible(true);
		}
	}
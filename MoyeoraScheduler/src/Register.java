import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.awt.*;
import java.io.IOException;

public class Register extends JFrame{
	public Register() {
		JPanel p = new JPanel();
		JLabel l1 = new JLabel("ID");
		JLabel l2 = new JLabel("Password");
		JLabel l3 = new JLabel("Password Check");
		JButton b1 = new JButton("Register");
		JButton b2 = new JButton("Cancel");
		add(l1);
		add(l2);
		add(l3);
		add(b1);
		add(b2);
		JTextField t1 = new JTextField();
		JTextField t2 = new JTextField();
		JTextField t3 = new JTextField();
		add(t1);
		add(t2);
		add(t3);
		l1.setBounds(600,300,200,30); //ID
		l2.setBounds(600,350,200,30); //Password
		l3.setBounds(600,400,200,30); //Password Check
		b1.setBounds(550,500,170,30); //Enroll
		b2.setBounds(750,500,170,30); //Cancel
		t1.setBounds(700,300,200,30);
		t2.setBounds(700,350,200,30);
		t3.setBounds(700,400,200,30);
		DB a = new DB();
		
		setSize(1500,1000);
		setTitle("Register");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLayout(null);
		setVisible(true);

		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent T) {
				try {
					String s1;
					s1=t1.getText();
					String s2, s3;
					s2=t2.getText();
					s3=t3.getText();
					BufferedWriter bos= new BufferedWriter(new FileWriter("ID_List.txt",true));
					if(s3.equals(s2)) { //비밀번호가 제대로 확인됐을 시
						System.out.println("Id: "+ s1 + "Pw: "+s2);
						a.Rinsert(s1,s2); //DB에 ID와 Password 삽입
						JOptionPane.showMessageDialog(null, "Success Register");
						dispose();
					}
					else { //비밀번호를 잘못 입력했을 시
						JOptionPane.showMessageDialog(null, "Check Password");
					}
				}catch(Exception Main) {
							JOptionPane.showMessageDialog(null, "Failed Register");
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
	}
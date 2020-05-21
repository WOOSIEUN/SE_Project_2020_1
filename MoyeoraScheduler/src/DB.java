import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
	public static Connection getMySQLConnection() {
		Connection conn = null;
		//�̸� ������ �� AWS RDS DB �ν��Ͻ��� ����
		String driver = "com.mysql.cj.jdbc.Driver";
		String dbURL = "jdbc:mysql://rds-mysql-se-project.ccpr71bawwpc.ap-northeast-2.rds.amazonaws.com/";
		String dbName = "MOYEORA";
		String user = "root";
		String pass = "pw123456"; //commit�� �ش�κ� �׻� ������ �� 
		
		try {
			Class.forName(driver); 						
			conn = DriverManager.getConnection(dbURL+dbName, user, pass);
			System.out.println("Success");
			
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return conn;
	}
	
}

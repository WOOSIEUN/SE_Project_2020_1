import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
	public static Connection getMySQLConnection() {
		Connection conn = null;
		//미리 생성해 둔 AWS RDS DB 인스턴스에 연결
		String driver = "com.mysql.cj.jdbc.Driver";
		String dbURL = "jdbc:mysql://rds-mysql-se-project.ccpr71bawwpc.ap-northeast-2.rds.amazonaws.com/";
		String dbName = "MOYEORA";
		String user = "root";
		String pass = ""; //commit시 해당부분 항상 삭제할 것 
		
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

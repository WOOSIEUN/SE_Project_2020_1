import java.sql.*;
import java.text.*;

public class DB {
	
	public String Id[] = new String[20], Password[] = new String[20], Name[] = new String[20];
	public boolean Admin[] = new boolean[20];
	
	public static Connection getMySQLConnection() {
		Connection conn = null;
		
		//미리 생성해 둔 AWS RDS DB 인스턴스에 연결
		String driver = "com.mysql.cj.jdbc.Driver";
		String dbURL = "jdbc:mysql://127.0.0.1:3307/scheduler?serverTimezone=UTC";
		String dbName = "MOYEORA";
		String user = "root";
		String pass = ""; //commit시 해당부분 항상 삭제할 것 

		try {
			Class.forName(driver); 						
			conn = DriverManager.getConnection(dbURL, user, pass);
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
		
		public void Rinsert(String _Id, String _password, String _name)
		// input data after register for login 
		{
			Connection conn = null;
			PreparedStatement pstmt = null;
			boolean admin = false;
			
			String driver = "com.mysql.cj.jdbc.Driver";
			String dbURL = "jdbc:mysql://127.0.0.1:3307/scheduler?serverTimezone=UTC";
			String dbName = "MOYEORA";
			String user = "root";
			String pass = "";
			
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(dbURL, user, pass);
				System.out.println("Connection Success");
				String sql = "INSERT INTO moyeora VALUES (?, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, _Id);
				pstmt.setString(2, _password);
				pstmt.setBoolean(3, admin);
				pstmt.setString(4, _name);
				
				int count = pstmt.executeUpdate();

				if(count==0) {
					System.out.println("Failed to input data");
				}
				else {
					System.out.println("Data input success");
				}
			}
			catch(ClassNotFoundException e) {
				System.out.println("Failed to load Driver");
			}
			catch(SQLException e) {
				System.out.println("Error: "+e);
			}
			finally {
				try {
					if(conn != null && !conn.isClosed()) {
						conn.close();
					}
					if(pstmt != null && !pstmt.isClosed()) {
						pstmt.close();
					}
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void Rselect()
		// Select Data from Register
		{
			Connection conn = null;
			ResultSet rs = null;
			Statement stmt = null;
			int i=0;
			
			String driver = "com.mysql.cj.jdbc.Driver";
			String dbURL = "jdbc:mysql://127.0.0.1:3307/scheduler?serverTimezone=UTC";
			String dbName = "MOYEORA";
			String user = "root";
			String pass = "";
			
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(dbURL, user, pass);
				System.out.println("Connection Success");
				stmt = conn.createStatement();
				String sql = "SELECT * FROM moyeora;";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					Id[i] = rs.getString(1);
					Password[i] = rs.getString(2);
					Admin[i] = rs.getBoolean(3);
					Name[i] = rs.getString(4);
					i++;
				}
			}
			catch(ClassNotFoundException e) {
				System.out.println("Failed to load Driver");
			}
			catch(SQLException e) {
				System.out.println("Error: "+e);
			}
			finally {
				try {
					if(conn != null && !conn.isClosed()) {
						conn.close();
					}
					if(stmt != null && !stmt.isClosed()) {
						stmt.close();
					}
					if(rs != null && !rs.isClosed()) {
						rs.close();
					}
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
			}	
		}
		
		public boolean find(String _Id, String _Password)
		// For login
		{
			int i;
			Rselect();

			for(i=0; i<20; i++)
			{
				if(Id[i]==null)
					break;
				if(Password[i]==null)
					break;
				// if Id or password is null stop
				if(_Id.equals(Id[i]))
					if(_Password.equals(Password[i]))
						return true;
			}
			return false;
		}
		
		public boolean ad_find(String _Id)
		// confirm administrator id
		{
			boolean isAdmin;
			Connection conn = null;
			ResultSet rs = null;
			Statement stmt = null;
			int i=0;
			
			String driver = "com.mysql.cj.jdbc.Driver";
			String dbURL = "jdbc:mysql://127.0.0.1:3307/scheduler?serverTimezone=UTC";
			String dbName = "MOYEORA";
			String user = "root";
			String pass = "";

			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(dbURL+dbName, user, pass);
				System.out.println("Connection Success");
				String sql = "SELECT Admin FROM moyeora where Id = "+ "'"+_Id +"'" +";";
				System.out.println(sql);
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					isAdmin=rs.getBoolean(1);
					if(isAdmin==true)
						return true; // this id is administrator's
				}
			}
			catch(ClassNotFoundException e) {
				System.out.println("Failed to load Driver");
			}
			catch(SQLException e) {
				System.out.println("Error: "+e);
			}
			finally {
				try {
					if(conn != null && !conn.isClosed()) {
						conn.close();
					}
					if(stmt != null && !stmt.isClosed()) {
						stmt.close();
					}
					if(rs != null && !rs.isClosed()) {
						rs.close();
					}
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
			}
			return false; // this id isn't administrator's
		}
		
		public String[] getId()
		{
			Rselect();
			return Id;
		}

		public String[] getPW()
		{
			Rselect();
			return Password;
		}
		
		public String[] get_Name()
		{
			Rselect();
			return Name;
		}
	}
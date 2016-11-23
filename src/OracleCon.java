import java.sql.*;

class OracleCon {
	
	private static Connection con;
	private static boolean exception;
	private static String exceptMsg;
	
	public OracleCon(){
		
		
		
	}
	
	public static ResultSet execute(String query){
		
		exception = false;
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ResultSet rs = null;
		try {
			
			System.out.println("Hello1");
			
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "tssaini", "G9o0o0g9l1e31");
			
			System.out.println("Hello");
			Statement stmt = con.createStatement();
			
			// step4 execute query
			rs = stmt.executeQuery(query);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			exception = true;
			exceptMsg = e.getMessage();
		}
		
		return rs;
	}
	
	public static String getExceptionMsg(){
		return exceptMsg;
	}
	
	public static boolean exception(){
		return exception;
	}
	
	public static void close(){
		
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
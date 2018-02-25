package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class Students {
	//Database name: db363demoss Username: dbu363demoss Password: CziY1366
	
    private String dbUrl = "jdbc:mysql://localhost:3306/university";
    private String user = "root";
    private String password = "2591gHsS";
    private static Connection con;
	
    private void viewDB() {
        try {
            // Load the driver (registers itself)
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception E) {
            System.err.println("Unable to load driver.");
            E.printStackTrace();
        }

        try {
            // Connect to the database
            con = DriverManager.getConnection(dbUrl, user, password);
            System.out.println("*** Connected to the database ***");

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }
	
	
	public static void main(String args[]){
		Students project2 = new Students();
		project2.viewDB();
		Statement statement1;
		Statement statement2;
		ResultSet rs = null;
		try {
			statement1 =  con.createStatement();
			statement2 = con.createStatement();
			rs = statement1.executeQuery("select * from Student s");
		
			System.out.println(rs); // testing only, remove when done with project
			int oldCredits = 0;
			int numClasses = 0;
			int totalCredits = 0;
			int oldGPA = 0;
			int studentID = 0;
			ResultSet rs2 = null;
			// loop for retrieving and editing info for each student
			while(rs.next()) {
				// select id of this student
				studentID = rs.getInt("StudentID");
				oldCredits = rs.getInt("CreditHours");
				oldGPA = rs.getInt("GPA");
				System.out.println(studentID); // testing only as well
				// find matching enrollment records 
				rs2 = statement2.executeQuery("select * from Enrollment e where e.StudentID = "); 
				while(rs2.next()) {
					//count classes the student is in
					if(rs2.getInt("StudentID") == studentID) {
						numClasses++;
						
					}				
				}
				System.out.println("Number of Classes: " + numClasses);
				System.out.println("old GPA: " + oldGPA);
				System.out.println("Credit Hours: " + oldCredits);
				numClasses = 0;
				}
			}
		catch(Exception e1) {
			System.out.println("Could not select!");
			e1.printStackTrace();
		}
	}

}

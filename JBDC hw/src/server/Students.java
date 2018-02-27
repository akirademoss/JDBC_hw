package server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class Students {
	//ISU Database name: db363demoss Username: dbu363demoss Password: CziY1366
	
    private String dbUrl = "jdbc:mysql://localhost:3306/university";
    private String user = "root";
    private String password = "2591gHsS";
    private static Connection con;
    private ResultSet rs = null;
	Statement stmnt;
	
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
    
    private void getStudentInfo(){
    	int studentID;
    	String grade;
    	double gpa = 0.0;
		try {
			stmnt =  con.createStatement();
			rs = stmnt.executeQuery("select e.StudentID, e.Grade from Enrollment e");
			
			//Gpa = (CreditHours*GPA + 3*(GPA in course))/(CreditHours + 3) --CreditHours + 3 is the new total credit ours
			//Updates Gpa for specific student id
			PreparedStatement stmt1 = con.prepareStatement("update Student" + " " 
															+ "set GPA = (CreditHours*GPA + 3*?)/(CreditHours + 3)" 
															+ " " + "where StudentID = ?");
			
			PreparedStatement stmt2 = con.prepareStatement("update Student" + " " + "set CreditHours = CreditHours +3" 
															+ " " + "where StudentID = ?");
		
			while(rs.next()) {
				studentID = rs.getInt("StudentID");
				grade = rs.getString("Grade");
				grade = grade.trim();
				
				if(grade == "A"){
					gpa = 4.00;
				}	
				else if(grade == "A-"){
					gpa = 3.67;
				}
				else if(grade == "B+"){
					gpa = 3.33;
				}
				else if(grade == "B"){
					gpa = 3.00;
				}	
				else if(grade == "B-"){
					gpa = 2.67;
				}
				else if(grade == "C+"){
					gpa = 2.33;
				}
				else if(grade == "C"){
					gpa = 2.00;
				}
				else if(grade == "C-"){
					gpa = 1.67;
				}
				else if(grade == "D+"){
					gpa = 1.33;
				}
				else if(grade == "D"){
					gpa = 1.00;
				}
				else if(grade == "F"){
					gpa = 0;
				}
				
				stmt1.setDouble(1, gpa);
				stmt1.setInt(2,studentID);
				stmt2.setInt(1, studentID);
				stmt1.executeUpdate();
				stmt2.executeUpdate();
			}
			//update classification
			stmt1 = con.prepareStatement("update Student" + " " + "set Classification= ?" + " " + "where CreditHours <= ? and CreditHours >= ?");
			stmt1.setString(1, "Freshman");
			stmt1.setInt(2, 29);
			stmt1.setInt(3, 0);
			stmt1.executeUpdate();
			
			stmt1.setString(1, "Sophomore");
			stmt1.setInt(2, 59);
			stmt1.setInt(3, 30);
			stmt1.executeUpdate();
			
			stmt1.setString(1, "Junior");
			stmt1.setInt(2, 89);
			stmt1.setInt(3, 60);
			stmt1.executeUpdate();
			
			
			stmt1 = con.prepareStatement("update Student" + " " + "set Classification = ?" + " " + "where CreditHours >= ?");
			stmt1.setString(1, "Senior");
			stmt1.setInt(2, 90);
			stmt1.executeUpdate();
			
			stmt1.close();
			}
		catch(Exception e) {
			System.out.println("Could not select!");
			e.printStackTrace();
		}
    	
    }
    
 // Part B follows 
 	public void getTopSeniors() {
 		try {
 			File file = new File("JDBC_StudentsOutput.txt");
 			PrintWriter writer = new PrintWriter(file);
 			System.out.println("Part B- Top 5(ish) Seniors: ");
 			int count = 0;
 			double gpa = 0;
 		
 			// retrieves the GPA and name of seniors, sorted by said GPA
 			rs = stmnt.executeQuery("select s.GPA, p1.Name, p2.Name " 
 										+ "from Student s, Person p1, Person p2 " 
 										+ "where s.Classification = 'Senior'  and p1.ID = s.StudentID and p2.ID = s.MentorID " 
 										+"order by s.GPA desc");
 			//print top 5 seniors
 			while(rs.next() &&  count < 5) {
 				count++;
 				gpa = rs.getDouble(1);
 				writer.println("Student: " + rs.getString(2)+ "; Mentor: " + rs.getString(3)+ "; GPA: " + Math.round(gpa*100)/100.0);			 
 			}
 	
 			//finds and prints 
 			while(rs.next() ) {
 				if(gpa == (rs.getDouble(1))){
 					writer.println("Student: " + rs.getString(2)+ "; Mentor: " + rs.getString(3)+ "; GPA: " + Math.round(gpa*100)/100.0);	
 				}
 			}
 			writer.close();
 			rs.close();
 			System.out.print("Query Complete!");
 		}
 		catch(Exception e2) {
 			System.out.println("Error retrieing top seniors or opening file!");
 		}
 	}

	
	
	public static void main(String args[]){
		Students project2 = new Students();
		project2.viewDB();
		//Part A
		project2.getStudentInfo();
		//Part B
		project2.getTopSeniors();
		
	}

}

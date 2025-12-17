package studentgrader;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import studentgrader.Student.Students;

@WebServlet("/showStudents")
public class Record extends HttpServlet  {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        ArrayList<Student.Students> students = getStudents(); 
        try{ServletContext context = getServletContext();
        String driver = context.getInitParameter("driver");
        String url = context.getInitParameter("connection");
        String dbUser = context.getInitParameter("dusername");
        String dbPass = context.getInitParameter("dpassword");

        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, dbUser, dbPass);

        String sql = "SELECT * FROM record WHERE grade IN ('A+', 'A', 'B', 'C', 'D') ORDER BY FIELD(grade, 'A+', 'A', 'B', 'C', 'D')";

        PreparedStatement ps = con.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();


        
        out.println("<html>");
        out.println("<head>");
        out.println("<style>");
        out.println("table { width: 100%; border-collapse: collapse; font-family: Arial; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
        out.println("tr:hover { background-color: #ddd; }");
        out.println("h2 { font-family: Arial; color: #333; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h2>Student Records</h2>");
        out.println("<table>");
        out.println("<tr><th>Name</th><th>Roll No</th><th>Class</th><th>Math</th><th>Science</th><th>Hindi</th><th>English</th><th>Total</th><th>Percentage</th><th>Grade</th></tr>");

        while (rs.next()) {
            out.println("<tr>");
            out.println("<td>" + rs.getString("name") + "</td>");
            out.println("<td>" + rs.getString("rollno") + "</td>");
            out.println("<td>" + rs.getString("class") + "</td>");
            out.println("<td>" + rs.getInt("math") + "</td>");
            out.println("<td>" + rs.getInt("science") + "</td>");
            out.println("<td>" + rs.getInt("hindi") + "</td>");
            out.println("<td>" + rs.getInt("english") + "</td>");
            out.println("<td>" + rs.getInt("total") + "</td>");
            out.println("<td>" + rs.getDouble("percentage") + "</td>");
            out.println("<td>" + rs.getString("grade") + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("</body></html>");

        // Close connections
        rs.close();
        ps.close();
        con.close();}
        catch(Exception e)
        {
        	System.out.println(e);
        }
        
    }
        
    public ArrayList<Student.Students> getStudents() {
    	
        return Student.getStudentList(); // ✅ Use public getter method
    }
}

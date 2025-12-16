package studentgrader;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/addStudent")
public class Student extends HttpServlet {

    // In-memory list to store student records
    private static ArrayList<Students> studentList = new ArrayList<>();

    // Student data class
    public static class Students {
        String name;
        String rollNo;
        String studentClass;
        int maths;
        int science;
        int english;
        int hindi;
        int total;
        double percentage;
        String grade;

        public Students(String name, String rollNo, String studentClass,
                       int maths, int science, int english, int hindi,
                       int total, double percentage, String grade) {
            this.name = name;
            this.rollNo = rollNo;
            this.studentClass = studentClass;
            this.maths = maths;
            this.science = science;
            this.english = english;
            this.hindi = hindi;
            this.total = total;
            this.percentage = percentage;
            this.grade = grade;
        }
    }

    // Function to add student to the list
    public void addStudent(Students student) {
        studentList.add(student);
    }

    // Handle POST request from the HTML form
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            // Get form data
            String name = req.getParameter("name");
            String rollNo = req.getParameter("rollno");
            String sClass = req.getParameter("class");

            int maths = Integer.parseInt(req.getParameter("maths"));
            int science = Integer.parseInt(req.getParameter("science"));
            int english = Integer.parseInt(req.getParameter("english"));
            int hindi = Integer.parseInt(req.getParameter("hindi"));

            int total = maths + science + english + hindi;
            double percentage = total / 4.0;
            String grade;

            if (percentage >= 90) grade = "A+";
            else if (percentage >= 80) grade = "A";
            else if (percentage >= 70) grade = "B";
            else if (percentage >= 60) grade = "C";
            else grade = "D";

            // Create student object and add to list
            Students student = new Students(name, rollNo, sClass,
                    maths, science, english, hindi, total, percentage, grade);
            addStudent(student);

            // Respond
            out.println("<h3 style='color:green;'>Student added successfully!</h3>");
            out.println("<html><body>");
            out.println("<h2>Student Added Successfully!</h2>");
            out.println("<p>Name: " + name + "</p>");
            out.println("<p>Roll No: " + rollNo + "</p>");
            out.println("<p>Class: " + sClass + "</p>");
            out.println("<p>Marks: Maths=" + maths + ", Science=" + science + ", English=" + english + ", Hindi=" + hindi + "</p>");
            out.println("<p>Total marks: " + total + "</p>");
            out.println("<p>Percentage " + percentage + "</p>");
            out.println("<p>Grade" + grade + "</p>");
            
            out.println("</body></html>");
            out.println("<a href='student.html'>← Back</a>");
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("driver");
            String url = context.getInitParameter("connection");
            String dbUser = context.getInitParameter("dusername");
            String dbPass = context.getInitParameter("dpassword");

            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, dbUser, dbPass);

            // Step 1: Check if email exists
            PreparedStatement ps = con.prepareStatement("INSERT into record (name ,rollno,class,math,science,hindi,english,total,percentage,grade) VALUES(?,?,?,?,?,?,?,?,?,?)");
          
			
            ps.setString(1, name);
            ps.setString(2,rollNo);
            ps.setString(3, sClass);
            ps.setLong(4, maths);
            ps.setLong(5, science);
            ps.setLong(6, hindi);
            ps.setLong(7, english);
            ps.setLong(8, total);
            ps.setDouble(9, percentage);
            ps.setString(10, grade);
            
            
            int row = ps.executeUpdate();
            System.out.println("effected row"+row);


        } catch (Exception e) {
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }
 // Getter to retrieve student list
    public static ArrayList<Students> getStudentList() {
        return studentList;
    }

}

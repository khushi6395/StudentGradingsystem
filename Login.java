9package studentgrader;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

  
public class  Login extends HttpServlet{  
public void doPost(HttpServletRequest req,HttpServletResponse res)  throws IOException,ServletException
{  
	res.setContentType("text/html");  
	PrintWriter out=res.getWriter();  
String uid=req.getParameter("email");
String pass=req.getParameter("password");
ServletContext d_obj=getServletContext();
String dname=d_obj.getInitParameter("driver");
String co=d_obj.getInitParameter("connection");
String uname=d_obj.getInitParameter("dusername");
String pas=d_obj.getInitParameter("dpassword");


try {
Class.forName("com.mysql.cj.jdbc.Driver");


Connection con=DriverManager.getConnection(co,uname,pas);


PreparedStatement ps = con.prepareStatement(
        "SELECT name FROM login WHERE email = ? AND pass = ?"
    );
    ps.setString(1, uid);
    ps.setString(2, pass);

    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        String name = rs.getString("name");
        out.println("<h3>Welcome, " + name + "!</h3>");
        RequestDispatcher rd = req.getRequestDispatcher("/show.html");
        rd.forward(req, res);
        
    } else {
        out.println("<h3>Invalid User ID or Password.</h3>");




}
}
catch(Exception e)
{
	out.print(e);
}
}
}

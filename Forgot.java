
package studentgrader;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Forgot extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String email = req.getParameter("email");
        String npass = req.getParameter("npass");
        String cpass = req.getParameter("cpass");

        try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("driver");
            String url = context.getInitParameter("connection");
            String dbUser = context.getInitParameter("dusername");
            String dbPass = context.getInitParameter("dpassword");

            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, dbUser, dbPass);

            // Step 1: Check if email exists
            PreparedStatement ps = con.prepareStatement("SELECT name FROM login WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // If password fields are null, show form first
                if (npass == null || cpass == null) {
                    // Show form
                    out.println("<!DOCTYPE html>");
                    out.println("<html><head><title>Forgot Password</title>");
                    out.println("<style>");
                    out.println("body { font-family: Arial, sans-serif; background: #f0f2f5; display: flex; justify-content: center; align-items: center; height: 100vh; }");
                    out.println(".container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); width: 350px; }");
                    out.println("h2 { text-align: center; color: #333; }");
                    out.println("input[type='password'] { width: 100%; padding: 12px; margin-top: 10px; border: 1px solid #ccc; border-radius: 6px; }");
                    out.println("button { width: 100%; padding: 12px; margin-top: 15px; background-color: #4f46e5; color: white; border: none; border-radius: 6px; cursor: pointer; }");
                    out.println("button:hover { background-color: #4338ca; }");
                    out.println(".link { text-align: center; margin-top: 15px; }");
                    out.println(".link a { color: #4f46e5; text-decoration: none; }");
                    out.println("</style>");
                    out.println("</head><body>");

                    out.println("<div class='container'>");
                    out.println("<h2>Reset Password</h2>");
                    out.println("<form method='post' action='forgot'>");
                    out.println("<input type='hidden' name='email' value='" + email + "'>");
                    out.println("<label for='npass'>New password</label>");
                    out.println("<input type='password' id='npass' name='npass' required>");
                    out.println("<label for='cpass'>Confirm password</label>");
                    out.println("<input type='password' id='cpass' name='cpass' required>");
                    out.println("<button type='submit'>Save</button>");
                    out.println("</form>");
                    out.println("<div class='link'><a href='login.html'>&larr; Back to login</a></div>");
                    out.println("</div>");
                    out.println("</body></html>");
                } else {
                    // Passwords entered — validate and update
                    if (!npass.equals(cpass)) {
                        out.println("<h3 style='color:red; text-align:center;'>Passwords do not match!</h3>");
                    } else {
                        PreparedStatement ps1 = con.prepareStatement("UPDATE login SET pass = ? WHERE email = ?");
                        ps1.setString(1, npass);
                        ps1.setString(2, email);
                        int updated = ps1.executeUpdate();

                        if (updated > 0) {
                            out.println("<h3 style='color:green; text-align:center;'>Password updated successfully!</h3>");
                            out.println("<div style='text-align:center;'><a href='index.html'>Go to Login</a></div>");
                        } else {
                            out.println("<h3 style='color:red; text-align:center;'>Failed to update password.</h3>");
                        }
                    }
                }
            } else {
                out.println("<h3 style='color:red; text-align:center;'>Email not found.</h3>");
            }

            con.close();
        } catch (Exception e) {
            out.println("<h3 style='color:red;'>" + e + "</h3>");
        }
    }
}

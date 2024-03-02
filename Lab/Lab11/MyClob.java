import java.sql.*;

public class MyClob {
    // 加载驱动
    static {
        try {
            Class.forName("COM.ibm.db2.jdbc.app.DB2Driver");
        } catch (Exception e) {
            System.exit(1);
        }
    }

    public static void main(String[] args) throws Exception{
        // 连接数据库
        String url = "jdbc:db2:sample";
        String userID = "db2admin";
        String passwd = "db2admin";
        Connection con = DriverManager.getConnection(url, userID, passwd);

        // 定义变量
        String resume = null;
        String empnum = "000130";
        int startper = 0, startper1, startdpt = 0;
        PreparedStatement stmt1, stmt2, stmt3 = null;
        String sql1, sql2,sql3 = null;
        String empno, resumefmt = null;
        Clob resumelob = null;
        ResultSet rs1, rs2, rs3 = null;

        // 完善并执行sql1
        sql1 = "SELECT POSSTR(RESUME, 'Personal') "
                + "FROM JLU.EMP_RESUME "
                + "WHERE EMPNO = ? AND RESUME_FORMAT = 'ascii' ";
        stmt1 = con.prepareStatement(sql1);
        stmt1.setString(1, empnum);
        rs1 = stmt1.executeQuery();
        while (rs1.next()) {
            startper = rs1.getInt(1);
        }

        // 完善并执行sql2
        sql2 = "SELECT POSSTR(RESUME, 'Department') "
                + "FROM JLU.EMP_RESUME "
                + "WHERE EMPNO = ? AND RESUME_FORMAT = 'ascii' ";
        stmt2 = con.prepareStatement(sql2);
        stmt2.setString(1, empnum);
        rs2 = stmt2.executeQuery();
        while (rs2.next()) {
            startdpt = rs2.getInt(1);
        }

        // 完善并执行sql3
        startper1 = startper - 1;
        sql3 = "SELECT EMPNO, RESUME_FORMAT, SUBSTR(RESUME, 1, ?) || SUBSTR(RESUME, ?) AS RESUME "
                + "FROM JLU.EMP_RESUME "
                + "WHERE EMPNO = ? AND RESUME_FORMAT = 'ascii' ";
        stmt3 = con.prepareStatement(sql3);
        stmt3.setInt(1, startper1);
        stmt3.setInt(2, startdpt);
        stmt3.setString(3,empnum);
        rs3 = stmt3.executeQuery();
        while (rs3.next()) {
            empno = rs3.getString(1);
            resumefmt = rs3.getString(2);
            resumelob = rs3.getClob(3);
            long len = resumelob.length();
            int len1 = (int) len;
            String resumeout = resumelob.getSubString(1, len1);
            // 输出
            System.out.println("Employee Number: " + empno);
            System.out.println("\nResume Format: " + resumefmt);
            System.out.println("\nResume: " + resumeout);
        }

        // 释放资源
        if (rs1 != null) {
            rs1.close();
        }
        if (rs2 != null) {
            rs2.close();
        }
        if (rs3 != null) {
            rs3.close();
        }
        if (stmt1 != null) {
            stmt1.close();
        }
        if (stmt2 != null) {
            stmt2.close();
        }
        if (stmt3 != null) {
            stmt3.close();
        }
        if (con != null) {
            con.close();
        }
    }
}
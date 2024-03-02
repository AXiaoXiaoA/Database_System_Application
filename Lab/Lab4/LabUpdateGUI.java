import java.sql.*;
import java.io.*;
import java.util.*;
import java.math.*;
import javax.swing.JOptionPane;

public class LabUpdateGUI {

    // 用于加载数据库驱动程序
    static {
        try {
            Class.forName ("COM.ibm.db2.jdbc.app.DB2Driver");
        } catch (Exception e) {
            System.out.println ("\n  Error loading DB2 Driver...\n");
            System.out.println (e);
            System.exit(1);
        }
    }

    public static void main( String args[]) throws Exception
    {
        /** 定义变量 **/
        String name = "";
        String deptno  = "";
        short id = 0;
        double salary = 0;
        String job = "";
        short NumEmp = 0;
        String sqlstmt = "UPDATE JLU.STAFF SET SALARY = SALARY * 1.05 WHERE DEPT = ?";
        String s = " ";
        int mydeptno = 0;
        int SQLCode = 0;
        String SQLState = "     ";
        BufferedReader in = new BufferedReader( new InputStreamReader (System.in));

        /** 建立连接并设置默认上下文 **/
        System.out.println("Connect statement follows:");
        // (3) 建立连接
        Connection sample = DriverManager.getConnection("jdbc:db2:sample","db2admin", "db2admin");
        System.out.println("Connect completed");
        // (4) 关闭自动提交
        sample.setAutoCommit(false);

        /** 弹出窗口，获取输入部门编号 **/
        s =  JOptionPane.showInputDialog(null,"This program will update the salaries for a department\n" + "Please enter a department number","Input",JOptionPane.PLAIN_MESSAGE);
        deptno = s.substring(0,2);
        mydeptno = Integer.parseInt(deptno);

        /** 发出SQL语句 **/
        System.out.println("Statement stmt follows");
        try {
            // (5) 创建PreparedStatement对象(pstmt)，使用prepareStatement()
            PreparedStatement pstmt = sample.prepareStatement(sqlstmt);

            // (6) 将marker设置为部门，并存放在mydeptno中
            pstmt.setInt(1, mydeptno);

            // (7) 执行SQL语句，修改的行数保存在updateCount中
            int updateCount = pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Number of rows updated: " + updateCount, "Success", JOptionPane.PLAIN_MESSAGE);
        } catch ( SQLException x ) {
            // (8) 发生错误，检索SQL代码
            SQLCode = x.getErrorCode();
            SQLState = x.getSQLState();
            String Message = x.getMessage();
            JOptionPane.showMessageDialog(
                    null,
                    "SQLCODE: " + SQLCode + "\nSQLSTATE: " + SQLState + "\nSQLERRM: " + Message,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        System.exit(0);
    }

}





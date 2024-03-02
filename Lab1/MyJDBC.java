import java.sql.*;
import java.lang.*;

// MyJDBC类
class MyJDBC {

    // 加载数据库驱动程序
    static {
        try {
            Class.forName("COM.ibm.db2.jdbc.app.DB2Driver");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main (String argv[]) {
        try {
            /* 检查命令行参数，确定数据库连接方式 */
            Connection con = null;
            String url = "jdbc:db2:sample";
            // 若提供用户名和密码，则使用默认连接模式
            if (argv.length == 0) {
                con = DriverManager.getConnection(url);
            }
            // 若提供了用户名和密码，则使用用户名和密码连接数据库
            else if (argv.length == 2) {
                String userid = argv[0];
                String passwd = argv[1];
                con = DriverManager.getConnection(url, userid, passwd);
            }
            // 若命令行参数不符合预期，则抛出异常并显示用法信息
            else {
                throw new Exception("\n Usage: java MyJDBC[,username,password\n]");
            }

            /* 创建数据库Statement对象 */
            Statement stmt = con.createStatement();

            /* 查找并输出资大于40000的员工的员工号和姓氏 */
            ResultSet rs = stmt.executeQuery
                    ("SELECT EMPNO, LASTNAME " +
                            " FROM JLU.TEMPL " +
                            " WHERE SALARY > 40000 ");
            while (rs.next()) {
                System.out.println("empno = " + rs.getString(1) + "lastname = " + rs.getString(2));
            }

            /* 关闭查询结果、Statement对象和数据库连接 */
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.io.*;


public class PictureGUI {
    public static void main(String[] args) {
        createGUI();
    }
    // GUI
    private static void createGUI(){
        /// 创建窗口
        final JFrame frame = new JFrame("员工图片");
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        /// 选择功能
        // 查找员工图片按钮
        JButton searchButton = new JButton("查找员工图片");
        searchButton.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 30, 0, 15);
        frame.add(searchButton, gbc);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理查找员工按钮的操作
                System.out.println("查找员工按钮被点击");
                // 创建面板用于输入
                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new Insets(5, 5, 5, 5);
                panel.add(new JLabel("员工工号:"), gbc);
                gbc.gridx = 1;
                JTextField EMPNOField = new JTextField(10);
                panel.add(EMPNOField, gbc);
                gbc.gridx = 0;
                gbc.gridy = 1;
                panel.add(new JLabel("图片格式:"), gbc);
                gbc.gridx = 1;
                String[] imageFormats = {"jpeg", "png", "gif", "bmp", "tiff", "svg", "raw", "ico", "webp"};
                JComboBox imageFormatComboBox = new JComboBox(imageFormats);
                panel.add(imageFormatComboBox, gbc);

                // 显示对话框以输入信息
                int result1 = JOptionPane.showConfirmDialog(null, panel, "输入信息", JOptionPane.OK_CANCEL_OPTION);
                // 完成选择
                if (result1 == JOptionPane.OK_OPTION) {
                    // 获取信息
                    String EMPNO = EMPNOField.getText();
                    String imageFormat = (String)imageFormatComboBox.getSelectedItem();
                    // 检查用户是否输入了员工工号
                    if (EMPNO.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "请输入员工工号", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // 弹出文件选择器，用于选择路径
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("选择存放位置");
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int result = fileChooser.showOpenDialog(null);
                    // 用户选择了文件
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedDirectory = fileChooser.getSelectedFile();
                        String savePath = selectedDirectory.getAbsolutePath();
                        try {
                            if (searchImage(EMPNO, imageFormat, savePath)) {
                                JOptionPane.showMessageDialog(null, "图片查询成功", "Success", JOptionPane.PLAIN_MESSAGE);
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });

        // 插入员工图片按钮
        JButton insertButton = new JButton("插入员工图片");
        insertButton.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 15, 0, 30);
        frame.add(insertButton, gbc);
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理插入员工按钮的操作
                System.out.println("插入员工按钮被点击");
                // 获取信息
                String EMPNO = JOptionPane.showInputDialog(frame, "请输入员工工号:");
                // 完成选择
                if (EMPNO != null) {
                    // 弹出文件选择器，用于选择图片
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("选择员工图片");
                    int result = fileChooser.showOpenDialog(frame);
                    // 用户选择了文件
                    if (result == JFileChooser.APPROVE_OPTION) {
                        String selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                        // 处理选择的员工工号和图片路径
                        try {
                            if (insertImage(EMPNO, selectedFilePath)) {
                                JOptionPane.showMessageDialog(null, "图片插入成功", "Success", JOptionPane.PLAIN_MESSAGE);
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static {
        try {
            Class.forName("COM.ibm.db2.jdbc.app.DB2Driver");
        } catch (Exception e) {
            System.exit(1);
        }
    }
    // 插入图片
    private static boolean insertImage(String EMPNO, String filePath) {
        try {
            /// 连接数据库
            String url = "jdbc:db2:sample";
            String userID = "db2admin";
            String passwd = "db2admin";
            Connection con = DriverManager.getConnection(url, userID, passwd);

            /// 设置sql,插入图片
            String sql = "INSERT INTO JLU.EMP_PHOTO VALUES(?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, EMPNO);
            // 获取图片格式
            File file = new File(filePath);
            BufferedImage image = ImageIO.read(file);
            if (image != null) {
                String fileName = file.getName();
                String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
                pstmt.setString(2, fileExtension);
            } else {
                System.out.println("Failed to read the image.");
            }
            // 获取图片
            BufferedInputStream imageInput = new BufferedInputStream(new FileInputStream(file));
            pstmt.setBinaryStream(3, imageInput, (int)file.length());

            /// 执行sql
            pstmt.executeUpdate();

            /// 释放资源
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "图片插入失败",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    // 读取图片
    private static boolean searchImage(String EMPNO, String imageFormat, String filePath) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // 连接数据库
            String url = "jdbc:db2:sample";
            String userID = "db2admin";
            String passwd = "db2admin";
            con = DriverManager.getConnection(url, userID, passwd);
            // 查询图片
            String sql = "SELECT PICTURE FROM JLU.EMP_PHOTO WHERE EMPNO=? AND PHOTO_FORMAT=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, EMPNO);
            pstmt.setString(2, imageFormat);
            rs = pstmt.executeQuery();

            // 检查 ResultSet 是否有任何行
            if (rs.next()) {
                Blob blob = (Blob) rs.getBlob(1);
                java.io.InputStream inputStream = blob.getBinaryStream();
                String fileName = "\\" + EMPNO + "." + imageFormat;
                File fileOutput = new File(filePath + fileName);
                FileOutputStream fo = new FileOutputStream(fileOutput);
                int c;
                while ((c = inputStream.read()) != -1) {
                    fo.write(c);
                }
                fo.close();
                inputStream.close();
                return true;
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "未查找到图片",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "图片查询失败: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "图片查询失败: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }  finally {
            // 释放资源
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
}
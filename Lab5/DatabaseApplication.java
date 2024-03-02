import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class DatabaseApplication {
    public static void main(String[] args) {
        // 创建GUI界面
        new GUI().createGUI();
    }
}

// GUI类
class GUI {
    // 初始界面组件
    private JFrame frame = null;
    private JScrollPane scrollPane = null;
    private JButton singleRowInsertButton = new JButton("单行插入");
    private JButton multipleRowsInsertButton = new JButton("多行插入");
    private JButton subqueryInsertButton = new JButton("子查询插入");
    private JLabel employeeNum = null;

    // 初始化控件
    public GUI() {
        // 单行插入控件初始化
        singleRowInsertButton.setPreferredSize(new Dimension(100, 30));
        singleRowInsertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Employee employee = new Employee();

                // 创建输入面版
                System.out.println("单行插入按钮被点击");
                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();

                // 添加输入框和下拉框
                gbc.gridx = 0; gbc.gridy = 0;
                JTextField empNoField = addInputText(panel, gbc, "工        号 ", employee.getEMPNO());
                JTextField firstNameField = addInputText(panel, gbc, "名        字 ", employee.getFIRSTNME());
                JTextField midInitField = addInputText(panel, gbc, "中  间  名 ", employee.getMIDINIT());
                gbc.gridx = 0; gbc.gridy = 1;
                JTextField lastNameField = addInputText(panel, gbc, "姓        氏 ", employee.getLASTNAME());
                JTextField workDeptField = addInputText(panel, gbc,  "工作部门 ", employee.getWORKDEPT());
                JTextField phoneNoField = addInputText(panel, gbc, "联系方式 ", employee.getPHONENO());
                gbc.gridx = 0; gbc.gridy = 2;
                JTextField hireDateField = addInputText(panel, gbc, "入职日期 ", employee.getHIREDATE());
                JTextField jobField = addInputText(panel, gbc, "工作职务 ", employee.getJOB());
                JTextField edLevelField = addInputText(panel, gbc, "等        级 ", employee.getEDLEVEL());
                gbc.gridx = 0; gbc.gridy = 3;
                JComboBox sexComboBox = addInputBox(panel, gbc, "性        别 ", new String[]{" ", "M", "F"});
                JTextField birthDateField = addInputText(panel, gbc, "出生日期 ", employee.getBIRTHDATE());
                JTextField salaryField = addInputText(panel, gbc, "薪        资 ", employee.getSALARY());
                gbc.gridx = 0; gbc.gridy = 4;
                JTextField bonusField = addInputText(panel, gbc, "奖        金 ", employee.getBONUS());
                JTextField commField = addInputText(panel, gbc, "佣        金 ", employee.getCOMM());

                while (true) {
                    // 添加输入框和下拉框
                    empNoField.setText(employee.getEMPNO());
                    firstNameField.setText(employee.getFIRSTNME());
                    midInitField.setText(employee.getMIDINIT());
                    lastNameField.setText(employee.getLASTNAME());
                    workDeptField.setText(employee.getWORKDEPT());
                    phoneNoField.setText(employee.getPHONENO());
                    hireDateField.setText(employee.getHIREDATE());
                    jobField.setText(employee.getJOB());
                    edLevelField.setText(employee.getEDLEVEL());
                    birthDateField.setText(employee.getBIRTHDATE());
                    salaryField.setText(employee.getSALARY());
                    bonusField.setText(employee.getBONUS());
                    commField.setText(employee.getCOMM());

                    // 弹出窗口，处理用户点击确认的情况
                    int result = JOptionPane.showConfirmDialog(null, panel, "单行插入", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        // 重新获取用户输入的值
                        employee.setEMPNO(empNoField.getText());
                        employee.setFIRSTNME(firstNameField.getText());
                        employee.setMIDINIT(midInitField.getText());
                        employee.setLASTNAME(lastNameField.getText());
                        employee.setWORKDEPT(workDeptField.getText());
                        employee.setPHONENO(phoneNoField.getText());
                        employee.setHIREDATE(hireDateField.getText());
                        employee.setJOB(jobField.getText());
                        employee.setEDLEVEL(edLevelField.getText());
                        employee.setSEX((String)sexComboBox.getSelectedItem());
                        employee.setBIRTHDATE(birthDateField.getText());
                        employee.setSALARY(salaryField.getText());
                        employee.setBONUS(bonusField.getText());
                        employee.setCOMM(commField.getText());

                        // 检验输入是否合法
                        if (!employee.isEMPNOCurrect()) {
                            JOptionPane.showMessageDialog(null, "员工工号非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        if (!employee.isFIRSTNMECurrect()) {
                            JOptionPane.showMessageDialog(null, "员工名字非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        if (!employee.isMIDINITCurrect()) {
                            JOptionPane.showMessageDialog(null, "员工中间名首字母非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        if (!employee.isLASTNAMECurrect()) {
                            JOptionPane.showMessageDialog(null, "员工姓氏非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        if (!employee.isWORKDEPTCurrect()) {
                            JOptionPane.showMessageDialog(null, "员工工作部门非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        if (!employee.isPHONENOCurrect()) {
                            JOptionPane.showMessageDialog(null, "员工联系方式非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        if (!employee.isHIREDATECurrect()) {
                            JOptionPane.showMessageDialog(null, "员工入职时间非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        if (!employee.isJOBCurrect()) {
                            JOptionPane.showMessageDialog(null, "员工工作职务非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        if (!employee.isEDLEVELCurrect()) {
                            JOptionPane.showMessageDialog(null, "员工等级非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        if (!employee.isBIRTHDATECurrect()) {
                            JOptionPane.showMessageDialog(null, "员工出生日期非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        if (!employee.isSALARYCurrect()) {
                            JOptionPane.showMessageDialog(null, "员工薪资非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        if (!employee.isBONUSCurrect()) {
                            JOptionPane.showMessageDialog(null, "员工奖金非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        if (!employee.isCOMMCurrect()) {
                            JOptionPane.showMessageDialog(null, "员工佣金非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        // 输入合法则插入到数据库表中
                        Database database = Database.getInstance();
                        database.singleRowInsert(employee);
                        updateGUI();
                        break;
                    }
                    else {
                        break;
                    }
                }
            }
        });
        // 多行插入控件初始化
        multipleRowsInsertButton.setPreferredSize(new Dimension(100, 30));
        multipleRowsInsertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Employee> employees = new ArrayList();
                int[] rows = new int[50];

                /// 创建输入面版
                System.out.println("多行插入按钮被点击");
                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0; gbc.gridy = 0;
                JTable table = createTable();
                JScrollPane inputTableScrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                showTable(inputTableScrollPane, employees, rows);
                panel.add(inputTableScrollPane);

                while (true) {
                    /// 展示表格
                    showTable(inputTableScrollPane, employees, rows);
                    employees.clear();

                    /// 弹出窗口，处理用户点击确认的情况
                    int result = JOptionPane.showConfirmDialog(null, panel, "多行插入", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        // 获取表格数据
                        JTable inputTable = ((JTable) ((JViewport) inputTableScrollPane.getComponent(0)).getView());
                        DefaultTableModel tableModel = (DefaultTableModel) inputTable.getModel();
                        int rowCount = tableModel.getRowCount();
                        int colCount = tableModel.getColumnCount();
                        for (int i = 0; i < rowCount; ++i) {
                            int flag = 0;
                            for (int j = 0; j < colCount && flag != 1; ++j) {
                                if (!tableModel.getValueAt(i, j).equals("") && tableModel.getValueAt(i, j) != null) {
                                    flag = 1;
                                }
                            }
                            // 一行全空则跳过此行
                            if (flag == 0) {
                                rows[i] = 0;
                                continue;
                            }
                            // 有非空元素则创建对象
                            Employee employee = new Employee();
                            employee.setEMPNO((String) tableModel.getValueAt(i, 0));
                            employee.setFIRSTNME((String) tableModel.getValueAt(i, 1));
                            employee.setMIDINIT((String) tableModel.getValueAt(i, 2));
                            employee.setLASTNAME((String) tableModel.getValueAt(i, 3));
                            employee.setWORKDEPT((String) tableModel.getValueAt(i, 4));
                            employee.setPHONENO((String) tableModel.getValueAt(i, 5));
                            employee.setHIREDATE((String) tableModel.getValueAt(i, 6));
                            employee.setJOB((String) tableModel.getValueAt(i, 7));
                            employee.setEDLEVEL((String) tableModel.getValueAt(i, 8));
                            employee.setSEX((String) tableModel.getValueAt(i, 9));
                            employee.setBIRTHDATE((String) tableModel.getValueAt(i, 10));
                            employee.setSALARY((String) tableModel.getValueAt(i, 11));
                            employee.setBONUS((String) tableModel.getValueAt(i, 12));
                            employee.setCOMM((String) tableModel.getValueAt(i, 13));
                            employees.add(employee);
                            rows[i] = 1;
                        }
                        // 检验输入是否合法
                        int flag = 0;
                        for (int i = -1, j = 0; j < rowCount; ++j) {
                            if (rows[j] == 0) {
                                continue;
                            }
                            ++i;
                            if (!employees.get(i).isEMPNOCurrect()) {
                                JOptionPane.showMessageDialog(null, "第" + (j + 1) + "行员工工号非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                                flag = 1;
                                break;
                            }
                            if (!employees.get(i).isFIRSTNMECurrect()) {
                                JOptionPane.showMessageDialog(null, "第" + (j + 1) + "行员工名字非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                                flag = 1;
                                break;
                            }
                            if (!employees.get(i).isMIDINITCurrect()) {
                                JOptionPane.showMessageDialog(null, "第" + (j + 1) + "行员工中间名首字母非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                                flag = 1;
                                break;
                            }
                            if (!employees.get(i).isLASTNAMECurrect()) {
                                JOptionPane.showMessageDialog(null, "第" + (j + 1) + "行员工姓氏非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                                flag = 1;
                                break;
                            }
                            if (!employees.get(i).isWORKDEPTCurrect()) {
                                JOptionPane.showMessageDialog(null, "第" + (j + 1) + "行员工工作部门非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                                flag = 1;
                                break;
                            }
                            if (!employees.get(i).isPHONENOCurrect()) {
                                JOptionPane.showMessageDialog(null, "第" + (j + 1) + "行员工联系方式非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                                flag = 1;
                                break;
                            }
                            if (!employees.get(i).isHIREDATECurrect()) {
                                JOptionPane.showMessageDialog(null, "第" + (j + 1) + "行员工入职时间非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                                flag = 1;
                                break;
                            }
                            if (!employees.get(i).isJOBCurrect()) {
                                JOptionPane.showMessageDialog(null, "第" + (j + 1) + "行员工工作职务非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                                flag = 1;
                                break;
                            }
                            if (!employees.get(i).isEDLEVELCurrect()) {
                                JOptionPane.showMessageDialog(null, "第" + (j + 1) + "行员工等级非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                                flag = 1;
                                break;
                            }
                            if (!employees.get(i).isBIRTHDATECurrect()) {
                                JOptionPane.showMessageDialog(null, "第" + (j + 1) + "行员工出生日期非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                                flag = 1;
                                break;
                            }
                            if (!employees.get(i).isSALARYCurrect()) {
                                JOptionPane.showMessageDialog(null, "第" + (j + 1) + "行员工薪资非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                                flag = 1;
                                break;
                            }
                            if (!employees.get(i).isBONUSCurrect()) {
                                JOptionPane.showMessageDialog(null, "第" + (j + 1) + "行员工奖金非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                                flag = 1;
                                break;
                            }
                            if (!employees.get(i).isCOMMCurrect()) {
                                JOptionPane.showMessageDialog(null, "第" + (j + 1) + "行员工佣金非法输入", "Error", JOptionPane.ERROR_MESSAGE);
                                flag = 1;
                                break;
                            }
                        }
                        // 输入合法则插入到数据库表中
                        if (flag == 0) {
                            Database database = Database.getInstance();
                            database.multipleRowsInsert(employees);
                            updateGUI();
                            break;
                        }
                    }
                    else {
                        break;
                    }
                }
            }
        });
        // 子查询插入按钮初始化
        subqueryInsertButton.setPreferredSize(new Dimension(100, 30));
        subqueryInsertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /// 输入搜索条件
                System.out.println("子查询插入按钮被点击");
                String searchCondition = JOptionPane.showInputDialog(null, "请输入搜索条件:", "输入搜索条件", JOptionPane.QUESTION_MESSAGE);
                final List<Employee> selectedEmployees = new ArrayList();
                final int[] rows = new int[50];
                final int[] count = {0};

                /// 创建窗口
                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                // 左表为查询的表
                gbc.gridx = 0; gbc.gridy = 0;
                gbc.insets = new Insets(0, 10, 10, 20);
                final JTable leftTable = createTable();
                Database database = Database.getInstance();
                ResultSet rs = database.searchTable("JLU.EMPLOYEE", searchCondition);
                final JScrollPane leftTableScrollPane = new JScrollPane(leftTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                showTable(leftTableScrollPane, rs);
                panel.add(leftTableScrollPane, gbc);
                // 右侧为待插入的表
                gbc.gridx = 1;
                gbc.insets = new Insets(0, 20, 10, 20);
                final JTable rightTable = createTable();
                final JScrollPane rightTableScrollPane = new JScrollPane(rightTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                showTable(rightTableScrollPane, selectedEmployees, rows);
                panel.add(rightTableScrollPane, gbc);
                // 添加添加按钮和删除按钮
                gbc.gridx = 0; gbc.gridy = 1;
                JButton selectButton = new JButton("添加");
                panel.add(selectButton, gbc);
                selectButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 处理选择逻辑
                        int[] selectedRows = leftTable.getSelectedRows();
                        DefaultTableModel leftTableModel = (DefaultTableModel) leftTable.getModel();
                        // 将选中的行数据添加到右侧表
                        for (int selectedRow : selectedRows) {
                            // 获取左侧表中选中的行数据
                            Employee employee = new Employee();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            employee.setEMPNO((String)leftTable.getValueAt(selectedRow, 0));
                            employee.setFIRSTNME((String)leftTable.getValueAt(selectedRow, 1));
                            employee.setMIDINIT((String)leftTable.getValueAt(selectedRow, 2));
                            employee.setLASTNAME((String)leftTable.getValueAt(selectedRow, 3));
                            employee.setWORKDEPT((String)leftTable.getValueAt(selectedRow, 4));
                            employee.setPHONENO((String)leftTable.getValueAt(selectedRow, 5));
                            employee.setHIREDATE(dateFormat.format(leftTable.getValueAt(selectedRow, 6)));
                            employee.setJOB((String)leftTable.getValueAt(selectedRow, 7));
                            employee.setEDLEVEL(String.valueOf(leftTable.getValueAt(selectedRow, 8)));
                            employee.setSEX((String)leftTable.getValueAt(selectedRow, 9));
                            employee.setBIRTHDATE(dateFormat.format(leftTable.getValueAt(selectedRow, 10)));
                            employee.setSALARY(leftTable.getValueAt(selectedRow, 11).toString());
                            employee.setBONUS(leftTable.getValueAt(selectedRow, 12).toString());
                            employee.setCOMM(leftTable.getValueAt(selectedRow, 13).toString());
                            selectedEmployees.add(employee);
                            rows[count[0]++] = 1;
                            // 删去右侧表中的这一行
                            leftTableModel.removeRow(selectedRow);
                        }
                        // 更新表格显示
                        showTable(rightTableScrollPane, selectedEmployees, rows);
                    }
                });
                gbc.gridx = 1; gbc.gridy = 1;
                JButton deleteButton = new JButton("删除");
                panel.add(deleteButton, gbc);
                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 处理选择逻辑
                        int[] selectedRows = rightTable.getSelectedRows();
                        DefaultTableModel leftTableModel = (DefaultTableModel) leftTable.getModel();
                        // 将选中的行数据添加到右侧表
                        for (int selectedRow : selectedRows) {
                            // 删去左侧表中选中的行数据
                            Employee employee = null;
                            String empNoToRemove = (String) leftTable.getValueAt(selectedRow, 0);
                            Iterator<Employee> iterator = selectedEmployees.iterator();
                            while (iterator.hasNext()) {
                                employee = iterator.next();
                                if (empNoToRemove.equals(employee.getEMPNO())) {
                                    iterator.remove();
                                    break;
                                }
                            }
                            if (count[0] > 0) {
                                rows[--count[0]] = 0;
                            }
                            // 在右侧表添加左侧表中的删去的行数据
                            leftTableModel.addRow(new Object[]{employee.getEMPNO(), employee.getFIRSTNME(), employee.getMIDINIT(), employee.getLASTNAME(),
                                    employee.getWORKDEPT(), employee.getPHONENO(), employee.getHIREDATE(), employee.getJOB(), employee.getEDLEVEL(),
                                    employee.getSEX(), employee.getBIRTHDATE(), employee.getSALARY(), employee.getBONUS(), employee.getCOMM()});
                            // 更新表格显示
                            showTable(rightTableScrollPane, selectedEmployees, rows);
                        }
                    }
                });

                /// 弹出窗口，处理用户点击确认的情况
                int result = JOptionPane.showConfirmDialog(null, panel, "子查询插入", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    database.subqueryInsert(selectedEmployees);
                    updateGUI();
                }
            }
        });
    }
    // 创建GUI界面
    public void createGUI() {
        /* 创建JFrame */
        frame = new JFrame("员工");
        frame.setSize(800, 600);
        GridBagConstraints gbc = new GridBagConstraints();

        /* 创建左侧JPanel，用于显示表格信息 */
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(600, 580));
        leftPanel.setLayout(new GridBagLayout());
        frame.add(leftPanel, BorderLayout.WEST);
        // 显示表格名
        JLabel tableName = new JLabel("员工信息表");
        gbc.insets = new Insets(0, 0, 10, 500);
        gbc.gridx = 0;
        gbc.gridy = 0;
        leftPanel.add(tableName, gbc);
        // 显示表格内容
        Database database = Database.getInstance();
        ResultSet rs = database.searchTable("DB2ADMIN.TEMPL", null);
        JTable table = createTable();
        scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        int count = showTable(scrollPane, rs);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 50, 10);
        leftPanel.add(scrollPane, gbc);

        /* 创建右侧JPanel，用于进行操作 */
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(200, 580));
        rightPanel.setLayout(new GridBagLayout());
        frame.add(rightPanel, BorderLayout.EAST);
        // 显示表格信息数
        employeeNum = new JLabel("当前员工数：   " + count);
        gbc.insets = new Insets(0, 0, 30, 50);
        gbc.gridy = 0;
        rightPanel.add(employeeNum, gbc);
        // 选择操作按钮
        gbc.insets = new Insets(0, 0, 20, 50);
        gbc.gridy = 1;
        rightPanel.add(singleRowInsertButton, gbc);
        gbc.gridy = 2;
        rightPanel.add(multipleRowsInsertButton, gbc);
        gbc.insets = new Insets(0, 0, 300, 50);
        gbc.gridy = 3;
        rightPanel.add(subqueryInsertButton, gbc);

        /* 窗口居中显示 */
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    // 更新GUI界面
    private void updateGUI() {
        Database database = Database.getInstance();
        ResultSet rs = database.searchTable("DB2ADMIN.TEMPL", null);
        employeeNum.setText("当前员工数：   " + showTable(scrollPane, rs));
    }
    // 添加输入框
    private JTextField addInputText(JPanel panel, GridBagConstraints gbc, String labelText, String inputText) {
        // 文本
        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(JLabel.RIGHT);
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(label, gbc);
        // 输入
        JTextField jTextField = new JTextField(inputText);
        jTextField.setHorizontalAlignment(JTextField.CENTER);
        jTextField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        jTextField.setPreferredSize(new Dimension(75, 30));
        ++gbc.gridx;
        gbc.insets = new Insets(0, 0, 10, 30);
        panel.add(jTextField, gbc);
        ++gbc.gridx;

        return jTextField;
    }
    // 添加下拉框
    private JComboBox addInputBox(JPanel panel, GridBagConstraints gbc, String labelText, String[] selectText) {
        // 文本
        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(JLabel.RIGHT);
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(label, gbc);
        // 输入
        JComboBox box = new JComboBox(selectText);
        box.setPreferredSize(new Dimension(75, 30));
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        box.setRenderer(renderer);
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        ++gbc.gridx;
        gbc.insets = new Insets(0, 0, 10, 30);
        panel.add(box, gbc);
        ++gbc.gridx;

        return box;
    }
    // 创建表格
    private JTable createTable() {
        /// 初始化表格模型
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("工号");
        tableModel.addColumn("名字");
        tableModel.addColumn("中间名");
        tableModel.addColumn("姓氏");
        tableModel.addColumn("部门");
        tableModel.addColumn("联系方式");
        tableModel.addColumn("入职日期");
        tableModel.addColumn("职务");
        tableModel.addColumn("等级");
        tableModel.addColumn("性别");
        tableModel.addColumn("出生日期");
        tableModel.addColumn("薪资");
        tableModel.addColumn("奖金");
        tableModel.addColumn("佣金");
        /// 创建表格
        final JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // 设置最小列宽
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setMinWidth(20);
        }
        // 表头文字居中
        JTableHeader header = table.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        // 每列文字居中
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        // 添加复制粘贴功能
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                /// 复制功能
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
                    // 获取当前选中的行和列
                    int[] selectedRows = table.getSelectedRows();
                    int[] selectedColumns = table.getSelectedColumns();
                    // 如果没有选中行或列，直接返回
                    if (selectedRows.length == 0 || selectedColumns.length == 0) {
                        return;
                    }
                    // 构建剪贴板内容
                    StringBuilder clipboardContent = new StringBuilder();
                    for (int i = 0; i < selectedRows.length; i++) {
                        for (int j = 0; j < selectedColumns.length; j++) {
                            clipboardContent.append(table.getValueAt(selectedRows[i], selectedColumns[j]));
                            if (j < selectedColumns.length - 1) {
                                clipboardContent.append('\t');
                            }
                        }
                        clipboardContent.append('\n');
                    }
                    // 将内容放入系统剪贴板
                    StringSelection stringSelection = new StringSelection(clipboardContent.toString());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                }
                /// 粘贴功能
                else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    Transferable transferable = clipboard.getContents(this);
                    if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        try {
                            // 获取剪贴板内容
                            String clipboardContent = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                            String[] rows = clipboardContent.split("\n");
                            // 将内容分割后填充到表格中
                            for (int i = 0; i < rows.length; i++) {
                                String[] values = rows[i].split("\t");
                                for (int j = 0; j < values.length; j++) {
                                    table.setValueAt(values[j], table.getSelectedRow() + i, table.getSelectedColumn() + j);
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
        return table;
    }
    // 输出数据
    private int showTable(JScrollPane scrollPane, ResultSet resultSet) {
        JViewport viewport = scrollPane.getViewport();
        JTable table = (JTable)viewport.getView();
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        tableModel.setRowCount(0);
        int count = 0;
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; ++i) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                ++count;
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            Database.getInstance().handleSQLException(e, "showTable(DefaultTableModel tableModel, ResultSet resultSet)");
        }
        tableModel.fireTableDataChanged();
        scrollPane.revalidate();
        scrollPane.repaint();
        return count;
    }
    private void showTable(JScrollPane scrollPane, List<Employee> employees, int[] rows) {
        JViewport viewport = scrollPane.getViewport();
        JTable table = (JTable)viewport.getView();
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        tableModel.setRowCount(0);
        for (int i = 0, j = -1; i < 50; ++i) {
            if (rows[i] == 1) {
                Employee employee = employees.get(++j);
                tableModel.addRow(new Object[]{employee.getEMPNO(), employee.getFIRSTNME(), employee.getMIDINIT(), employee.getLASTNAME(),
                        employee.getWORKDEPT(), employee.getPHONENO(), employee.getHIREDATE(), employee.getJOB(), employee.getEDLEVEL(),
                        employee.getSEX(), employee.getBIRTHDATE(), employee.getSALARY(), employee.getBONUS(), employee.getCOMM()});
            }
            else {
                tableModel.addRow(new Object[]{"", "", "", "", "", "", "", "", "", "", "", "", "", ""});
            }
        }
        tableModel.fireTableDataChanged();
        scrollPane.revalidate();
        scrollPane.repaint();
    }
}

// 数据库类
class Database {
    // 数据库变量
    private static Database database = null;
    private Connection con = null;

    /**
     * 通用部分
     * */
    // 用于加载数据库驱动程序
    static {
        try {
            Class.forName ("COM.ibm.db2.jdbc.app.DB2Driver");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error loading DB2 Driver...\n" + e,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    // 创建数据库
    private Database() {
        try {
            // 与数据库建立连接，并关闭自动提交
            System.out.println("Connect statement follows:");
            this.con = DriverManager.getConnection("jdbc:db2:sample", "db2admin", "db2admin");
            System.out.println("Connect completed");
            con.setAutoCommit(true);
        } catch (SQLException e) {
            Database.getInstance().handleSQLException(e, "Database()");
        }
    }
    // 获取数据库
    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }
    // 异常捕获
    void handleSQLException(SQLException e, String place) {
        int SQLCode = e.getErrorCode();
        String SQLState = e.getSQLState();
        String Message = e.getMessage();
        JOptionPane.showMessageDialog(
                null,
                "SQLCODE: " + SQLCode + "\nSQLSTATE: " + SQLState + "\nSQLERRM: " + Message + "\nplace:" + place,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 功能部分
     * */
    /// 插入
    // 单行插入
    public void singleRowInsert(Employee employee) {
        try{
            // 初始化sql语句
            String sql = "INSERT INTO DB2ADMIN.TEMPL VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            // 完善sql语句
            pstmt.setString(1, employee.getEMPNO());

            pstmt.setString(2, employee.getFIRSTNME());

            if (employee.getMIDINIT().isEmpty()) {
                pstmt.setNull(3, Types.CHAR);
            }
            else {
                pstmt.setString(3, employee.getMIDINIT());
            }

            pstmt.setString(4, employee.getLASTNAME());

            if (employee.getWORKDEPT().isEmpty()) {
                pstmt.setNull(5, Types.CHAR);
            }
            else {
                pstmt.setString(5, employee.getWORKDEPT());
            }

            if (employee.getPHONENO().isEmpty()) {
                pstmt.setNull(6, Types.CHAR);
            }
            else {
                pstmt.setString(6, employee.getPHONENO());
            }

            if (employee.getHIREDATE().isEmpty()) {
                pstmt.setNull(7, Types.DATE);
            }
            else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date tempDate = null;
                try {
                    tempDate = format.parse(employee.getHIREDATE());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                java.sql.Date date = new java.sql.Date(tempDate.getTime());
                pstmt.setDate(7, date);
            }

            if (employee.getJOB().isEmpty()) {
                pstmt.setNull(8, Types.CHAR);
            }
            else {
                pstmt.setString(8, employee.getJOB());
            }

            pstmt.setString(9, employee.getEDLEVEL());

            if (employee.getSEX() == " ") {
                pstmt.setNull(10, Types.CHAR);
            }
            else {
                pstmt.setString(10, employee.getSEX());
            }

            if (employee.getBIRTHDATE().isEmpty()) {
                pstmt.setNull(11, Types.DATE);
            }
            else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date tempDate = null;
                try {
                    tempDate = format.parse(employee.getBIRTHDATE());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                java.sql.Date date = new java.sql.Date(tempDate.getTime());
                pstmt.setDate(11, date);
            }

            if (employee.getSALARY().isEmpty()) {
                pstmt.setNull(12, Types.DECIMAL);
            }
            else {
                pstmt.setDouble(12, Double.parseDouble(employee.getSALARY()));
            }

            if (employee.getBONUS().isEmpty()) {
                pstmt.setNull(13, Types.DECIMAL);
            }
            else {
                pstmt.setDouble(13, Double.parseDouble(employee.getBONUS()));
            }

            if (employee.getCOMM().isEmpty()) {
                pstmt.setNull(14, Types.DECIMAL);
            }
            else {
                pstmt.setDouble(14, Double.parseDouble(employee.getCOMM()));
            }

            // 执行sql语句
            int insertCount = pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "插入员工数：" + insertCount, "Success", JOptionPane.PLAIN_MESSAGE);
            pstmt.close();
        } catch (SQLException e) {
            Database.getInstance().handleSQLException(e, "singleRowInsert(Employee employee)");
        }
    }
    // 多行插入
    void multipleRowsInsert(List<Employee> employees) {
        try {
            // 初始化sql语句
            String sql = "INSERT INTO DB2ADMIN.TEMPL VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            int insertCount = 0;
            // 完善sql语句
            for (Employee employee:employees) {
                pstmt.setString(1, employee.getEMPNO());
                pstmt.setString(2, employee.getFIRSTNME());
                pstmt.setString(3, employee.getMIDINIT());
                pstmt.setString(4, employee.getLASTNAME());
                pstmt.setString(5, employee.getWORKDEPT());
                pstmt.setString(6, employee.getPHONENO());
                if (employee.getHIREDATE().isEmpty()) {
                    pstmt.setDate(7, null);
                }
                else {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date tempDate = null;
                    try {
                        tempDate = format.parse(employee.getHIREDATE());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    java.sql.Date date = new java.sql.Date(tempDate.getTime());
                    pstmt.setDate(7, date);
                }
                pstmt.setString(8, employee.getJOB());
                pstmt.setString(9, employee.getEDLEVEL());
                if (employee.getSEX() == " ") {
                    employee.setSEX("");
                }
                pstmt.setString(10, employee.getSEX());
                if (employee.getBIRTHDATE().isEmpty()) {
                    pstmt.setDate(11, null);
                }
                else {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date tempDate = null;
                    try {
                        tempDate = format.parse(employee.getBIRTHDATE());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    java.sql.Date date = new java.sql.Date(tempDate.getTime());
                    pstmt.setDate(11, date);
                }
                if (employee.getSALARY().isEmpty()) {
                    pstmt.setDouble(12, 0);
                }
                else {
                    pstmt.setDouble(12, Double.parseDouble(employee.getSALARY()));
                }
                if (employee.getBONUS().isEmpty()) {
                    pstmt.setDouble(13, 0);
                }
                else {
                    pstmt.setDouble(13, Double.parseDouble(employee.getBONUS()));
                }
                if (employee.getCOMM().isEmpty()) {
                    pstmt.setDouble(14, 0);
                }
                else {
                    pstmt.setDouble(14, Double.parseDouble(employee.getCOMM()));
                }
                pstmt.executeUpdate();
                ++insertCount;
            }
            // 执行sql语句
            JOptionPane.showMessageDialog(null, "插入员工数：" + insertCount, "Success", JOptionPane.PLAIN_MESSAGE);
            pstmt.close();
        } catch (SQLException e) {
            Database.getInstance().handleSQLException(e, "multipleRowsInsert(List<Employee> employees)");
        }
    }
    // 子查询插入
    public void subqueryInsert(List<Employee> employees) {
        try {
            // 初始化sql语句
            String standardSql = "INSERT INTO DB2ADMIN.TEMPL SELECT * FROM JLU.EMPLOYEE";
            // 执行sql语句
            int insertCount = 0;
            Statement stmt = con.createStatement();
            for (Employee employee : employees) {
                String sql = standardSql + " WHERE " + "EMPNO=" + employee.getEMPNO();
                stmt.executeUpdate(sql);
                ++insertCount;
            }
            JOptionPane.showMessageDialog(null, "插入员工数：" + insertCount, "Success", JOptionPane.PLAIN_MESSAGE);
            stmt.close();
        } catch (SQLException e) {
            Database.getInstance().handleSQLException(e, "subqueryInsert(String condition)");
        }
    }
    /// 查询
    public ResultSet searchTable(String tableName, String condition) {
        ResultSet rs = null;
        try{
            // 初始化sql语句
            String sql = "SELECT * FROM " + tableName;
            if (condition != null) {
                sql = sql +  " WHERE " + condition;
            }
            Statement stmt = con.createStatement();
            // 执行sql语句
            rs = stmt.executeQuery(sql);
        }catch (SQLException e) {
            Database.getInstance().handleSQLException(e, "searchTable(String tableName, String condition)");
        }
        return rs;
    }
}

// 员工类
class Employee {
    /// 员工属性
    private String EMPNO; // 员工工号(character 6 不可空)
    private String FIRSTNME; // 员工名字(verchar 12 不可空)
    private String MIDINIT; // 员工中间名首字母(character 1 可空)
    private String LASTNAME; // 员工姓氏(verchar 15 不可空)
    private String WORKDEPT; // 员工工作部门(character 3 可空)
    private String PHONENO; // 员工联系方式(character 4 可空)
    private String HIREDATE; // 员工入职日期(date 4 可空)
    private String JOB; // 员工工作职务(character 8 可空)
    private String EDLEVEL; // 员工等级(smallint 2 不可空)
    private String SEX; // 员工性别(character 1 可空)
    private String BIRTHDATE; // 员工出生日期(date 4 可空)
    private String SALARY; // 员工薪资(decimal 9 可空)
    private String BONUS; // 员工奖金(decimal 9 可空)
    private String COMM; // 员工佣金(decimal 9 可空)

    /// 方法
    // 创建员工
    public Employee() {
        this.EMPNO = ""; // 员工工号
        this.FIRSTNME = ""; // 员工名字
        this.MIDINIT = ""; // 员工中间名首字母
        this.LASTNAME = ""; // 员工姓氏
        this.WORKDEPT = ""; // 员工工作部门
        this.PHONENO = ""; // 员工联系方式
        this.HIREDATE = ""; // 员工入职时间
        this.JOB = ""; // 员工工作职务
        this.EDLEVEL = ""; // 员工等级
        this.SEX = ""; // 员工性别
        this.BIRTHDATE = ""; // 员工出生日期
        this.SALARY = ""; // 员工薪资
        this.BONUS = ""; // 员工奖金
        this.COMM = ""; // 员工佣金
    }
    // 获取员工信息
    public String getEMPNO() {
        return this.EMPNO;
    }
    public String getFIRSTNME() {
        return this.FIRSTNME;
    }
    public String getMIDINIT() {
        return this.MIDINIT;
    }
    public String getLASTNAME() {
        return this.LASTNAME;
    }
    public String getWORKDEPT() {
        return this.WORKDEPT;
    }
    public String getPHONENO() {
        return this.PHONENO;
    }
    public String getHIREDATE() {
        return this.HIREDATE;
    }
    public String getJOB() {
        return this.JOB;
    }
    public String getEDLEVEL() {
        return this.EDLEVEL;
    }
    public String getSEX() {
        return this.SEX;
    }
    public String getBIRTHDATE() {
        return this.BIRTHDATE;
    }
    public String getSALARY() {
        return this.SALARY;
    }
    public String getBONUS() {
        return this.BONUS;
    }
    public String getCOMM() {
        return this.COMM;
    }
    // 修改员工信息
    public void setEMPNO(String input) {
        this.EMPNO = input;
    }
    public void setFIRSTNME(String input) {
        this.FIRSTNME = input;
    }
    public void setMIDINIT(String input) {
        this.MIDINIT = input;
    }
    public void setLASTNAME(String input) {
        this.LASTNAME = input;
    }
    public void setWORKDEPT(String input) {
        this.WORKDEPT = input;
    }
    public void setPHONENO(String input) {
        this.PHONENO = input;
    }
    public void setHIREDATE(String input) {
        this.HIREDATE = input;
    }
    public void setJOB(String input) {
        this.JOB = input;
    }
    public void setEDLEVEL(String input) {
        this.EDLEVEL = input;
    }
    public void setSEX(String input) {
        this.SEX = input;
    }
    public void setBIRTHDATE(String input) {
        this.BIRTHDATE = input;
    }
    public void setSALARY(String input) {
        this.SALARY = input;
    }
    public void setBONUS(String input) {
        this.BONUS = input;
    }
    public void setCOMM(String input) {
        this.COMM = input;
    }
    // 校验员工信息
    public boolean isEMPNOCurrect() {
        return isDataCorrect(this.EMPNO, "int", 6, false, true, false);
    }
    public boolean isFIRSTNMECurrect() {
        return isDataCorrect(this.FIRSTNME, "name", 12, false, false, true);
    }
    public boolean isMIDINITCurrect() {
        return isDataCorrect(this.MIDINIT, "char", 1, true, true, false);
    }
    public boolean isLASTNAMECurrect() {
        return isDataCorrect(this.LASTNAME, "name", 15, false, false, true);
    }
    public boolean isWORKDEPTCurrect() {
        return isDataCorrect(this.WORKDEPT, "charInt", 3, true, true, false);
    }
    public boolean isPHONENOCurrect() {
        return isDataCorrect(this.PHONENO, "int", 4, true, true, false);
    }
    public boolean isHIREDATECurrect() {
        return isDataCorrect(this.HIREDATE, "date", 10, true, true, false);
    }
    public boolean isJOBCurrect() {
        return isDataCorrect(this.JOB, "char", 8, true, false, false);
    }
    public boolean isEDLEVELCurrect() {
        return isDataCorrect(this.EDLEVEL, "int", 2, false, true, false);
    }
    public boolean isBIRTHDATECurrect() {
        return isDataCorrect(this.BIRTHDATE, "date", 10, true, true, false);
    }
    public boolean isSALARYCurrect() {
        return isDataCorrect(this.SALARY, "double", 9, true, false, false);
    }
    public boolean isBONUSCurrect() {
        return isDataCorrect(this.BONUS, "double", 9, true, false, false);
    }
    public boolean isCOMMCurrect() {
        return isDataCorrect(this.COMM, "double", 9, true, false, false);
    }
    private boolean isDataCorrect(String input, String dataType, int dataLength, boolean isEmpty, boolean isLengthFixed, boolean isLengthExpendable) {
        /// 检测输入是否可为空
        if (isEmpty && input.isEmpty()) {
            return true;
        }
        else if (!isEmpty && input.isEmpty()) {
            return false;
        }

        /// 检测输入是否要求定长
        if (isLengthFixed && input.length() != dataLength) {
            return false;
        }

        /// 检测输入是否过长
        if (!isLengthExpendable && input.length() > dataLength) {
            return false;
        }

        /// 保证输入的数据类型
        // 数据为字符
        if (dataType.equals("char") || dataType.equals("name")) {
            for (int i = 0; i < input.length(); ++i) {
                if (input.charAt(i) >= 'A' && input.charAt(i) <= 'Z') { }
                else if (input.charAt(i) >= 'a' && input.charAt(i) <= 'z') { }
                else if (input.charAt(i) == ' ') { }
                else if (dataType == "name" && input.charAt(i) == '\'') { }
                else {
                    return false;
                }
            }
        }
        // 数据为整形
        else if (dataType.equals("int")) {
            for (int i = 0; i < input.length(); ++i) {
                if (input.charAt(i) >= '0' && input.charAt(i) <= '9') { }
                else {
                    return false;
                }
            }
        }
        // 数据为日期
        else if (dataType.equals("date")) {
            for (int i = 0; i < 10; ++i) {
                if (input.charAt(i) >= '0' && input.charAt(i) <= '9') { }
                else if ((i == 4 || i == 7) && input.charAt(i) == '-') { }
                else {
                    return false;
                }
            }
        }
        // 数据为浮点
        else if (dataType.equals("double")) {
            for (int i = 0; i < input.length(); ++i) {
                if (input.charAt(i) >= '0' && input.charAt(i) <= '9') { }
                else if (i == input.length() - 3 && input.charAt(i) == '.') { }
                else {
                    return false;
                }
            }
        }
        // 数据为员工部门
        else {
            if (input.charAt(0) >= 'A' && input.charAt(0) <= 'Z') { }
            else {
                return false;
            }
            if (input.charAt(1) >= '0' && input.charAt(1) <= '9') { }
            else {
                return false;
            }
            if (input.charAt(2) >= '0' && input.charAt(2) <= '9') { }
            else {
                return false;
            }
        }
        return true;
    }
}

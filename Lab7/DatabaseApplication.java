import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private JButton searchButton = new JButton("条件查询");
    private JLabel employeeNum = null;
    private JLabel searchCondition = null;

    // 初始化控件
    public GUI() {
        // 单行插入按钮初始化
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
                JTextField hireDateField = addInputText(panel, gbc, "入职日期 ", employee.getHIREDATE().toString());
                JTextField jobField = addInputText(panel, gbc, "工作职务 ", employee.getJOB());
                JTextField edLevelField = addInputText(panel, gbc, "等        级 ", employee.getEDLEVEL().toString());
                gbc.gridx = 0; gbc.gridy = 3;
                JComboBox sexComboBox = addInputBox(panel, gbc, "性        别 ", new String[]{" ", "M", "F"});
                JTextField birthDateField = addInputText(panel, gbc, "出生日期 ", employee.getBIRTHDATE().toString());
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
                    hireDateField.setText(employee.getHIREDATE().toString());
                    jobField.setText(employee.getJOB());
                    edLevelField.setText(employee.getEDLEVEL().toString());
                    birthDateField.setText(employee.getBIRTHDATE().toString());
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
                        // 输入合法则插入到数据库表中
                        Database database = Database.getInstance();
                        if (database.singleRowInsert(employee)) {
                            updateGUI(null);
                            break;
                        }
                    }
                    else {
                        break;
                    }
                }
            }
        });
        // 多行插入按钮初始化
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
                        }
                        // 输入合法则插入到数据库表中
                        if (flag == 0) {
                            Database database = Database.getInstance();
                            database.multipleRowsInsert(employees);
                            updateGUI(null);
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
                            employee.setHIREDATE((String)leftTable.getValueAt(selectedRow, 6));
                            employee.setJOB((String)leftTable.getValueAt(selectedRow, 7));
                            employee.setEDLEVEL((String)leftTable.getValueAt(selectedRow, 8));
                            employee.setSEX((String)leftTable.getValueAt(selectedRow, 9));
                            employee.setBIRTHDATE((String) leftTable.getValueAt(selectedRow, 10));
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

                while (true) {
                    /// 弹出窗口，处理用户点击确认的情况
                    int result = JOptionPane.showConfirmDialog(null, panel, "子查询插入", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        if (database.subqueryInsert(selectedEmployees)) {
                            updateGUI(null);
                            break;
                        }
                    }
                    else {
                        break;
                    }
                }
            }
        });
        // 查询按钮
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 输入搜索条件
                System.out.println("查询按钮被点击");
                String searchCondition = JOptionPane.showInputDialog(null, "请输入搜索条件:", "输入搜索条件", JOptionPane.QUESTION_MESSAGE);
                updateGUI(searchCondition);
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
        rightPanel.setPreferredSize(new Dimension(300, 580));
        rightPanel.setLayout(new GridBagLayout());
        frame.add(rightPanel, BorderLayout.EAST);
        // 显示表格信息
        employeeNum = new JLabel("当前员工数：   " + count);
        gbc.insets = new Insets(0, 40, 20, 10);
        gbc.gridy = 0;
        rightPanel.add(employeeNum, gbc);
        searchCondition = new JLabel("当前搜索条件：   ");
        gbc.insets = new Insets(0, 40, 40, 10);
        ++gbc.gridy;
        rightPanel.add(searchCondition, gbc);
        // 选择操作按钮
        gbc.insets = new Insets(0, 40, 20, 10);
        ++gbc.gridy;
        rightPanel.add(singleRowInsertButton, gbc);
        ++gbc.gridy;
        rightPanel.add(multipleRowsInsertButton, gbc);
        ++gbc.gridy;
        rightPanel.add(subqueryInsertButton, gbc);
        ++gbc.gridy;
        rightPanel.add(searchButton, gbc);

        /* 窗口居中显示 */
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    // 更新GUI界面
    private void updateGUI(String condition) {
        Database database = Database.getInstance();
        ResultSet rs = database.searchTable("DB2ADMIN.TEMPL", condition);
        employeeNum.setText("当前员工数：   " + showTable(scrollPane, rs));
        searchCondition.setText("当前搜索条件：   " + condition);
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
                    Object columnValue = resultSet.getObject(i);
                    if (resultSet.wasNull() || columnValue.toString().trim().isEmpty()) {
                        rowData[i - 1] = "空";
                    }
                    else {
                        rowData[i - 1] = columnValue;
                    }
                }
                ++count;
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            Database.getInstance().handleSQLException(e);
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
            Database.getInstance().handleSQLException(e);
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
    void handleSQLException(SQLException e) {
        int SQLCODE = e.getErrorCode();
        String message = null;
        /// 违反约束
        // 主键重复
        if (SQLCODE == -803) {
            message = "工号不可重复";
        }
        // 非空数据为空
        else if (SQLCODE == -407) {
            message = "输入不可为空";
        }
        // 外键约束
        else if (SQLCODE == -530) {
            message = "不满足外键约束";
        }
        /// 类型不匹配
        else if (SQLCODE == -420) {
            message = "类型不匹配";
        }
        /// 溢出
        // 字符串输入过大
        else if (SQLCODE == -302) {
            message = "输入数据过长";
        }
        // 数字输入过大
        else if (SQLCODE == -4220) {
            message = "输入数据过大";
        }
        /// 其他错误
        else {
            JOptionPane.showMessageDialog(null, "SQLCODE:" + SQLCODE + "   SQLSTATE:" + e.getSQLState() + "    SQLERROR: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    void handleNumberFormatException(NumberFormatException e) {
        JOptionPane.showMessageDialog(null,  "类型不匹配", "Error", JOptionPane.ERROR_MESSAGE);
    }
    void handleIllegalArgumentException(IllegalArgumentException e) {
        JOptionPane.showMessageDialog(null,  "日期格式错误", "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 功能部分
     * */
    /// 插入
    // 单行插入
    public boolean singleRowInsert(Employee employee) {
        PreparedStatement pstmt = null;
        try{
            // 初始化sql语句
            String sql = "INSERT INTO DB2ADMIN.TEMPL VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(sql);
            // 完善sql语句
            if (employee.getEMPNO().isEmpty()) {
                pstmt.setNull(1, Types.CHAR);
            }
            else {
                pstmt.setString(1, employee.getEMPNO());
            }
            if (employee.getFIRSTNME().isEmpty()) {
                pstmt.setNull(2, Types.CHAR);
            }
            else {
                pstmt.setString(2, employee.getFIRSTNME());
            }
            if (employee.getMIDINIT().isEmpty()) {
                pstmt.setNull(3, Types.CHAR);
            }
            else {
                pstmt.setString(3, employee.getMIDINIT());
            }
            if (employee.getLASTNAME().isEmpty()) {
                pstmt.setNull(4, Types.CHAR);
            }
            else {
                pstmt.setString(4, employee.getLASTNAME());
            }
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
                pstmt.setDate(7, Date.valueOf(employee.getBIRTHDATE()));
            }
            if (employee.getJOB().isEmpty()) {
                pstmt.setNull(8, Types.CHAR);
            }
            else {
                pstmt.setString(8, employee.getJOB());
            }
            if (employee.getEDLEVEL().isEmpty()) {
                pstmt.setNull(9, Types.SMALLINT);
            }
            else {
                pstmt.setShort(9, Short.parseShort(employee.getEDLEVEL()));
            }
            if (employee.getSEX().equals(" ")) {
                pstmt.setNull(10, Types.CHAR);
            }
            else {
                pstmt.setString(10, employee.getSEX());
            }
            if (employee.getBIRTHDATE().isEmpty()) {
                pstmt.setNull(11, Types.DATE);
            }
            else {
                pstmt.setDate(11, Date.valueOf(employee.getBIRTHDATE()));
            }
            if (employee.getSALARY().isEmpty()) {
                pstmt.setNull(12, Types.DECIMAL);
            }
            else {
                pstmt.setBigDecimal(12, new BigDecimal(employee.getSALARY()));
            }
            if (employee.getBONUS().isEmpty()) {
                pstmt.setNull(13, Types.DECIMAL);
            }
            else {
                pstmt.setBigDecimal(13, new BigDecimal(employee.getBONUS()));
            }
            if (employee.getCOMM().isEmpty()) {
                pstmt.setNull(14, Types.DECIMAL);
            }
            else {
                pstmt.setBigDecimal(14, new BigDecimal(employee.getCOMM()));
            }
            // 执行sql语句
            int insertCount = pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "插入员工数：" + insertCount, "Success", JOptionPane.PLAIN_MESSAGE);
            return true;
        } catch (NumberFormatException e) {
            Database.getInstance().handleNumberFormatException(e);
            return false;
        } catch (IllegalArgumentException e) {
            Database.getInstance().handleIllegalArgumentException(e);
            return false;
        } catch (SQLException e) {
            Database.getInstance().handleSQLException(e);
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                Database.getInstance().handleSQLException(e);
            }
        }
    }
    // 多行插入
    public boolean multipleRowsInsert(List<Employee> employees) {
        PreparedStatement pstmt = null;
        try {
            // 初始化sql语句
            String sql = "INSERT INTO DB2ADMIN.TEMPL VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(sql);
            int insertCount = 0;
            // 完善sql语句
            for (Employee employee:employees) {
                // 完善sql语句
                if (employee.getEMPNO().isEmpty()) {
                    pstmt.setNull(1, Types.CHAR);
                }
                else {
                    pstmt.setString(1, employee.getEMPNO());
                }
                if (employee.getFIRSTNME().isEmpty()) {
                    pstmt.setNull(2, Types.CHAR);
                }
                else {
                    pstmt.setString(2, employee.getFIRSTNME());
                }
                if (employee.getMIDINIT().isEmpty()) {
                    pstmt.setNull(3, Types.CHAR);
                }
                else {
                    pstmt.setString(3, employee.getMIDINIT());
                }
                if (employee.getLASTNAME().isEmpty()) {
                    pstmt.setNull(4, Types.CHAR);
                }
                else {
                    pstmt.setString(4, employee.getLASTNAME());
                }
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
                    pstmt.setDate(7, Date.valueOf(employee.getHIREDATE()));
                }
                if (employee.getJOB().isEmpty()) {
                    pstmt.setNull(8, Types.CHAR);
                }
                else {
                    pstmt.setString(8, employee.getJOB());
                }
                if (employee.getEDLEVEL().isEmpty()) {
                    pstmt.setNull(9, Types.SMALLINT);
                }
                else {
                    pstmt.setShort(9, Short.parseShort(employee.getEDLEVEL()));
                }
                if (employee.getSEX().equals(" ")) {
                    pstmt.setNull(10, Types.CHAR);
                }
                else {
                    pstmt.setString(10, employee.getSEX());
                }
                if (employee.getBIRTHDATE().isEmpty()) {
                    pstmt.setNull(11, Types.DATE);
                }
                else {
                    pstmt.setDate(11, Date.valueOf(employee.getBIRTHDATE()));
                }
                if (employee.getSALARY().isEmpty()) {
                    pstmt.setNull(12, Types.DECIMAL);
                }
                else {
                    pstmt.setBigDecimal(12, new BigDecimal(employee.getSALARY()));
                }
                if (employee.getBONUS().isEmpty()) {
                    pstmt.setNull(13, Types.DECIMAL);
                }
                else {
                    pstmt.setBigDecimal(13, new BigDecimal(employee.getBONUS()));
                }
                if (employee.getCOMM().isEmpty()) {
                    pstmt.setNull(14, Types.DECIMAL);
                }
                else {
                    pstmt.setBigDecimal(14, new BigDecimal(employee.getCOMM()));
                }
                pstmt.executeUpdate();
                ++insertCount;
            }
            // 执行sql语句
            JOptionPane.showMessageDialog(null, "插入员工数：" + insertCount, "Success", JOptionPane.PLAIN_MESSAGE);
            return true;
        } catch (NumberFormatException e) {
            Database.getInstance().handleNumberFormatException(e);
            return false;
        } catch (IllegalArgumentException e) {
            Database.getInstance().handleIllegalArgumentException(e);
            return false;
        } catch (SQLException e) {
            Database.getInstance().handleSQLException(e);
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                Database.getInstance().handleSQLException(e);
            }
        }
    }
    // 子查询插入
    public boolean subqueryInsert(List<Employee> employees) {
        Statement stmt = null;
        try {
            // 初始化sql语句
            String standardSql = "INSERT INTO DB2ADMIN.TEMPL SELECT * FROM JLU.EMPLOYEE";
            // 执行sql语句
            int insertCount = 0;
            stmt = con.createStatement();
            for (Employee employee : employees) {
                String sql = standardSql + " WHERE " + "EMPNO=" + employee.getEMPNO();
                stmt.executeUpdate(sql);
                ++insertCount;
            }
            JOptionPane.showMessageDialog(null, "插入员工数：" + insertCount, "Success", JOptionPane.PLAIN_MESSAGE);
            return true;
        } catch (SQLException e) {
            Database.getInstance().handleSQLException(e);
            return false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                Database.getInstance().handleSQLException(e);
            }
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
            Database.getInstance().handleSQLException(e);
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
}

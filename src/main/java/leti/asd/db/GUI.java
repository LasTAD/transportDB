package leti.asd.db;

import leti.asd.db.db_list.DBRecordField;
import leti.asd.db.db_list.DBrecord;
import leti.asd.db.db_list.ListDB;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;


/**
 * Created by nikolaikobyzev on 14.10.16.
 */

public class GUI extends JFrame {
    private static final Dimension WINDOW_SIZE = new Dimension(800,600);

    private JPanel root;
    private JScrollPane tableScrollPane;
    private JTable table;
    private JButton buttonAdd, buttonRemove, buttonEdit, buttonSearch, buttonAddFile, buttonSearchNext;
    private GroupLayout layout;
    private ListDB listDB;

    private DBRecordField DBRecordFieldSearch;
    private Object valueSearch;
    private int indexSearch;


    GUI(){
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("TransportDB");
        setMinimumSize(WINDOW_SIZE);
        setLocationRelativeTo(null);
        setTitle("transportDB");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                save();
                System.exit(0);
            }
        });
        initListDB();
        initComponents();
    }

    private void initListDB() {
        listDB = new ListDB();
        load();
    }

    private void initComponents() {
        root = new JPanel();
        layout = new GroupLayout(root);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        root.setLayout(layout);
        add(root);

        table = new JTable();
        table.setModel(new TableModel(listDB));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getSelectionModel().addListSelectionListener(e -> {
            boolean act = table.getSelectedRow() != -1;
            buttonEdit.setEnabled(act);
            buttonRemove.setEnabled(act);
        });
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                switch (col) {
                    case 0:
                        listDB.sort((o1, o2) -> o1.getFullName().compareTo(o2.getFullName()));
                        ((AbstractTableModel) table.getModel()).fireTableDataChanged();
                        break;
                    case 1:
                        listDB.sort((o1, o2) -> Integer.compare(o1.getLevel(), o2.getLevel()));
                        ((AbstractTableModel) table.getModel()).fireTableDataChanged();
                        break;
                    case 2:
                        listDB.sort((o1, o2) -> Integer.compare(o1.getYears_work(), o2.getYears_work()));
                        ((AbstractTableModel) table.getModel()).fireTableDataChanged();
                        break;
                    case 3:
                        listDB.sort((o1, o2) -> Integer.compare(o1.getSalary(), o2.getSalary()));
                        ((AbstractTableModel) table.getModel()).fireTableDataChanged();
                        break;
                }
            }
        });
        tableScrollPane = new JScrollPane(table);

       buttonAdd = new JButton("Добавить");
        buttonAdd.addActionListener(e -> addClick());
        buttonEdit = new JButton("Изменить");
        buttonEdit.addActionListener(e -> editClick());
        buttonEdit.setEnabled(false);
        buttonRemove = new JButton("Удалить");
        buttonRemove.addActionListener(e -> removeClick());
        buttonRemove.setEnabled(false);
        buttonSearch = new JButton("Поиск");
        buttonSearch.addActionListener(e -> searchClick());
        buttonAddFile = new JButton("Добавить из файла");
        buttonAddFile.addActionListener(e -> addFileClick());
        buttonSearchNext = new JButton("Искать далее");
        buttonSearchNext.setEnabled(false);
        buttonSearchNext.addActionListener(e -> searchNextClick());

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                        .addComponent(tableScrollPane)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(buttonAddFile, 100, -1, -1)
                                .addComponent(buttonAdd, 100, -1, -1)
                                .addComponent(buttonEdit, 100, -1, -1)
                                .addComponent(buttonRemove, 100, -1, -1)
                                .addComponent(buttonSearch, 100, -1, -1)
                                .addComponent(buttonSearchNext, 100, -1, -1))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(tableScrollPane)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(buttonAddFile)
                                .addComponent(buttonAdd)
                                .addComponent(buttonEdit)
                                .addComponent(buttonRemove)
                                .addComponent(buttonSearch)
                                .addComponent(buttonSearchNext))
        );
    }

    private void searchNextClick() {
        indexSearch = listDB.searchRec(valueSearch, DBRecordFieldSearch);
        if (indexSearch == -1) {
            JOptionPane.showMessageDialog(this, "Поиск завершен!", "Результаты поиска", JOptionPane.INFORMATION_MESSAGE);
            buttonSearchNext.setEnabled(false);
        } else {
            table.setRowSelectionInterval(indexSearch, indexSearch);
        }
    }

    private void searchClick() {
        JPanel panel = new JPanel();
        GroupLayout paneLayout = new GroupLayout(panel);
        panel.setLayout(paneLayout);
        paneLayout.setAutoCreateGaps(true);
        paneLayout.setAutoCreateContainerGaps(true);

        JLabel warning1 = new JLabel("<html>Перед поиском необходимо<br>отсортировать соответствующий столбец</html>");
        JComboBox comboBox1 = new JComboBox(new Object[] { "ФИО", "Разряд", "Стаж", "Зарплата"});
        JTextField textField1 = new JTextField();

        paneLayout.setHorizontalGroup(
                paneLayout.createParallelGroup()
                        .addComponent(comboBox1)
                        .addComponent(textField1)
                        .addComponent(warning1)
        );

        paneLayout.setVerticalGroup(
                paneLayout.createSequentialGroup()
                        .addComponent(comboBox1)
                        .addComponent(textField1)
                        .addComponent(warning1)
        );

        while (true) {
            int result = JOptionPane.showOptionDialog(
                    this, panel, "Поиск работника", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    new Object[]{"Искать", "Отмена"}, "Искать");

            if (result == 0) {
                DBRecordFieldSearch = DBRecordField.FULLNAME;
                switch (comboBox1.getSelectedIndex()) {
                    case 0: DBRecordFieldSearch = DBRecordField.FULLNAME; break;
                    case 1: DBRecordFieldSearch = DBRecordField.LEVEL; break;
                    case 2: DBRecordFieldSearch = DBRecordField.YEARS_WORK; break;
                    case 3: DBRecordFieldSearch = DBRecordField.SALARY; break;
                }

                //valueSearch = DBRecordFieldSearch; // == DBRecordField.LEVEL ? Integer.parseInt(textField1.getText()) : textField1.getText();
                if(DBRecordFieldSearch == DBRecordField.LEVEL || DBRecordFieldSearch == DBRecordField.YEARS_WORK || DBRecordFieldSearch == DBRecordField.SALARY)
                    valueSearch = Integer.parseInt(textField1.getText());
                else valueSearch = textField1.getText();
                try {
                    indexSearch = listDB.searchRec(valueSearch, DBRecordFieldSearch);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Неверный ввод числа!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (indexSearch == -1) {
                    JOptionPane.showMessageDialog(this, "Ничего не найдено!", "Результаты поиска", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    buttonSearchNext.setEnabled(true);
                    table.setRowSelectionInterval(indexSearch, indexSearch);
                }
            }
            break;
        }
    }


    private void removeClick() {
        DBrecord selectedRecord = listDB.get(table.getSelectedRow());
        int result = JOptionPane.showOptionDialog(this, "Вы действительно хотите удалить \"" + selectedRecord.getFullName() + "\" из списка?", "Удаление работника",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Да", "Нет"}, "Нет");
        if (result == 0) {
            listDB.remove(selectedRecord);
        }
        ((AbstractTableModel) table.getModel()).fireTableDataChanged();
    }

    private void editClick() {
        JPanel panel = new JPanel();
        GroupLayout paneLayout = new GroupLayout(panel);
        panel.setLayout(paneLayout);
        paneLayout.setAutoCreateGaps(true);
        paneLayout.setAutoCreateContainerGaps(true);

        DBrecord selectedRecord = listDB.get(table.getSelectedRow());

        JLabel label1 = new JLabel("ФИО:");
        JLabel label2 = new JLabel("Разряд:");
        JLabel label3 = new JLabel("Стаж:");
        JLabel label4 = new JLabel("Зарплата:");
        JTextField textField1 = new JTextField(selectedRecord.getFullName());
        JTextField textField2 = new JTextField(String.valueOf(selectedRecord.getLevel()));
        JTextField textField3 = new JTextField(String.valueOf(selectedRecord.getYears_work()));
        JTextField textField4 = new JTextField(String.valueOf(selectedRecord.getSalary()));

        paneLayout.setHorizontalGroup(
                paneLayout.createSequentialGroup()
                        .addGroup(
                                paneLayout.createParallelGroup()
                                        .addComponent(label1)
                                        .addComponent(label2)
                                        .addComponent(label3)
                                        .addComponent(label4)
                        )
                        .addGroup(
                                paneLayout.createParallelGroup()
                                        .addComponent(textField1)
                                        .addComponent(textField2)
                                        .addComponent(textField3)
                                        .addComponent(textField4)
                        )
        );

        paneLayout.setVerticalGroup(
                paneLayout.createSequentialGroup()
                        .addGroup(
                                paneLayout.createParallelGroup()
                                        .addComponent(label1)
                                        .addComponent(textField1)
                        )
                        .addGroup(
                                paneLayout.createParallelGroup()
                                        .addComponent(label2)
                                        .addComponent(textField2)
                        )
                        .addGroup(
                                paneLayout.createParallelGroup()
                                        .addComponent(label3)
                                        .addComponent(textField3)
                        )
                        .addGroup(
                                paneLayout.createParallelGroup()
                                        .addComponent(label4)
                                        .addComponent(textField4)
                        )
        );

        while (true) {
            int result = JOptionPane.showOptionDialog(
                    this, panel, "Изменить данные о работнике", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    new Object[]{"Изменить", "Отмена"}, "Изменить");

            if (result == 0) {
                int level, years_work, salary;
                try {
                    level = Integer.parseInt(textField2.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Неверный ввод поля \"Разряд\"!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                try{
                    years_work = Integer.parseInt(textField3.getText());
                }
                catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(this, "Неверный ввод поля \"Стаж\"!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                try{
                    salary = Integer.parseInt(textField4.getText());
                }
                catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(this, "Неверный ввод поля \"Зарплата\"!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                selectedRecord.setFullName(textField1.getText());
                selectedRecord.setLevel(level);
                selectedRecord.setYears_work(years_work);
                selectedRecord.setSalary(salary);
                ((AbstractTableModel) table.getModel()).fireTableDataChanged();
            }
            break;
        }

    }

    private void addClick() {
        JPanel panel = new JPanel();
        GroupLayout paneLayout = new GroupLayout(panel);
        panel.setLayout(paneLayout);
        paneLayout.setAutoCreateGaps(true);
        paneLayout.setAutoCreateContainerGaps(true);

        JLabel label1 = new JLabel("ФИО: ");
        JLabel label2 = new JLabel("Разряд: ");
        JLabel label3 = new JLabel("Срок работы: ");
        JLabel label4 = new JLabel("Зарплата:");
        JTextField textField1 = new JTextField();
        JTextField textField2 = new JTextField();
        JTextField textField3 = new JTextField();
        JTextField textField4 = new JTextField();

        paneLayout.setHorizontalGroup(
                paneLayout.createSequentialGroup()
                        .addGroup(
                                paneLayout.createParallelGroup()
                                        .addComponent(label1)
                                        .addComponent(label2)
                                        .addComponent(label3)
                                        .addComponent(label4)
                        )
                        .addGroup(
                                paneLayout.createParallelGroup()
                                        .addComponent(textField1)
                                        .addComponent(textField2)
                                        .addComponent(textField3)
                                        .addComponent(textField4)
                        )
        );

        paneLayout.setVerticalGroup(
                paneLayout.createSequentialGroup()
                        .addGroup(
                                paneLayout.createParallelGroup()
                                        .addComponent(label1)
                                        .addComponent(textField1)
                        )
                        .addGroup(
                                paneLayout.createParallelGroup()
                                        .addComponent(label2)
                                        .addComponent(textField2)
                        )
                        .addGroup(
                                paneLayout.createParallelGroup()
                                        .addComponent(label3)
                                        .addComponent(textField3)
                        )
                        .addGroup(
                                paneLayout.createParallelGroup()
                                        .addComponent(label4)
                                        .addComponent(textField4)
                        )
        );

        while (true) {
            int result = JOptionPane.showOptionDialog(
                    this, panel, "Добавить работника", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    new Object[]{"Добавить", "Отмена"}, "Добавить");

            if (result == 0) {
                int level, years_work, salary;
                try {
                    level = Integer.parseInt(textField2.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Неверный ввод поля \"Разряд\"!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                try{
                    years_work = Integer.parseInt(textField3.getText());
                }
                catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(this, "Неверный ввод поля \"Стаж\"!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                try{
                    salary = Integer.parseInt(textField4.getText());
                }
                catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(this, "Неверный ввод поля \"Зарплата\"!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                DBrecord rec = new DBrecord(textField1.getText(), level, years_work, salary);
                listDB.add(rec);
                ((AbstractTableModel) table.getModel()).fireTableDataChanged();
            }
            break;
        }
    }

    private void save() {
        try{
            FileController.saveToFile(listDB, "transportDB");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,"Не удалось сохранить изменения в файл!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void load() {
        try {
            FileController.loadFromFile(listDB, "/Users/nikolaikobyzev/Documents/Для учебы/Курсовые проекты/АиСД/transportDB/transportDB");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,"Не удалось загрузить данные из файла!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void addFileClick() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Добавить из файла");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы", "txt"));
        int returnVal = fileChooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            ListDB tempListDB = new ListDB();
            try {
                FileController.loadFromTextFile(tempListDB, fileChooser.getSelectedFile().getPath());
                JOptionPane.showMessageDialog(this, "Добавлено записей: " + tempListDB.size(), "Результаты", JOptionPane.INFORMATION_MESSAGE);
                listDB.addAll(tempListDB);
                ((AbstractTableModel) table.getModel()).fireTableDataChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

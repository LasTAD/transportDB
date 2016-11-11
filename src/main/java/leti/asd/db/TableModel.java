package leti.asd.db;

import leti.asd.db.db_list.ListDB;

import javax.swing.table.AbstractTableModel;

/**
 * Created by nikolaikobyzev on 07.11.16.
 */
public class TableModel extends AbstractTableModel {

    private static final String[] columns = { "ФИО", "Разряд", "Стаж", "Зарплата" };

    private ListDB listDB;

    public TableModel(ListDB listDB) {
        this.listDB = listDB;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    public int getRowCount() {
        return listDB.size();
    }

    public int getColumnCount() {
        return columns.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: return listDB.get(rowIndex).getFullName();
            case 1: return listDB.get(rowIndex).getLevel();
            case 2: return listDB.get(rowIndex).getYears_work();
            case 3: return listDB.get(rowIndex).getSalary();
            default:return null;
        }
    }


}

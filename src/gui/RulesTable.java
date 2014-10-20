package gui;

import java.awt.Component;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class RulesTable extends JTable {
	final static String[] OPERATIONS = { "do_nothing", "star", "random_number", "replace_from_seeds_file", "random_rc",
			"random_phone_number", "replace_with_random_digits", "replace_with_random_characters", "IBAN" };
	final static String[] COLUMNS = { "Name", "Type", "Length", "Index", "Operation", "Parameters" };
	final static int ROW_HEIGHT = 20;
	DefaultTableModel tableModel = new DefaultTableModel(new Object[0][COLUMNS.length], COLUMNS);
	TableColumnModel columnModel;

	public RulesTable() {
		this.setModel(tableModel);
		columnModel = getColumnModel();
		getTableHeader().setReorderingAllowed(false); // vypne dragovani sloupcu

	}

	public void finishInit() {
		columnModel.getColumn(1).setMaxWidth(50);
		columnModel.getColumn(2).setMaxWidth(50);
		columnModel.getColumn(3).setMaxWidth(50);

		JComboBox jcb = new JComboBox(OPERATIONS);
		
		DefaultCellEditor dce = new DefaultCellEditor(jcb);
		columnModel.getColumn(4).setCellEditor(dce);
		this.setRowHeight(ROW_HEIGHT);
	}

	private void addRow(int i) {
		int offset = 1;
		if (tableModel.getRowCount() > 0) {
			try {
				offset = Integer.parseInt(tableModel.getValueAt(tableModel.getRowCount() - 1, 2).toString())
						+ Integer.parseInt(tableModel.getValueAt(tableModel.getRowCount() - 1, 3).toString());
			} catch (Exception e) {
			}
		}
		tableModel.insertRow(i, new String[] { "", "text", "1", offset + "", "do_nothing"});
	}

	public void addRow() {
		addRow(tableModel.getRowCount());
	}

	public void removeRow(int i) {
		tableModel.removeRow(i);
	}

	public boolean moveRowBy(int row, int by) { // vraci uspech
		if (row < 0 || row >= tableModel.getRowCount()) {
			return false;
		}
		if (row + by < 0 || row + by >= tableModel.getRowCount()) {
			return false;
		}
		tableModel.moveRow(row, row, row + by);
		return true;
	}

	public Vector<Vector<Object>> getData() {
		return tableModel.getDataVector();
	}

	public void setData(Vector<Vector<Object>> to) {
		Vector<String> columnNames = new Vector<String>(Arrays.asList(COLUMNS));
		tableModel.setDataVector(to, columnNames);
		finishInit();
		// addRow();
	}
}

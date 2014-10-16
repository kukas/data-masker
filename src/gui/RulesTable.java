package gui;

import java.util.Arrays;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class RulesTable extends JTable {
	String[] columns = { "Name", "Type", "Length", "Offset", "Operation", "Parameters" };

	DefaultTableModel tableModel = new DefaultTableModel(new Object[0][columns.length], columns);
	TableColumnModel columnModel;

	public RulesTable() {
		this.setModel(tableModel);
		// this.dataModel = tableModel;
		//tableModel.addRow(new String[] {});
		// DefaultTableColumnModel columnModel = new DefaultTableColumnModel();
		columnModel = getColumnModel();
	}

	public Vector<Vector<Object>> getData() {
		return tableModel.getDataVector();
	}

	public void setData(Vector<Vector<Object>> to){
		Vector<String> columnNames = new Vector<String>(Arrays.asList(columns));
		tableModel.setDataVector(to, columnNames);
	}
}

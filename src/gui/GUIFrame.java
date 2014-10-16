package gui;

import input.DatabaseReader;
import input.FileReader;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import log.Logger;
import masking.Masker;
import output.DatabaseWriter;

public class GUIFrame extends JFrame {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		GUIFrame frame = new GUIFrame();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static final Insets DEFAULT_INSETS = new Insets(5, 5, 5, 5);
	private static final double INPUT_HEIGHT = 0;

	private void placeComponent(Component c, int x, int y, int w, int h, int fill, int anchor, double xWeight,
			double yWeight) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.fill = fill;
		gbc.anchor = anchor;
		gbc.weightx = xWeight;
		gbc.weighty = yWeight;
		gbc.insets = DEFAULT_INSETS;
		this.add(c, gbc);
	}

	private void displayMessage(String message) {
		JOptionPane.showMessageDialog(GUIFrame.this, message);
	}

	public GUIFrame() {
		final RulesTable table = new RulesTable();
		JLabel inputLabel, outputLabel, rulesLabel;
		final JTextField inputField, outputField, rulesField;
		JButton inputButton, outputButton, rulesButton, runButton, saveButton;
		// postranni
		JButton addButton, removeButton, upButton, downButton;

		setSize(800, 600);
		setTitle("Data masking");
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.insets = new Insets(10, 10, 10, 10);

		// input label
		inputLabel = new JLabel("Input file");
		placeComponent(inputLabel, 0, 0, 1, 1, GridBagConstraints.NONE, GridBagConstraints.LINE_END, 0, INPUT_HEIGHT);

		// input field
		inputField = new JTextField("");
		placeComponent(inputField, 1, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.5,
				INPUT_HEIGHT);

		// input button
		inputButton = new JButton("Select file");
		placeComponent(inputButton, 4, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0,
				INPUT_HEIGHT);
		inputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int res = chooser.showOpenDialog(GUIFrame.this);
				if (res == JFileChooser.APPROVE_OPTION) {
					inputField.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		// output label
		outputLabel = new JLabel("Output file");
		placeComponent(outputLabel, 0, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.LINE_END, 0, INPUT_HEIGHT);

		// output field
		outputField = new JTextField("");
		placeComponent(outputField, 1, 1, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 1,
				INPUT_HEIGHT);

		// output button
		outputButton = new JButton("Select file");
		placeComponent(outputButton, 4, 1, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0,
				INPUT_HEIGHT);
		outputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int res = chooser.showOpenDialog(GUIFrame.this);
				if (res == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getAbsolutePath();
					outputField.setText(path);

					FileReader fr = new FileReader(path);

				}
			}
		});

		// rules label
		rulesLabel = new JLabel("Rules");
		placeComponent(rulesLabel, 0, 2, 1, 1, GridBagConstraints.NONE, GridBagConstraints.LINE_END, 0, INPUT_HEIGHT);

		// rules field
		rulesField = new JTextField("");
		placeComponent(rulesField, 1, 2, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.8,
				INPUT_HEIGHT);

		// rules button
		rulesButton = new JButton("Select file");
		placeComponent(rulesButton, 4, 2, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0,
				INPUT_HEIGHT);
		rulesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int res = chooser.showOpenDialog(GUIFrame.this);
				if (res == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getAbsolutePath();
					rulesField.setText(path);
					FileReader fr = new FileReader(path);
					String[] popisySloupcu = fr.read();
				}
				Masker masker = new Masker(rulesField.getText());
				table.setData(masker.getData());
			}
		});
		Masker masker = new Masker();
		table.setData(masker.getData());

		// big table

		JScrollPane tablePane = new JScrollPane(table);

		placeComponent(tablePane, 0, 3, 5, 4, GridBagConstraints.BOTH, GridBagConstraints.LINE_START, 1, 0.8);
		table.finishInit(); // graficke nastaveni tabulky se musi provest az po pridani dat

		// side buttons
		addButton = new JButton("+");
		placeComponent(addButton, 5, 3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_END, 0, 0);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				table.addRow();
			}
		});
		removeButton = new JButton("-");
		placeComponent(removeButton, 5, 4, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_END, 0, 0);
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (table.getSelectedRow() >= 0) {
					table.removeRow(table.getSelectedRow());
				}
			}
		});
		upButton = new JButton("Up");
		placeComponent(upButton, 5, 5, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_END, 0, 0);
		upButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (table.moveRowBy(table.getSelectedRow(), -1)) {
					table.setRowSelectionInterval(table.getSelectedRow() - 1, table.getSelectedRow() - 1);
				}
			}
		});
		downButton = new JButton("Down");
		placeComponent(downButton, 5, 6, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.FIRST_LINE_END, 0, 0);
		downButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (table.moveRowBy(table.getSelectedRow(), 1)) {
					table.setRowSelectionInterval(table.getSelectedRow() + 1, table.getSelectedRow() + 1);
				}}
		});

		// save button
		saveButton = new JButton("Save");
		placeComponent(saveButton, 0, 7, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START, 0, 0.05);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//prikazy pri ulozeni
			}
		});
		
		
		// run button
		runButton = new JButton("Run");
		placeComponent(runButton, 1, 7, 4, 1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START, 0.5, 0.05);
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File inFile = new File(inputField.getText());
				File rulesFile = new File(rulesField.getText());

				if (!inFile.exists()) {
					displayMessage("Input file doesn't exists");
					return;
				}
				if (outputField.getText().equals("")) {
					displayMessage("Output file not set.");
					return;
				}
				if (!rulesFile.exists()) {
					displayMessage("Rules file doesn't exist.");
					return;
				}

				String inputFile = inputField.getText();
				String outputFile = outputField.getText();
				String maskingSettingsFile = rulesField.getText();

				int lines = 100000;
				int header = 3;

				FileReader fReader = new FileReader(inputFile);
				DatabaseReader dReader = new DatabaseReader(fReader.readNLines(header));
				DatabaseWriter writer = new DatabaseWriter(outputFile, dReader.getHeader());
				Masker masker = new Masker(maskingSettingsFile);

				/*
				 * Masker masker = new Masker(); if(!masker.setData(table.getData())){
				 * JOptionPane.showMessageDialog(GUIFrame.this, "Invalid data."); return; }
				 */
				String[] input;
				String[][] database;
				try {
					writer.prepareFile();
				} catch (Exception e) {
					Logger.log(e.getMessage());
				}

				while ((input = fReader.readNLines(lines))[0] != null) {
					Logger.debug("Masking " + input.length + " lines");
					dReader.input = input;
					database = dReader.read();
					database = masker.mask(database);
					try {
						writer.append(database);
					} catch (Exception e) {
						Logger.log(e.getMessage());
					}
				}

				writer.closeFile();
				displayMessage("Done.");
			}
		});

	}
}
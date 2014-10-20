package gui;

import input.DatabaseReader;
import input.FileReader;

import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import log.Logger;
import masking.Masker;
import output.DatabaseWriter;
import output.SettingsWriter;
import exception.MaskingException;

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
	
	private String currentDir = null;

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

	private Vector<Vector<Object>> loadTableDataFromFile(String path) {
		if (!new File(path).exists()) {
			return null;
		}
		FileReader fr = new FileReader(path);
		String[] popisySloupcu = fr.read();
		Vector<Vector<Object>> doTabulky = new Vector<Vector<Object>>(popisySloupcu.length);
		for (int i = 0; i < popisySloupcu.length; i++) {
			doTabulky.add(new Vector<Object>(Arrays.asList(popisySloupcu[i].split(";"))));
		}
		return doTabulky;
	}

	public GUIFrame() {
		final ConfigSaver cfg = new ConfigSaver();
		final RulesTable table = new RulesTable();
		JLabel inputLabel, outputLabel, rulesLabel;
		final JTextField inputField, outputField, rulesField;
		JButton inputButton, outputButton, rulesButton, runButton, saveButton;
		// postranni
		JButton addButton, removeButton, upButton, downButton, helpButton;

		setSize(800, 600);
		setTitle("Data Masking");
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.insets = new Insets(10, 10, 10, 10);

		// Output for logger
		JTextArea logs = new JTextArea("Welcome to Data Masking by psvt");
		JScrollPane scroll = new JScrollPane(logs, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		final Logger logger = new Logger(logs, scroll);
		placeComponent(scroll, 0, 10, 4, 4,  GridBagConstraints.BOTH, GridBagConstraints.LINE_START, 0, 0);
		
		logger.logGUI(
				"                  ,.\n" +
				"                 (\\(\\)\n" +
				" ,_              ;  o >\n" +
				"  {`-.          /  (_) \n" +
				"  `={\\`-._____/`   |\n" +
				"   `-{ /    -=`\\   |\n" +
				"    `={  -= = _/   /\n" +
				"       `\\  .-'   /`\n" +
				"        {`-,__.'===,_\n" +
				"        //`        `\\\n" +
				"       //\n" +
				"      `\\=\n");
		// input label
		inputLabel = new JLabel("Input file");
		placeComponent(inputLabel, 0, 0, 1, 1, GridBagConstraints.NONE, GridBagConstraints.LINE_END, 0, INPUT_HEIGHT);

		// input field
		inputField = new JTextField("");
		placeComponent(inputField, 1, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.5,
				INPUT_HEIGHT);
		inputField.setToolTipText("Insert a path to file containing input data.");
		
		// input button
		inputButton = new JButton("Select file");
		placeComponent(inputButton, 4, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0,
				INPUT_HEIGHT);
		inputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String dir = inputField.getText();
				if (inputField.getText().equals("")) {
					dir = currentDir;
				}
				
				JFileChooser chooser = new JFileChooser(dir);

				int res = chooser.showOpenDialog(GUIFrame.this);
				
				if (res == JFileChooser.APPROVE_OPTION) {
					inputField.setText(chooser.getSelectedFile().getAbsolutePath());
					
					currentDir = chooser.getSelectedFile().getParent();
				}
				cfg.config[0] = inputField.getText();
				cfg.write(cfg.config);
			}
		});

		// output label
		outputLabel = new JLabel("Output file");
		placeComponent(outputLabel, 0, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.LINE_END, 0, INPUT_HEIGHT);

		// output field
		outputField = new JTextField("");
		placeComponent(outputField, 1, 1, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 1,
				INPUT_HEIGHT);
		outputField.setToolTipText("Insert file path for the masked data. If the file does not exist, the program will create it.");

		// output button
		outputButton = new JButton("Select file");
		placeComponent(outputButton, 4, 1, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0,
				INPUT_HEIGHT);
		outputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String dir = outputField.getText();
				if (outputField.getText().equals("")) {
					dir = currentDir;
				}
				
				JFileChooser chooser = new JFileChooser(dir);
				int res = chooser.showOpenDialog(GUIFrame.this);
				
				if (res == JFileChooser.APPROVE_OPTION) {
					outputField.setText(chooser.getSelectedFile().getAbsolutePath());
					
					currentDir = chooser.getSelectedFile().getParent();
				}
				cfg.config[0] = inputField.getText();
				cfg.config[1] = outputField.getText();
				// cfg.config[2] = rulesField.getText();
				cfg.write(cfg.config);
			}

		});

		// rules label
		rulesLabel = new JLabel("Rules");
		placeComponent(rulesLabel, 0, 2, 1, 1, GridBagConstraints.NONE, GridBagConstraints.LINE_END, 0, INPUT_HEIGHT);

		// rules field
		rulesField = new JTextField("");
		placeComponent(rulesField, 1, 2, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.8,
				INPUT_HEIGHT);
		Vector data = (loadTableDataFromFile(cfg.config[2]));
		if (data != null) {
			table.setData(data);
		}
		rulesField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Vector data = (loadTableDataFromFile(rulesField.getText()));
				if (data != null) {
					table.setData(data);
				}
			}

		});
		rulesField.setToolTipText("Insert masking rules file path.");

		// rules button
		rulesButton = new JButton("Select file");
		placeComponent(rulesButton, 4, 2, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0,
				INPUT_HEIGHT);
		rulesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String dir = rulesField.getText();
				if (rulesField.getText().equals("")) {
					dir = currentDir;
				}
				
				JFileChooser chooser = new JFileChooser(dir);
				int res = chooser.showOpenDialog(GUIFrame.this);
				
				if (res == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getAbsolutePath();
					rulesField.setText(path);
					Vector data = (loadTableDataFromFile(path));
					if (data != null) {
						table.setData(data);
					}
				}
				cfg.config[0] = inputField.getText();
				cfg.config[1] = outputField.getText();
				cfg.config[2] = rulesField.getText();
				cfg.write(cfg.config);
			}
		});
		// separator
		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		placeComponent(separator, 0, 3, 6, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 1, 0);
		
		// big table

		JScrollPane tablePane = new JScrollPane(table);

		placeComponent(tablePane, 0, 4, 5, 6, GridBagConstraints.BOTH, GridBagConstraints.LINE_START, 1, 0.8);
		table.finishInit(); // graficke nastaveni tabulky se musi provest az po pridani dat

		// side buttons
		addButton = new JButton("+");
		placeComponent(addButton, 5, 4, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_END, 0, 0);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				table.addRow();
			}
		});
		addButton.setToolTipText("Add a row to the table.");
		
		removeButton = new JButton("-");
		placeComponent(removeButton, 5, 5, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_END, 0, 0);
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (table.getSelectedRow() >= 0) {
					int row = table.getSelectedRow();
					table.removeRow(row);
					if (row < table.getRowCount()) {
						table.setRowSelectionInterval(row, row);
					} else if (table.getRowCount() > 0) {
						table.setRowSelectionInterval(row - 1, row - 1);
					}
				}
			}
		});
		removeButton.setToolTipText("Remove the selected row from the table.");
		
		upButton = new JButton("Move up");
		placeComponent(upButton, 5, 6, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_END, 0, 0);
		upButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (table.moveRowBy(table.getSelectedRow(), -1)) {
					table.setRowSelectionInterval(table.getSelectedRow() - 1, table.getSelectedRow() - 1);
				}
			}
		});
		upButton.setToolTipText("Move the selected row up.");
		
		
		downButton = new JButton("Move down");
		placeComponent(downButton, 5, 7, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.FIRST_LINE_END, 0, 0);
		downButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (table.moveRowBy(table.getSelectedRow(), 1)) {
					table.setRowSelectionInterval(table.getSelectedRow() + 1, table.getSelectedRow() + 1);
				}
			}
		});
		downButton.setToolTipText("Move the selected row down.");
		
		// save button
		saveButton = new JButton("Save Rules");
		placeComponent(saveButton, 5, 8, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START, 0, 0);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// prikazy pri ulozeni
				SettingsWriter writ = new SettingsWriter();
				JFileChooser chooser = new JFileChooser(rulesField.getText());
				int res = chooser.showSaveDialog(GUIFrame.this);
				if (res == JFileChooser.APPROVE_OPTION) {
					String address = chooser.getSelectedFile().getAbsolutePath();
					if (!address.endsWith(".txt")) {
						address += ".txt";

						rulesField.setText(address);

					}

					rulesField.setText(address);
					cfg.config[0] = inputField.getText();
					cfg.config[1] = outputField.getText();
					cfg.config[2] = rulesField.getText();
					cfg.write(cfg.config);

					writ.write(address, table.getData());
				}

			}
		});
		saveButton.setToolTipText("Save masking rules.");
		
		// run button
		runButton = new JButton("Run");
		placeComponent(runButton, 5, 10, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START, 0, 0.3);
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				table.requestFocus();
				File inFile = new File(inputField.getText());
				File rulesFile = new File(rulesField.getText());

				if (!inFile.exists()) {
					displayMessage("Input file doesn't exist.");
					return;
				}
				if (outputField.getText().equals("")) {
					displayMessage("Output file not set.");
					return;
				}
				/*
				 * if (!rulesFile.exists()) { displayMessage("Rules file doesn't exist."); return; }
				 */

				String inputFile = inputField.getText();
				String outputFile = outputField.getText();
				String maskingSettingsFile = rulesField.getText();

				Vector<Vector<Object>> tableDATA = table.getData();
				int[] lengths = new int[tableDATA.size()];
				int[] offsets = new int[tableDATA.size()];

				for (int i = 0; i < tableDATA.size(); i++) {
					try {
						lengths[i] = Integer.parseInt((String) tableDATA.get(i).get(2));
					} catch (NumberFormatException e) {
						displayMessage("Invalid length argument at column " + (i + 1) + ": "
								+ tableDATA.get(i).get(2).toString());
						return;
					}

					try {
						offsets[i] = Integer.parseInt((String) tableDATA.get(i).get(3));
					} catch (NumberFormatException e) {
						displayMessage("Invalid offset argument at column " + (i + 1) + ": "
								+ tableDATA.get(i).get(3).toString());
						return;
					}

					if (lengths[i] < 1) {
						displayMessage("Invalid length argument at column " + (i + 1) + ": "
								+ tableDATA.get(i).get(2).toString());
						return;
					}

					if (offsets[i] < 0) {
						displayMessage("Invalid offset argument at column " + (i + 1) + ": "
								+ tableDATA.get(i).get(3).toString());
						return;
					}
				}
				;

				int lines = 100000;

				FileReader fReader = new FileReader(inputFile);
				DatabaseReader dReader = new DatabaseReader(lengths, offsets);
				DatabaseWriter writer = new DatabaseWriter(outputFile, dReader.getHeader());

				FileReader lineLengthReader = new FileReader(inputFile);
				String[] firstLines = lineLengthReader.readNLines(20);
				int rowLength = 0;
				for (int i = 0; i < firstLines.length; i++) {
					if (firstLines[i].length() > rowLength) {
						rowLength = firstLines[i].length();
					}
				}
				if (rowLength > 0) {
					for (int i = 0; i < lengths.length; i++) {
						if (offsets[i] + lengths[i] > rowLength) {
							displayMessage("Column " + (i + 1) + " is out of range.");
							return;
						}
					}
				} else {
					displayMessage("Input file is empty.");
					return;
				}

				if (table.getRowCount() == 0) {
					displayMessage("No masking rules specified. Masking aborted.");
					return;
				}

				try {
					Masker masker = new Masker(table.getData());
					/*
					 * if(!masker.setData()){ JOptionPane.showMessageDialog(GUIFrame.this, "Invalid data."); return; }
					 */
					/*
					 * Masker masker = new Masker(); if(!masker.setData(table.getData())){
					 * JOptionPane.showMessageDialog(GUIFrame.this, "Invalid data."); return; }
					 */
					String[] input;
					String[][] database;

					try {
						writer.prepareFile();
					} catch (Exception e) {
						e.printStackTrace();
					}
					int linesMasked = 0;
					while ((input = fReader.readNLines(lines))[0] != null) {
						linesMasked += input.length;
						dReader.input = input;
						database = dReader.read();
						database = masker.mask(database);
						try {
							writer.append(database, input);
							// writer.append(input, database);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					writer.closeFile();
					displayMessage("Done.");
					logger.logGUI("Finished: Masked "+linesMasked+" lines");
				} catch (MaskingException e) {
					JOptionPane.showMessageDialog(GUIFrame.this, e.getMessage());
					return;
				}
			}
		});
		runButton.setToolTipText("Deselect lines in the table and click to run the program. See output file for masked data.");
		
		helpButton = new JButton("Help");
		placeComponent(helpButton, 5, 9, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.FIRST_LINE_END, 0, 0);
		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//display help
				 Desktop desktop = Desktop.getDesktop();  
		          try {
		        	  Logger.debug("šít");
		            File f = new File("User documentation.docx");
		             desktop.open(f);
		          }
		          catch(Exception ex) {		
		        	  Logger.debug("šít");
		        	  
		          }
			}
		});
		helpButton.setToolTipText("Help! I'm stuck in a tooltip factory!");
		
		
		inputField.setText(cfg.config[0]);
		outputField.setText(cfg.config[1]);
		rulesField.setText(cfg.config[2]);

	}

}

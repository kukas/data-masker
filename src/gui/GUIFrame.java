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
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import log.Logger;
import masking.Masker;
import output.DatabaseWriter;

public class GUIFrame extends JFrame {
	public static void main(String[] args) {
		GUIFrame frame = new GUIFrame();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static final Insets DEFAULT_INSETS = new Insets(10, 10, 10, 10);
	private static final double INPUT_HEIGHT = 0.1;

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

	public GUIFrame() {
		final RulesTable table = new RulesTable();

		setSize(800, 600);
		setTitle("Data masking");
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.insets = new Insets(10, 10, 10, 10);

		// input label
		JLabel inputLabel = new JLabel("Input file");
		placeComponent(inputLabel, 0, 0, 1, 1, GridBagConstraints.NONE, GridBagConstraints.LINE_END, 0.5, INPUT_HEIGHT);

		// input field
		final JTextField inputField = new JTextField("");
		placeComponent(inputField, 1, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.5,
				INPUT_HEIGHT);

		// input button
		JButton inputButton = new JButton("Select file");
		placeComponent(inputButton, 3, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.5,
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
		JLabel outputLabel = new JLabel("Output file");
		placeComponent(outputLabel, 0, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.LINE_END, 0.5, INPUT_HEIGHT);

		// output field
		final JTextField outputField = new JTextField("");
		placeComponent(outputField, 1, 1, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 1,
				INPUT_HEIGHT);

		// output button
		JButton outputButton = new JButton("Select file");
		placeComponent(outputButton, 3, 1, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.5,
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
		JLabel rulesLabel = new JLabel("Rules");
		placeComponent(rulesLabel, 0, 2, 1, 1, GridBagConstraints.NONE, GridBagConstraints.LINE_END, 0.5, INPUT_HEIGHT);

		// rules field
		final JTextField rulesField = new JTextField("");
		placeComponent(rulesField, 1, 2, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.8,
				INPUT_HEIGHT);

		// rules button
		JButton rulesButton = new JButton("Select file");
		placeComponent(rulesButton, 3, 2, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.5,
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
					Vector<Vector<Object>> doTabulky = new Vector<Vector<Object>>(popisySloupcu.length);
					System.out.println(doTabulky.size());
					for(int i = 0; i < popisySloupcu.length; i++){
						doTabulky.add( new Vector<Object>(Arrays.asList(popisySloupcu[i].split(";"))));
					};
					table.setData(doTabulky);
				}
				Masker masker = new Masker(rulesField.getText());
				//table.setData(masker.getData());
			}
		});

		// big table
		
		JScrollPane tablePane = new JScrollPane(table);
		placeComponent(tablePane, 0, 3, 5, 1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START, 0.5, 0.8);

		// run button
		JButton runButton = new JButton("Run");
		placeComponent(runButton, 0, 5, 5, 1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START, 0.5, 0.1);
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File inFile = new File(inputField.getText());
				File rulesFile = new File(rulesField.getText());

				if (!inFile.exists()) {
					JOptionPane.showMessageDialog(GUIFrame.this, "Input file doesn't exist.");
					return;
				}
				if (outputField.getText().equals("")) {
					JOptionPane.showMessageDialog(GUIFrame.this, "Output file not set.");
					return;
				}
				if (!rulesFile.exists()) {
					JOptionPane.showMessageDialog(GUIFrame.this, "Rules file doesn't exist.");
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

				/*Masker masker = new Masker();
				if(!masker.setData(table.getData())){
					JOptionPane.showMessageDialog(GUIFrame.this, "Invalid data.");
					return;
				}*/
				String[] input;
				String[][] database;
				try {
					writer.prepareFile();
				}
				catch (Exception e) {
					Logger.log(e.getMessage());
				}
				
				while((input = fReader.readNLines(lines))[0] != null){
					Logger.debug("Masking "+input.length+" lines");
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
			}
		});

	}
}
/*
 * JButton button; pane.setLayout(new GridBagLayout()); GridBagConstraints c = new GridBagConstraints(); if (shouldFill)
 * { //natural height, maximum width c.fill = GridBagConstraints.HORIZONTAL; } button = new JButton("Button 1"); if
 * (shouldWeightX) { c.weightx = 0.5; } c.fill = GridBagConstraints.HORIZONTAL; c.gridx = 0; c.gridy = 0;
 * pane.add(button, c); button = new JButton("Button 2"); c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 0.5;
 * c.gridx = 1; c.gridy = 0; pane.add(button, c); button = new JButton("Button 3"); c.fill =
 * GridBagConstraints.HORIZONTAL; c.weightx = 0.5; c.gridx = 2; c.gridy = 0; pane.add(button, c); button = new
 * JButton("Long-Named Button 4"); c.fill = GridBagConstraints.HORIZONTAL; c.ipady = 40; //make this component tall
 * c.weightx = 0.0; c.gridwidth = 3; c.gridx = 0; c.gridy = 1; pane.add(button, c); button = new JButton("5"); c.fill =
 * GridBagConstraints.HORIZONTAL; c.ipady = 0; //reset to default c.weighty = 1.0; //request any extra vertical space
 * c.anchor = GridBagConstraints.PAGE_END; //bottom of space c.insets = new Insets(10,0,0,0); //top padding c.gridx = 1;
 * //aligned with button 2 c.gridwidth = 2; //2 columns wide c.gridy = 2; //third row pane.add(button, c);
 */

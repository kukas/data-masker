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

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
		placeComponent(inputLabel, 0, 0, 1, 1, GridBagConstraints.NONE, GridBagConstraints.LINE_END, 0.5, 0.5);

		// input field
		final JTextField inputField = new JTextField("");
		placeComponent(inputField, 1, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.5, 0.5);

		// input button
		JButton inputButton = new JButton("Select file");
		placeComponent(inputButton, 3, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.5, 0.5);
		inputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int res = chooser.showOpenDialog(GUIFrame.this);
				if(res==JFileChooser.APPROVE_OPTION){
					inputField.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		// output label
		JLabel outputLabel = new JLabel("Output file");
		placeComponent(outputLabel, 0, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.LINE_END, 0.5, 0.5);

		// output field
		final JTextField outputField = new JTextField("");
		placeComponent(outputField, 1, 1, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.5, 0.5);

		// output button
		JButton outputButton = new JButton("Select file");
		placeComponent(outputButton, 3, 1, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.5, 0.5);
		outputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int res = chooser.showOpenDialog(GUIFrame.this);
				if(res==JFileChooser.APPROVE_OPTION){
					outputField.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		
		// rules label
		JLabel rulesLabel = new JLabel("Rules");
		placeComponent(rulesLabel, 0, 2, 1, 1, GridBagConstraints.NONE, GridBagConstraints.LINE_END, 0.5, 0.5);

		// rules field
		final JTextField rulesField = new JTextField("");
		placeComponent(rulesField, 1, 2, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.5, 0.5);

		// rules button
		JButton rulesButton = new JButton("Select file");
		placeComponent(rulesButton, 3, 2, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START, 0.5, 0.5);
		rulesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int res = chooser.showOpenDialog(GUIFrame.this);
				if(res==JFileChooser.APPROVE_OPTION){
					rulesField.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		
		
		// run button
		JButton runButton = new JButton("Run");
		placeComponent(runButton, 0, 5, 5, 1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START, 0.5, 0.2);
		outputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileReader fReader = new FileReader(inputField.getText());
				DatabaseReader dReader = new DatabaseReader(fReader.readNLines(3)); //header
				DatabaseWriter writer = new DatabaseWriter("output/out.txt", dReader.getHeader());
				Masker masker = new Masker("output/maskingsetting.txt");
				String[] input;
				String[][] database;
				while((input = fReader.readNLines(lines))[0] != null){System.out.println("Masking "+input.length+" lines");
					dReader.input = input;
					database = dReader.read();
					database = masker.mask(database);
					try {
						writer.write(database);
					} catch (Exception e) {
						Logger.log(e.getMessage());
					}
				}
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

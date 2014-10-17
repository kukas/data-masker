package log;

import java.awt.Rectangle;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Logger {
	public JTextArea destination;
	public JScrollPane scroll;
	
	public static void log(String in) {
		System.out.println(in);
	}
	
	public void logGUI(String in){
		destination.append("\n"+in);
		scrollToBottom();
	}

	public static void debug(String in) {
		System.out.println("debug: " + in);
	}
	
	public Logger(JTextArea textarea, JScrollPane scroll){
		destination = textarea;
		this.scroll = scroll;
		destination.setEditable(false);
		destination.setLineWrap(true);
	}
	
	public void scrollToBottom(){
		Rectangle visibleRect = destination.getVisibleRect();
		visibleRect.y = destination.getHeight() - visibleRect.height + destination.getFontMetrics(destination.getFont()).getHeight();
		destination.scrollRectToVisible(visibleRect);
	};
}

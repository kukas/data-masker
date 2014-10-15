package log;

public class Logger {
	public static void log(String in) {
		System.out.println(in);
	}

	public static void debug(String in) {
		System.out.println("debug: " + in);
	}
}

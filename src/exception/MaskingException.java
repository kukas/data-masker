package exception;

public class MaskingException extends Exception{
	public MaskingException(){
		super();
	}
	public MaskingException(String message){
		super(message);
	}
	public MaskingException(String message, Throwable cause) {
		super(message, cause); 
		}
	  public MaskingException(Throwable cause) { super(cause); }
}


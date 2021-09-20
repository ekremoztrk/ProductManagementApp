package utilities;

@SuppressWarnings("serial")
public class AlreadyExistsException extends Exception {

	public AlreadyExistsException() {
		super();
	}

	public AlreadyExistsException(String msg) {
		super(msg);
	}

	public String getMessage() {
		return super.getMessage();
	}

}

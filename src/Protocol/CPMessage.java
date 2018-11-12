package Protocol;

public class CPMessage {
	private int code;
	private String message;
	
	public static final CPMessage OpenConnection =
			new CPMessage(100, "Open Connection");
	public static final CPMessage DenyConnection =
			new CPMessage(101, "Deny Connection");
	public static final CPMessage CloseConnection =
			new CPMessage(102,  "Close Connection");
	public static final CPMessage OK =
			new CPMessage(200, "OK");
	public static final CPMessage OpenTextStream =
			new CPMessage(201, "Open Text Stream");
	public static final CPMessage OpenByteStream =
			new CPMessage(202, "Open Binary Stream");
	public static final CPMessage SendingFinished =
			new CPMessage(203, "Sending Finished");
	public static final CPMessage Empty =
			new CPMessage(204, "Empty") ;
	public static final CPMessage AmbiguousArgument =
			new CPMessage(205, "Ambiguous Argument");
	public static final CPMessage BadRequest =
			new CPMessage(400, "Bad Request");
	public static final CPMessage IncompleteRequest =
			new CPMessage(401, "Incomplete Request");
	public static final CPMessage Error =
			new CPMessage(404, "Error");
	
	public CPMessage(int newCode, String newMessage) {
		this.code = newCode;
		this.message = newMessage;
	}
	
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	
	public String toString() {
		return Integer.toString(code) + " " + message;
	}
}

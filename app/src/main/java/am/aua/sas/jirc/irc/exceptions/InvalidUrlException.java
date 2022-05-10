package am.aua.sas.jirc.irc.exceptions;

public class InvalidUrlException extends IllegalArgumentException {
	public InvalidUrlException() {
		super("The supplied URL is invalid.");
	}

	public InvalidUrlException(String message) {
		super(message);
	}
}

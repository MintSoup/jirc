package am.aua.sas.jirc.irc;

public class IRCException extends Exception {

	public IRCException() {
	}

	public IRCException(int errcode){
		this("IRC Exception:" + errcode);
	}

	public IRCException(String message) {
		super(message);
	}
}

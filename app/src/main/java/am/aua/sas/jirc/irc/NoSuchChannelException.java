package am.aua.sas.jirc.irc;

public class NoSuchChannelException extends IRCException {

	public NoSuchChannelException() {
	}

	public NoSuchChannelException(String message) {
		super(message);
	}

}

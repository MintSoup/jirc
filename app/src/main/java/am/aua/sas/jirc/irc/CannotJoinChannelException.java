package am.aua.sas.jirc.irc;

public class CannotJoinChannelException extends IRCException {

	public CannotJoinChannelException() {
	}

	public CannotJoinChannelException(String message) {
		super(message);
	}
}

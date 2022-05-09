package am.aua.sas.jirc.irc;

public class NicknameAlreadyInUseException extends IRCException {
	public NicknameAlreadyInUseException(String msg) {
		super(msg);
	}

	public NicknameAlreadyInUseException() {
	}
}

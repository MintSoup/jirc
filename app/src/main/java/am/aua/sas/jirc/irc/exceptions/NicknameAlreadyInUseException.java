package am.aua.sas.jirc.irc.exceptions;

public class NicknameAlreadyInUseException extends IRCException {
    public NicknameAlreadyInUseException() {
        super();
    }

    public NicknameAlreadyInUseException(String msg) {
        super(msg);
    }
}

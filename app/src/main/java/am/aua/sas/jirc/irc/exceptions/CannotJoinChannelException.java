package am.aua.sas.jirc.irc.exceptions;

public class CannotJoinChannelException extends IRCException {
    public CannotJoinChannelException() {
        super();
    }

    public CannotJoinChannelException(String message) {
        super(message);
    }
}

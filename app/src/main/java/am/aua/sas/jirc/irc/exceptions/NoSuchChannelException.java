package am.aua.sas.jirc.irc.exceptions;

public class NoSuchChannelException extends IRCException {
    public NoSuchChannelException() {
        super();
    }

    public NoSuchChannelException(String message) {
        super(message);
    }
}

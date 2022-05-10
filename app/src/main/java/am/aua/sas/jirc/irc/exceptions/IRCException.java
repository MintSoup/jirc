package am.aua.sas.jirc.irc.exceptions;

public class IRCException extends Exception {
    public IRCException() {
        super();
    }

    public IRCException(int errcode) {
        super("IRC Exception:" + errcode);
    }

    public IRCException(String message) {
        super(message);
    }
}

package am.aua.sas.jirc.irc;

public interface MessageListener {
	boolean handleMessage(String raw, Message m);
}

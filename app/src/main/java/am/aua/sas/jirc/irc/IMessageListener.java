package am.aua.sas.jirc.irc;

public interface IMessageListener {
	boolean handleMessage(String raw, Message m);
}

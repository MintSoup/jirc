package am.aua.sas.jirc.irc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Comparable<Message> {
	private final String sender;
	private final String channel;
	private final String content;
	private final Date timestamp;

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

	public Message(String sender, String channel, String content, Date timestamp) {
		this.sender = sender;
		this.channel = channel;
		this.content = content;
		this.timestamp = timestamp;
	}

	@Override
	public int compareTo(Message m) {
		return timestamp.compareTo(m.timestamp);
	}

	public String getSender() {
		return sender;
	}

	public String getChannel() {
		return channel;
	}

	public String getContent() {
		return content;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return String.format("[%s] <%s> %s", DATE_FORMAT.format(timestamp), sender, content);
	}

}

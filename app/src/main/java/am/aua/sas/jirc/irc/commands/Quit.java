package am.aua.sas.jirc.irc.commands;

public class Quit extends Command {

	private final String reason;

	private static final int[] replies = {};

	public Quit(String channel) {
		this.reason = channel;
	}

	public Quit() {
		this.reason = null;
	}

	@Override
	public String getText() {
		if (reason == null)
			return "QUIT";
		return "QUIT :" + reason;
	}

	@Override
	public int[] getReplies() {
		return replies;
	}

}

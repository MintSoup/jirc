package am.aua.sas.jirc.irc.commands;

public class Pong extends Command {

	private final String message;

	private static final int[] replies = { Command.NumericReplies.ERR_NOORIGIN,
			Command.NumericReplies.ERR_NOSUCHSERVER };

	public Pong(String message) {
		this.message = message;
	}

	@Override
	public String getText() {
		return "PONG " + message;
	}

	@Override
	public int[] getReplies() {
		return replies;
	}

}

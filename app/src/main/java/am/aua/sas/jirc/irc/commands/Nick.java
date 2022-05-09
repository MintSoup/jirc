package am.aua.sas.jirc.irc.commands;

public class Nick extends Command {

	private final String nick;
	private static final int[] replies = {
		Command.NumericReplies.ERR_NONICKNAMEGIVEN,
		Command.NumericReplies.ERR_NICKNAMEINUSE,
		Command.NumericReplies.ERR_UNAVAILRESOURCE,
		Command.NumericReplies.ERR_ERRONEUSNICKNAME,
		Command.NumericReplies.ERR_NICKCOLLISION,
		Command.NumericReplies.ERR_RESTRICTED,
	};

	@Override
	public int[] getReplies() {
		return replies;
	}

	public Nick(String nick) {
		this.cmd = IrcCommand.NICK;
		this.nick = nick;
	}

	@Override
	public String getText() {
		return "NICK " + this.nick;
	}
}

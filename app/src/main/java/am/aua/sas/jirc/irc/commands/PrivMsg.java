package am.aua.sas.jirc.irc.commands;

public class PrivMsg extends Command {

	private final String target;
	private final String content;

	public PrivMsg(String target, String content) {
		this.target = target;
		this.content = content;
	}

	private static final int[] replies = {
		Command.NumericReplies.ERR_NORECIPIENT,
		Command.NumericReplies.ERR_NOTEXTTOSEND,
		Command.NumericReplies.ERR_CANNOTSENDTOCHAN,
		Command.NumericReplies.ERR_NOTOPLEVEL,
		Command.NumericReplies.ERR_WILDTOPLEVEL,
		Command.NumericReplies.ERR_TOOMANYTARGETS,
		Command.NumericReplies.ERR_NOSUCHNICK,
		Command.NumericReplies.RPL_AWAY,
	};

	@Override
	public String getText() {
		return "PRIVMSG " + target + " :" + content;
	}

	@Override
	public int[] getReplies() {
		return replies;
	}

}

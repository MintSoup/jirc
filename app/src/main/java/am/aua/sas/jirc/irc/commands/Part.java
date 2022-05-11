package am.aua.sas.jirc.irc.commands;

public class Part extends Command {

	private final String channel;

	private static final int[] replies = {
		Command.NumericReplies.ERR_NEEDMOREPARAMS,
		Command.NumericReplies.ERR_NOSUCHCHANNEL,
		Command.NumericReplies.ERR_NOTONCHANNEL
	};

	public Part(String channel) {
		this.channel = channel;
	}

	@Override
	public String getText() {
		return "PART " + channel;
	}

	@Override
	public int[] getReplies() {
		return replies;

	}

}

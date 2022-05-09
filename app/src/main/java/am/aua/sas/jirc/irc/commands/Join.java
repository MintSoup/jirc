package am.aua.sas.jirc.irc.commands;

public class Join extends Command {

	private final String channel;

	private static final int[] replies = {
		Command.NumericReplies.ERR_NEEDMOREPARAMS,
		Command.NumericReplies.ERR_INVITEONLYCHAN,
		Command.NumericReplies.ERR_CHANNELISFULL,
		Command.NumericReplies.ERR_NOSUCHCHANNEL,
		Command.NumericReplies.ERR_TOOMANYTARGETS,
		Command.NumericReplies.RPL_TOPIC,
		Command.NumericReplies.ERR_BANNEDFROMCHAN,
		Command.NumericReplies.ERR_BADCHANNELKEY,
		Command.NumericReplies.ERR_BADCHANMASK,
		Command.NumericReplies.ERR_TOOMANYCHANNELS,
		Command.NumericReplies.ERR_UNAVAILRESOURCE,


		Command.NumericReplies.RPL_NAMREPLY,
	};

	public Join(String channel) {
		this.channel = channel;
	}

	@Override
	public String getText() {
		return "JOIN " + channel;
	}

	@Override
	public int[] getReplies() {
		return replies;
	}

}

package am.aua.sas.jirc.irc.commands;

public class User extends Command {

	private final String user;
	private final int mode;
	private final String realname;

	private static final int[] replies = { Command.NumericReplies.ERR_NEEDMOREPARAMS,
			Command.NumericReplies.ERR_ALREADYREGISTRED, };

	@Override
	public int[] getReplies() {
		return replies;
	}

	public User(String user, int mode, String realname) {
		this.user = user;
		this.mode = mode;
		this.realname = realname;
	}

	public User(String user) {
		this(user, 0, user);
	}

	@Override
	public String getText() {
		return "USER " + user + " " + mode + " * :" + realname;
	}
}

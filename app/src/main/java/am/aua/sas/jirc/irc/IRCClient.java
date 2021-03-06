package am.aua.sas.jirc.irc;

import am.aua.sas.jirc.irc.commands.*;
import am.aua.sas.jirc.irc.exceptions.IRCException;
import am.aua.sas.jirc.irc.exceptions.NicknameAlreadyInUseException;
import am.aua.sas.jirc.irc.exceptions.NoSuchChannelException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

public class IRCClient {
    private final String hostname;
    private final int port;
    private String nick;

    private Socket socket;

    private PrintWriter out;
    private BufferedReader in;

    public static final String DEFAULT_SERVER = "irc.libera.chat";
    public static final int DEFAULT_PORT = 6667;
    public static final String DEFAULT_USERNAME = "jirc_user";

    public List<String> channels;

    public IRCClient() {
        this(DEFAULT_SERVER, DEFAULT_PORT, DEFAULT_USERNAME);
    }

    public IRCClient(Server server, String username) {
        this(server.getHostname(), server.getPort(), username);
    }

    public IRCClient(String hostname, int port, String username) {
        this.hostname = hostname;
        this.port = port;
        this.nick = username;
    }

    public void open() throws UnknownHostException, IOException, IRCException {
        socket = new Socket(hostname, port);
        socket.setSoTimeout(0);

        out = new PrintWriter(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        registerUser();
    }

    public void join(String channel) throws IRCException, IOException {
        if (channel.equals("0"))
            throw new IllegalArgumentException("Use .leaveAllChannels() instead.");
        joinCmd(channel);
    }

    private void joinCmd(String channel) throws IOException, IRCException {
        Join j = new Join(channel);
        writeCommand(j);
        int n = waitForReply(j);
        switch (n) {
            case Command.NumericReplies.RPL_TOPIC:
            case Command.NumericReplies.RPL_NAMREPLY:
                break;
            case Command.NumericReplies.ERR_NOSUCHCHANNEL:
                throw new NoSuchChannelException("Channel " + channel + " not found.");
        }
    }

    public void sendMessage(String target, String content) throws IOException {
        PrivMsg cmd = new PrivMsg(target, content);
        writeCommand(cmd);
    }

    public void listenForMessages(MessageListener ml) throws IOException {
        while (true) {
            String line = readLine();
            if (handlePing(line))
                continue;
            Message m = parseMessage(line);
            if (ml.handleMessage(line, m))
                break;

        }
    }

    private boolean handlePing(String line) throws IOException {
        String[] arr = line.split(" ");
        if (!arr[0].equals("PING"))
            return false;

        Pong pong = new Pong(line.substring(line.indexOf(' ') + 1));
        writeCommand(pong);
        return true;
    }

    private Message parseMessage(String line) {
        try {
            String[] headBody = line.split(":");
            String[] head = headBody[1].split(" ");
            if (!head[1].equals("PRIVMSG"))
                return null;
            String user = head[0].substring(0, head[0].indexOf('!'));
            String channel;

            // for DMs the channel gets set to
            // our username. This is not very useful
            // (or logical), so if it's a DM we set the
            // channel to the username of the sender
            if (head[2].startsWith("#"))
                channel = head[2];
            else
                channel = user;

            return new Message(user, channel, headBody[2], new Date());
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {
            return null; // :\
        }
    }

    public void part(String channel) throws IOException {
        Part j = new Part(channel);
        writeCommand(j);
    }

    public void partAll() throws IOException, IRCException {
        joinCmd("0");
    }

    private void registerUser() throws IOException, IRCException {
        User usercmd = new User(this.nick);
        writeCommand(usercmd);
        Nick nickcmd = updateNickname(nick);
        int[] logonSequence = {Command.NumericReplies.RPL_WELCOME, Command.NumericReplies.RPL_YOURHOST,
                Command.NumericReplies.RPL_CREATED, Command.NumericReplies.RPL_MYINFO,};

        int n;
        while ((n = waitForReply(nickcmd.getReplies(), usercmd.getReplies(),
                logonSequence)) != Command.NumericReplies.RPL_MYINFO)
            switch (n) {
                case Command.NumericReplies.RPL_WELCOME:
                case Command.NumericReplies.RPL_YOURHOST:
                case Command.NumericReplies.RPL_CREATED:
                    continue;
                case Command.NumericReplies.ERR_NICKNAMEINUSE:
                    throw new NicknameAlreadyInUseException("Nickname " + nick + " is already in use.");
                default:
                    throw new IRCException(n);
            }
    }

    private void writeCommand(Command c) throws IOException {
        out.println(c.getText());
        out.flush();
        System.out.println("[SEND] " + c.getText());
    }

    private String readLine() throws IOException {
        return in.readLine();
    }

    private Nick updateNickname(String nick) throws IOException, IRCException {
        Nick cmd = new Nick(nick);
        writeCommand(cmd);
        this.nick = nick;
        return cmd;
    }

    private int waitForReply(Command c) throws IOException {
        return waitForReply(c.getReplies());
    }

    private int waitForReply(int[]... replies) throws IOException {
        String line = null;
        while ((line = readLine()) != null) {
            try {
                int n = Integer.parseInt(line.split(" ")[1]);
                for (int[] r : replies)
                    for (int reply : r)
                        if (reply == n)
                            return n;
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return -1;
    }

    public void quit() throws IOException {
        writeCommand(new Quit());
        in.close();
        out.close();
        socket.close();
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getNickname() {
        return nick;
    }

    public static boolean validateChannelName(String channel) {
        if (channel == null || channel.length() <= 1 || channel.length() > 50) {
            return false;
        }

        if (!channel.trim().equals(channel) || channel.contains("\u0007") || channel.contains(",")) {
            return false;
        }

        char prefix = channel.charAt(0);

        return prefix == '&' || prefix == '#' || prefix == '+' || prefix == '!';
    }
}

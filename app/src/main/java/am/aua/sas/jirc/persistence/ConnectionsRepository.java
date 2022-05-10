package am.aua.sas.jirc.persistence;

import am.aua.sas.jirc.irc.IRCClient;
import am.aua.sas.jirc.irc.Server;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ConnectionsRepository implements Repository<Server> {
    public static final String CONNECTIONS = "connections.txt";

    private final ArrayList<Server> servers = new ArrayList<>();

    private final Scanner reader;
    private final PrintWriter writer;

    private static final ConnectionsRepository instance;

    static {
        try {
            instance = new ConnectionsRepository(CONNECTIONS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ConnectionsRepository(String source) throws IOException {
        File sourceFile = new File(source);
        sourceFile.createNewFile();

        this.reader = new Scanner(new FileInputStream(sourceFile));
        this.writer = new PrintWriter(new FileOutputStream(sourceFile, true));

        this.hydrate();
    }

    public static ConnectionsRepository getInstance() {
        return instance;
    }

    @Override
    public Server get(int index) {
        return this.servers.get(index).clone();
    }

    // It makes sense, in theory, for the Server class to be extended and possibly have mutable child classes.
    // Thus, it may be wise to make a manual deep copy of the ArrayList instead of simply calling clone() on it.
    public ArrayList<Server> getAll() {
        ArrayList<Server> deepClone = new ArrayList<>();
        for (Server element : this.servers) {
            deepClone.add(element.clone());
        }

        return deepClone;
    }

    @Override
    public void add(Server element) {
        this.servers.add(element.clone());

        this.writer.println(element);
        this.writer.flush();
    }

    @Override
    public void set(int index, Server element) {
        this.servers.set(index, element.clone());
        // TODO
    }

    @Override
    public void remove(int index) {
        this.servers.remove(index);
        // TODO
    }

    private void hydrate() {
        this.servers.clear();

        this.servers.add(new Server(IRCClient.DEFAULT_SERVER, IRCClient.DEFAULT_PORT));

        while (this.reader.hasNextLine()) {
            String url = this.reader.nextLine();
            Server server = new Server(url);
            this.servers.add(server);
        }
    }
}

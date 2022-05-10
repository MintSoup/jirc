package am.aua.sas.jirc.persistence;

import am.aua.sas.jirc.irc.Server;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ConnectionsRepository {
    public static final String CONNECTIONS = "connections.txt";

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
    }

    public static ConnectionsRepository getInstance() {
        return instance;
    }

    public ArrayList<Server> get() {
        ArrayList<Server> servers = new ArrayList<>();
        while (this.reader.hasNextLine()) {
            String url = this.reader.nextLine();
            Server server = new Server(url);
            servers.add(server);
        }

        return servers;
    }

    public void persist(Server server) {
        this.writer.println(server);
        this.writer.flush();
    }
}

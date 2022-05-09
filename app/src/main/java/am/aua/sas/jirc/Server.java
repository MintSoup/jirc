package am.aua.sas.jirc;

import java.io.Serializable;

public class Server implements Serializable {
    private final String hostname;
    private final String port;

    public Server(String hostname, String port) {
        this.hostname = hostname;
        this.port = port;
    }

    public Server(Server server) {
        this(server.hostname, server.port);
    }

    public String getHostname() {
        return hostname;
    }

    public String getPort() {
        return port;
    }

    @Override
    public String toString() {
        return this.hostname + ":" + this.port;
    }
}

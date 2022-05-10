package am.aua.sas.jirc.irc;

import am.aua.sas.jirc.irc.exceptions.InvalidUrlException;

import java.io.Serializable;

public class Server implements Cloneable, Serializable {
    private final String hostname;
    private final int port;

    public Server(String hostname, int port) {
        this.hostname = getValidatedHostname(hostname);
        this.port = getValidatedPort(port);
    }

    public Server(String url) throws InvalidUrlException {
        String[] urlComponents = url.split(":");
        if (urlComponents.length != 2) {
            throw new InvalidUrlException();
        }

        this.hostname = getValidatedHostname(urlComponents[0]);
        this.port = getValidatedPort(urlComponents[1]);
    }

    public Server(Server server) {
        this(server.hostname, server.port);
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Server s = (Server) o;

        return this.hostname.equals(s.hostname) && this.port == s.port;
    }

    @Override
    public String toString() {
        return this.hostname + ":" + this.port;
    }

    private static String getValidatedHostname(String hostname) {
        if (hostname == null) {
            throw new InvalidUrlException("Hostname cannot be null.");
        }

        return hostname.trim();
    }

    private static int getValidatedPort(String port) {
        int parsedPort;
        try {
            parsedPort = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            throw new InvalidUrlException("Port must be a valid integer.");
        }

        return getValidatedPort(parsedPort);
    }

    private static int getValidatedPort(int port) {
        if (port < 0 || port > 65535) {
            throw new InvalidUrlException("Port is out of range: " + port);
        }

        return port;
    }

    @Override
    public Server clone() {
        try {
            return (Server) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

package am.aua.sas.jirc.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Repository {
    public static final String PREFERENCES = "preferences.csv";
    public static final String CONNECTIONS = "connections.csv";

    private final Scanner reader;
    private final PrintWriter writer;

    private static final Repository instance;

    static {
        try {
            instance = new Repository("");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Repository(String source) throws FileNotFoundException {
        this.reader = new Scanner(new FileInputStream(source));
        this.writer = new PrintWriter(new FileOutputStream(source));
    }

    public static Repository getInstance() {
        return instance;
    }

    public void get(String key) {
        this.reader.next(key);
    }

    public void persist(Object s) {
        this.writer.println(s);
    }
}

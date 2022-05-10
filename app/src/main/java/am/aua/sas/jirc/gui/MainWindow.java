package am.aua.sas.jirc.gui;

import am.aua.sas.jirc.gui.intl.Strings;
import am.aua.sas.jirc.irc.IRCClient;
import am.aua.sas.jirc.irc.exceptions.IRCException;
import am.aua.sas.jirc.irc.Message;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.IOException;
import java.util.Date;

public class MainWindow extends JFrame {
    private static GridBagConstraints gbc = new GridBagConstraints();

    private IRCClient client;
    private Thread listenerThread;

    private JTextPane chat;

    private static final int INT_MESSAGE_COLOR = 0x98c379;
    private static final int SERVER_LINE_COLOR = 0x767676;
    private static final int DATE_COLOR = 0x51afef;
    private static final int MY_NICKNAME_COLOR = 0xc678dd;
    private static final int NICKNAME_COLOR = 0xd19a66;

    public MainWindow(IRCClient client) {
        this.client = client;
        this.setTitle(Strings.APP_NAME);
        // this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(5, 5));

        JMenuBar menu = new JMenuBar();

        JMenu jirc = new JMenu(Strings.FILE_MENU_LABEL);

        JMenuItem about = new JMenuItem(Strings.ABOUT_WINDOW_TITLE);
        about.addActionListener((e) -> new AboutWindow());
        jirc.add(about);

        menu.add(jirc);
        // menu.add(new JMenu("View"));
        this.setJMenuBar(menu);

        String[] buttons = new String[]{"Dummy1", "Dummy2", "Dummy3"};
        JList<String> channels = new JList<>(buttons);
        channels.setBackground(Color.GRAY);
        channels.setPreferredSize(new Dimension(100, 640));
        // channelsPrinter(channels, new String[]{"Dummy1", "Dummy2", "Dummy3"});
        this.add(channels, BorderLayout.WEST);

        JPanel center = new JPanel(new GridBagLayout());
        chat = new JTextPane();
        chat.setEditable(false);
        chat.setPreferredSize(new Dimension(800, 800));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        center.add(chat, gbc);

        JTextField message = new JTextField();
        // add placeholder here
        message.addActionListener((e) -> {
            try {
                client.sendMessage("#test", message.getText());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Message m = new Message(client.getNickname(), "#test", message.getText(), new Date());
            appendMine(m);
            message.setText("");
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        center.add(message, gbc);

        this.add(center, BorderLayout.CENTER);

        listenerThread = new Thread(() -> {
            try {
                client.open();
                client.join("#test");
                appendInternalMessage("Joined #test\n");
                client.listenForMessages((r, m) -> {
                    if (m != null)
                        append(m);
                    else
                        appendServerLine(r + "\n");
                    return false;
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (IRCException e1) {
                e1.printStackTrace();
            }
        }, "Message Receiver");
        listenerThread.start();
    }

    private void append(String message, int color, boolean bold) {
        StyledDocument doc = chat.getStyledDocument();
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setFontSize(keyWord, 16);
        StyleConstants.setBold(keyWord, bold);
        StyleConstants.setForeground(keyWord, new Color(color));
        try {
            doc.insertString(doc.getLength(), message, keyWord);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void append(Message m) {
        append("[" + Message.DATE_FORMAT.format(m.getTimestamp()) + "] ", DATE_COLOR, true);
        append("<" + m.getSender() + "> ", NICKNAME_COLOR, true);
        append(m.getContent() + "\n", 0, false);
    }

    private void appendMine(Message m) {
        append("[" + Message.DATE_FORMAT.format(m.getTimestamp()) + "] ", DATE_COLOR, true);
        append("<" + m.getSender() + "> ", MY_NICKNAME_COLOR, true);
        append(m.getContent() + "\n", 0, false);
    }

    private void appendServerLine(String line) {
        append(line, SERVER_LINE_COLOR, false);
    }

    private void appendInternalMessage(String line) {
        append(line, INT_MESSAGE_COLOR, true);
    }
}

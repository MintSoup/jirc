package am.aua.sas.jirc.gui;

import am.aua.sas.jirc.irc.IRCClient;
import am.aua.sas.jirc.irc.IRCException;
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

    private final IRCClient client;

    public MainWindow(IRCClient client) {
        this.client = client;

        this.setSize(1080, 1080);
        this.setTitle("Jirc");
        //this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(5,5));

        JMenuBar menu = new JMenuBar();

        JMenu jirc = new JMenu(Strings.FILE_MENU_LABEL);

        JMenuItem about = new JMenuItem(Strings.ABOUT_MENU_ITEM_LABEL);
        about.addActionListener((e) -> new AboutWindow());
        jirc.add(about);

        menu.add(jirc);
//        menu.add(new JMenu("View"));
        this.setJMenuBar(menu);

        String[] buttons = new String[]{"Dummy1", "Dummy2", "Dummy3"};
        JList<String> channels = new JList<>(buttons);
        channels.setBackground(Color.GRAY);
        channels.setPreferredSize(new Dimension(100, 640));
        //channelsPrinter(channels, new String[]{"Dummy1", "Dummy2", "Dummy3"});
        this.add(channels, BorderLayout.WEST);

        JPanel center = new JPanel(new GridBagLayout());
        JTextPane chat = new JTextPane();
        chat.setContentType("text/html");
        chat.setEditable(false);

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
            append(chat, m);
            message.setText("");
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        center.add(message, gbc);

        /*JButton send = new JButton("Send");
        send.addActionListener((e) -> {
            showMessage(chat, new String[]{"09.05.2022", "Suren2003ah", message.getText()});
            message.setText("");
        });*/
        //messageBox.add(send);
        this.add(center, BorderLayout.CENTER);

        /*String[] arr = new String[]{"Suren2003ah", "MintSoup", "Anton LaVel"};
        JList<String> status = new JList<>(arr);
        status.setPreferredSize(new Dimension(200, chat.getPreferredSize().height));
        status.setBackground(Color.GRAY);

        this.add(status, BorderLayout.EAST);*/

        this.setVisible(true);

        try {
            client.join("#test");
        } catch (IRCException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Thread flow = new Thread(() -> {
            try {
                client.listenForMessages((r, m) -> {
                    if (m != null)
                        append(chat, m);
                    return false;
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, "Message Receiver");
        flow.start();
    }

    private void channelsPrinter(JPanel panel, String[] names){
        for (int i = 0; i < names.length; i++){
            JButton temp = new JButton(names[i]);
            panel.add(temp);
        }
    }

    private void append(JTextPane chat, Message m){
        StyledDocument doc = chat.getStyledDocument();
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, Color.BLACK);
        //StyleConstants.setBackground(keyWord, Color.YELLOW);
        StyleConstants.setBold(keyWord, true);
        try {
            doc.insertString(doc.getLength(), m.toString() + "\n", keyWord);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    private void peoplePrinter(JPanel panel, String[] people){
        for (int i = 0; i < people.length; i++){
            JLabel temp = new JLabel("<html> " + people[i] + " <br><br> </html>");
            panel.add(temp);
        }
    }
}

package am.aua.sas.jirc.gui;

import am.aua.sas.jirc.gui.intl.Strings;
import am.aua.sas.jirc.irc.IRCClient;
import am.aua.sas.jirc.irc.Message;
import am.aua.sas.jirc.irc.exceptions.IRCException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

public class MainWindow extends JFrame {
    private final IRCClient client;
    private final Thread listenerThread;
    private HashMap<String, Channel> channels;
    private String currentChannel;

    public MainWindow(IRCClient client, String... autoJoin) {
        this.client = client;
        this.channels = new HashMap<>();
        this.currentChannel = autoJoin[0];

        this.initFrame();

        this.setUpMenuBar();

        JPanel center = new JPanel();
        center.setLayout(new CardLayout());

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> channelList = new JList<>(model);
        channelList.setBackground(Color.GRAY);
        channelList.setPreferredSize(new Dimension(100, 640));
        channelList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = channelList.locationToIndex(e.getPoint());
                String s = model.getElementAt(i);
                currentChannel = s;
                ((CardLayout) center.getLayout()).show(center, s);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        this.add(channelList, BorderLayout.WEST);

        this.add(center, BorderLayout.CENTER);

        listenerThread = new Thread(() -> {
                try {
                    this.setTitle("Loading...");
                    client.open();
                    for (int i = 0; i < autoJoin.length; i++) {
                        client.join(autoJoin[i]);
                        model.addElement(autoJoin[i]);
                        Channel c = Channel.generateCenterBox(autoJoin[i], client);
                        channels.put(autoJoin[i], c);
                        center.add(c.getPanel(), autoJoin[i]);
                    }
                    this.setTitle("Jirc");
                    //appendInternalMessage("Joined #test\n");
                    client.listenForMessages((r, m) -> {
                        if (m != null) {
                            Channel c = channels.get(m.getChannel());
                            c.getChat().append(m);
                        } else
                            channels.get(currentChannel).getChat().appendServerLine(r);
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

    private void initFrame() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(Strings.APP_NAME);

        LayoutManager layout = new BorderLayout(5, 5);
        this.setLayout(layout);
    }

    private void setUpMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu(Strings.FILE_MENU_LABEL);
        JMenuItem about = new JMenuItem(Strings.ABOUT_WINDOW_TITLE);
        about.addActionListener((e) -> JircGui.show(new AboutWindow()));
        fileMenu.add(about);
        menuBar.add(fileMenu);

        JMenu exportMenu = new JMenu(Strings.EXPORT_MENU_LABEL);
        JMenuItem exportCurrent = new JMenuItem(Strings.EXPORT_CURRENT_MENU_ITEM_LABEL);
        //exportCurrent.addActionListener((e) -> save());
        exportMenu.add(exportCurrent);
        menuBar.add(exportMenu);

        this.setJMenuBar(menuBar);
    }

    private void save() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream("app/src/main/java/am/aua/sas/jirc/exports/database.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String content = channels.get(currentChannel).getChat().getText();
        writer.println(content);
        writer.close();
    }


    private static class Channel {
        private static final GridBagConstraints constraints = new GridBagConstraints();
        private JPanel panel;
        private ChatPane chat;
        private JScrollPane scrollPane;

        public Channel(JPanel panel, ChatPane chat, JScrollPane scrollPane) {
            this.panel = panel;
            this.chat = chat;
            this.scrollPane = scrollPane;
        }

        public JPanel getPanel() {
            return panel;
        }

        public ChatPane getChat() {
            return chat;
        }

        public JScrollPane getScrollPane() {
            return scrollPane;
        }

        public static Channel generateCenterBox(String channel, IRCClient client) {
            JPanel center = new JPanel(new GridBagLayout());

            ChatPane chat = new ChatPane();
            JScrollPane scrollPane = new JScrollPane(chat);
            chat.setEditable(false);
            chat.setPreferredSize(new Dimension(800, 800));

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.BOTH;
            center.add(scrollPane, constraints);

            JTextField message = new JTextField();
            // add placeholder here
            message.addActionListener((e) -> {
                try {
                    client.sendMessage(channel, message.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Message m = new Message(client.getNickname(), channel, message.getText(), new Date());
                chat.appendMine(m);
                message.setText("");
            });
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.weightx = 1;
            constraints.weighty = 0;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            center.add(message, constraints);

            Channel c = new Channel(center, chat, scrollPane);

            return c;
        }
    }
}

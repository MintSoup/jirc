package am.aua.sas.jirc.gui;

import am.aua.sas.jirc.Server;
import am.aua.sas.jirc.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JircLoginForm extends JFrame {
    private static final Dimension SIZE = new Dimension(400, 200);

    private final JPanel contentPanel = new JPanel();
    private final JPanel actionsPanel = new JPanel();

    // TODO
    private final Server[] servers = {
            new Server("irc.libera.chat", "6667"),
    };

    public JircLoginForm() {
        this.initFrame();
        this.initContentPanel();

        JLabel serverListLabel = new FormLabel(Strings.SERVER);
        JComboBox<Server> serverList = new JComboBox<>(this.servers);
        serverList.setEditable(true);
        serverList.setSelectedIndex(0); // TODO
        serverList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("comboBoxEdited")) {
                    Object newItem = serverList.getSelectedItem();
                    if (newItem != null && !newItem.toString().trim().equals("")) {
                        Server newServer = new Server(newItem.toString(), "6667");
                        serverList.addItem(newServer);
                        serverList.setSelectedItem(newServer);
                    }
                }
            }
        });
        GridBagConstraints c = new GridBagConstraints();
        this.add(serverListLabel, c);
        this.add(serverList);

        JLabel nicknameLabel = new FormLabel(Strings.NICKNAME);
        JTextField nickname = new JTextField();
        this.add(nicknameLabel);
        this.add(nickname);

        JLabel usernameLabel = new FormLabel(Strings.USERNAME);
        JTextField username = new JTextField();
        this.add(usernameLabel);
        this.add(username);
    }

    private void initFrame() {
        this.setMinimumSize(SIZE);
        this.setSize(SIZE);
        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        LayoutManager layout = new BorderLayout();
        this.setLayout(layout);
    }

    private void initContentPanel() {
        LayoutManager layout = new GridBagLayout();
        this.contentPanel.setLayout(layout);

        this.add(this.contentPanel);
    }

    private static class FormLabel extends JLabel {
        public FormLabel(String text) {
            super(text, JLabel.TRAILING);
        }

        @Override
        public void setText(String text) {
            super.setText(text + ": ");
        }
    }
}

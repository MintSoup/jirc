package am.aua.sas.jirc.gui;

import am.aua.sas.jirc.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionForm extends JFrame {
    private static final Dimension SIZE = new Dimension(400, 200);

    private final JPanel contentPanel = new JPanel();
    private final JPanel actionsPanel = new JPanel();

    // TODO
    private final Server[] servers = {
            new Server("irc.libera.chat", "6667"),
    };

    public ConnectionForm() {
        this.initFrame();
        this.initContentPanel();
        this.initActionsPanel();

        JLabel serverListLabel = new FormLabel(Strings.SERVER_LABEL);
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

        // TODO
        final Insets labelInsets = new Insets(10, 0, 0, 4);
        final Insets fieldInsets = new Insets(10, 0, 0, 0);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 8;

        c.gridy = 0;
        c.gridx = 0;
        c.insets = labelInsets;
        this.contentPanel.add(serverListLabel, c);
        c.gridx = 1;
        c.insets = fieldInsets;
        this.contentPanel.add(serverList, c);

        JLabel nicknameLabel = new FormLabel(Strings.NICKNAME_LABEL);
        JTextField nickname = new JTextField();

        c.gridy++;
        c.gridx = 0;
        c.insets = labelInsets;
        this.contentPanel.add(nicknameLabel, c);
        c.gridx = 1;
        c.insets = fieldInsets;
        this.contentPanel.add(nickname, c);

        JLabel usernameLabel = new FormLabel(Strings.USERNAME_LABEL);
        JTextField username = new JTextField();

        c.gridy++;
        c.gridx = 0;
        c.insets = labelInsets;
        this.contentPanel.add(usernameLabel, c);
        c.gridx = 1;
        c.insets = fieldInsets;
        this.contentPanel.add(username, c);

        JButton continueButton = new JButton(Strings.CONNECTION_FORM_BUTTON_OKAY);
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // TODO
            }
        });
        this.actionsPanel.add(continueButton);
    }

    private void initFrame() {
        this.setMinimumSize(SIZE);
        this.setSize(SIZE);
        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(Strings.CONNECTION_FORM_TITLE);

        LayoutManager layout = new BorderLayout();
        this.setLayout(layout);
    }

    private void initContentPanel() {
        GridBagLayout layout = new GridBagLayout();
        this.contentPanel.setLayout(layout);

        this.add(this.contentPanel, BorderLayout.PAGE_START);
    }

    private void initActionsPanel() {
        FlowLayout layout = new FlowLayout(FlowLayout.TRAILING);
        this.actionsPanel.setLayout(layout);

        this.add(this.actionsPanel, BorderLayout.PAGE_END);
    }

    private static class FormLabel extends JLabel {
        public FormLabel(String text) {
            super(text, JLabel.TRAILING);
        }

        @Override
        public void setText(String text) {
            super.setText(text + ":");
        }
    }
}

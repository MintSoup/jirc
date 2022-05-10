package am.aua.sas.jirc.gui;

import am.aua.sas.jirc.gui.intl.Strings;
import am.aua.sas.jirc.irc.IRCClient;
import am.aua.sas.jirc.irc.Server;
import am.aua.sas.jirc.persistence.ConnectionsRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ConnectionsWindow extends JFrame {
    private static final Dimension SIZE = new Dimension(400, 200);

    private final JPanel contentPanel = new JPanel();
    private final JPanel actionsPanel = new JPanel();

    private final DefaultComboBoxModel<Server> serversModel = new DefaultComboBoxModel<>();

    {
        serversModel.addElement(new Server(IRCClient.DEFAULT_SERVER, IRCClient.DEFAULT_PORT));
        serversModel.addAll(ConnectionsRepository.getInstance().get());
    }

    public ConnectionsWindow() {
        this.initFrame();
        this.initContentPanel();
        this.initActionsPanel();

        JLabel serverListLabel = new FormLabel(Strings.SERVER_LABEL);
        JComboBox<Server> serverField = new JComboBox<>(serversModel);
        serverField.setEditable(true);
        serverField.setSelectedIndex(0); // TODO: Select last used
        serverField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Save to disk
                if (e.getActionCommand().equals("comboBoxEdited")) {
                    Object selectedItem = serversModel.getSelectedItem();
                    if (selectedItem == null) {
                        return;
                    }

                    String[] parsedItem = selectedItem.toString().trim().split(":");
                    Server newServer;
                    // split() notoriously returns a non-empty array even if the string is empty
                    if (parsedItem[0].length() == 0) {
                        newServer = new Server(IRCClient.DEFAULT_SERVER, IRCClient.DEFAULT_PORT);
                    } else if (parsedItem.length == 1) {
                        newServer = new Server(parsedItem[0], IRCClient.DEFAULT_PORT);
                        // TODO: Check for duplicates
                        ConnectionsRepository.getInstance().persist(newServer);
                        serversModel.addElement(newServer);
                    } else {
                        newServer = new Server(parsedItem[0], Integer.parseInt(parsedItem[1]));
                        ConnectionsRepository.getInstance().persist(newServer);
                        serversModel.addElement(newServer);
                    }
                    serversModel.setSelectedItem(newServer);
                }
            }
        });

        // TODO: Abstract away
        serverField.setPreferredSize(new Dimension(200, serverField.getPreferredSize().height));

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
        this.contentPanel.add(serverField, c);

        JLabel usernameLabel = new FormLabel(Strings.USERNAME_LABEL);
        JTextField usernameField = new JTextField();

        c.gridy++;
        c.gridx = 0;
        c.insets = labelInsets;
        this.contentPanel.add(usernameLabel, c);
        c.gridx = 1;
        c.insets = fieldInsets;
        this.contentPanel.add(usernameField, c);

        JButton continueButton = new JButton(Strings.CONNECTION_FORM_BUTTON_OKAY);
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Server selectedServer = (Server) Objects.requireNonNull(serverField.getSelectedItem());
                // TODO: Try to split into hostname and port, otherwise use default port
                String username = usernameField.getText();
                if (username.trim().equals("")) {
                    showErrorMessage(Strings.BLANK_USERNAME_ERROR_MESSAGE);
                    return;
                }
                JircGui.hide(ConnectionsWindow.this);
                JircGui.show(new MainWindow(new IRCClient(IRCClient.DEFAULT_SERVER, IRCClient.DEFAULT_PORT, username)));
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

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, Strings.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
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

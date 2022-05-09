package am.aua.sas.jirc.gui;

import am.aua.sas.jirc.Server;
import am.aua.sas.jirc.irc.IRCClient;
import am.aua.sas.jirc.irc.IRCException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class ConnectionForm extends JFrame {
    private static final Dimension SIZE = new Dimension(400, 200);

    private final JPanel contentPanel = new JPanel();
    private final JPanel actionsPanel = new JPanel();

    // TODO: Read from disk
    private final Server[] servers = {
            new Server(IRCClient.DEFAULT_SERVER, IRCClient.DEFAULT_PORT),
    };

    public ConnectionForm() {
        this.initFrame();
        this.initContentPanel();
        this.initActionsPanel();

        JLabel serverListLabel = new FormLabel(Strings.SERVER_LABEL);
        JComboBox<Server> serverField = new JComboBox<>(this.servers);
        serverField.setEditable(true);
        serverField.setSelectedIndex(0); // TODO: Select last used
        serverField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Save to disk
                if (e.getActionCommand().equals("comboBoxEdited")) {
                    Object newItem = serverField.getSelectedItem();
                    if (newItem != null && !newItem.toString().trim().equals("")) {
                        Server newServer = new Server(newItem.toString(), IRCClient.DEFAULT_PORT);
                        serverField.addItem(newServer);
                        serverField.setSelectedItem(newServer);
                    } else {
                        Server newServer = new Server(IRCClient.DEFAULT_SERVER, IRCClient.DEFAULT_PORT);
                        serverField.setSelectedItem(newServer);
                    }
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
                String username = usernameField.getText();
                if (username.trim().equals("")) {
                    showErrorMessage(Strings.BLANK_USERNAME_ERROR_MESSAGE);
                    return;
                }

                IRCClient client = new IRCClient(selectedServer.getHostname(), selectedServer.getPort(), username);
                try {
                    client.open();
                    JircGui.hide(ConnectionForm.this);
                    JircGui.show(new MainWindow(client));
                } catch (IOException e) {
                    showErrorMessage(Strings.CONNECTION_ERROR_MESSAGE);
                } catch (IRCException e) {
                    showErrorMessage(Strings.DUPLICATE_USERNAME_ERROR_MESSAGE);
                }
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
        JOptionPane.showMessageDialog(
                this,
                message,
                Strings.ERROR_TITLE,
                JOptionPane.ERROR_MESSAGE
        );
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

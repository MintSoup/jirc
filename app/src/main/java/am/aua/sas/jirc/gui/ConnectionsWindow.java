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
    private static final Insets LABEL_INSETS = new Insets(10, 0, 0, 4);
    private static final Insets FIELD_INSETS = new Insets(10, 0, 0, 0);

    private final JPanel contentPanel = new JPanel();
    private final JPanel actionsPanel = new JPanel();

    private final GridBagConstraints constraints;

    private final ConnectionsRepository repository = ConnectionsRepository.getInstance();
    private final DefaultComboBoxModel<Server> serversModel = new DefaultComboBoxModel<>();

    {
        serversModel.addAll(repository.getAll());
    }

    public ConnectionsWindow() {
        this.initFrame();
        this.initContentPanel();
        this.initActionsPanel();

        this.constraints = new GridBagConstraints();
        this.constraints.fill = GridBagConstraints.HORIZONTAL;
        this.constraints.ipady = 8;

        JComboBox<Server> serverField = new JComboBox<>(serversModel);
        serverField.setEditable(true);
        serverField.setSelectedIndex(0); // TODO: Select last used
        serverField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                        repository.add(newServer);
                        serversModel.addElement(newServer);
                    } else {
                        newServer = new Server(parsedItem[0], Integer.parseInt(parsedItem[1]));
                        repository.add(newServer);
                        serversModel.addElement(newServer);
                    }

                    serversModel.setSelectedItem(newServer);
                }
            }
        });
        this.addField(Strings.SERVER_LABEL, serverField);

        JTextField usernameField = new JTextField();
        this.addField(Strings.USERNAME_LABEL, usernameField);

        JButton continueButton = new JButton(Strings.CONNECTIONS_WINDOW_BUTTON_OKAY);
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

                JircGui.hide(ConnectionsWindow.this);
                JircGui.show(new MainWindow(client));
            }
        });
        this.actionsPanel.add(continueButton);
    }

    private void initFrame() {
        this.setSize(SIZE);
        this.setMinimumSize(SIZE);
        this.setPreferredSize(SIZE);
        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(Strings.CONNECTIONS_WINDOW_TITLE);

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

    private void addField(String label, Component field) {
        constraints.gridy++;
        constraints.gridx = 0;
        constraints.insets = LABEL_INSETS;
        this.contentPanel.add(new FormLabel(label), constraints);

        constraints.gridx = 1;
        constraints.insets = FIELD_INSETS;
        field.setPreferredSize(new Dimension(200, field.getPreferredSize().height));
        this.contentPanel.add(field, constraints);
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

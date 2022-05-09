package am.aua.sas.jirc.gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private static GridBagConstraints gbc = new GridBagConstraints();

    public MainWindow(){
        this.setSize(1080, 1080);
        this.setTitle("Jirc");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());

        JMenuBar menu = new JMenuBar();

        JMenu jirc = new JMenu("Jirc");

        JMenuItem about = new JMenuItem("About");
        about.addActionListener((e) -> new AboutWindow());
        jirc.add(about);

        /* testing purposes
        JMenuItem title = new JMenuItem("Change The Title");
        title.addActionListener((e) -> this.setTitle("lmao"));
        jirc.add(title);
         */

        menu.add(jirc);
        menu.add(new JMenu("View"));
        this.setJMenuBar(menu);

        JPanel channels = new JPanel();
        channels.setLayout(new GridBagLayout());

        channelsPrinter(channels, new String[]{"Dummy1", "Dummy2", "Dummy3"});

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(channels, gbc);

        JTextArea chat = new JTextArea(40, 60);
        chat.setEnabled(false);

        showMessage(chat, new String[]{"09.05.2022", "Suren2003ah", "helo"});

        gbc.gridx = 1;
        gbc.gridy = 0;
        this.add(chat, gbc);

        JPanel messageBox = new JPanel();
        JTextField message = new JTextField();
        JButton send = new JButton("Send");
        send.addActionListener((e) -> {
            showMessage(chat, new String[]{"09.05.2022", "Suren2003ah", message.getText()});
            message.setText("");
        });
        message.setColumns(50);
        messageBox.add(message);
        messageBox.add(send);
        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(messageBox, gbc);

        JPanel status = new JPanel();
        status.setLayout(new GridBagLayout());
        peoplePrinter(status, new String[]{"Suren2003ah", "MintSoup", "Anton LaVel"});
        gbc.gridx = 2;
        gbc.gridy = 0;
        this.add(status, gbc);

        this.setVisible(true);
    }

    private void channelsPrinter(JPanel panel, String[] names){
        for (int i = 0; i < names.length; i++){
            JButton temp = new JButton(names[i]);
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(temp, gbc);
        }
    }

    private void showMessage(JTextArea area, String[] messageInfo){
        area.append("[" + messageInfo[0] + "] <" + messageInfo[1] + ">: " + messageInfo[2] + "\n");
    }

    private void peoplePrinter(JPanel panel, String[] people){
        for (int i = 0; i < people.length; i++){
            JLabel temp = new JLabel("<html> " + people[i] + " <br><br> </html>");
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(temp, gbc);
        }
    }
}

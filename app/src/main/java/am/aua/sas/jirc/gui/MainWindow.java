package am.aua.sas.jirc.gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private static GridBagConstraints gbc = new GridBagConstraints();

    public MainWindow(){
        this.setSize(1080, 1080);
        this.setTitle("Jirc");
        //this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(5,5));

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

        String[] buttons = new String[]{"Dummy1", "Dummy2", "Dummy3"};
        JList<String> channels = new JList<>(buttons);
        channels.setBackground(Color.GRAY);
        channels.setPreferredSize(new Dimension(100, 640));
        //channelsPrinter(channels, new String[]{"Dummy1", "Dummy2", "Dummy3"});
        this.add(channels, BorderLayout.WEST);

        JPanel center = new JPanel(new GridBagLayout());
        JTextArea chat = new JTextArea();
        chat.setEnabled(false);

        showMessage(chat, new String[]{"09.05.2022", "Suren2003ah", "helo"});
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        center.add(chat, gbc);

        JTextField message = new JTextField();
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

        String[] arr = new String[]{"Suren2003ah", "MintSoup", "Anton LaVel"};
        JList<String> status = new JList<>(arr);
        status.setPreferredSize(new Dimension(200, chat.getPreferredSize().height));
        status.setBackground(Color.GRAY);

        this.add(status, BorderLayout.EAST);

        this.setVisible(true);
    }

    private void channelsPrinter(JPanel panel, String[] names){
        for (int i = 0; i < names.length; i++){
            JButton temp = new JButton(names[i]);
            panel.add(temp);
        }
    }

    private void showMessage(JTextArea area, String[] messageInfo){
        area.append("[" + messageInfo[0] + "] <" + messageInfo[1] + ">: " + messageInfo[2] + "\n");
    }

    private void peoplePrinter(JPanel panel, String[] people){
        for (int i = 0; i < people.length; i++){
            JLabel temp = new JLabel("<html> " + people[i] + " <br><br> </html>");
            panel.add(temp);
        }
    }
}

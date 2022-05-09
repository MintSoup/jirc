package am.aua.sas.jirc.gui;

import javax.swing.*;
import java.awt.*;

public class AboutWindow extends JFrame {
    private static GridBagConstraints gbc = new GridBagConstraints();
    public AboutWindow(){
        this.setTitle("About");
        this.setSize(300, 300);
        this.setLayout(new GridBagLayout());

        JLabel text = new JLabel("<html> <center> Created by <center> <br> Suren Hakobyan <br> Areg Hovhannisyan <br> Samvel Davtyan <br><br><br> </html>");
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(text, gbc);

        JButton ok = new JButton("OK");
        ok.addActionListener((e) -> this.dispose());
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(ok, gbc);

        this.setVisible(true);
    }
}

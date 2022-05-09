package am.aua.sas.jirc.gui;

import javax.swing.*;
import java.awt.*;

public class JircGUI extends JFrame {
    public JircGUI(){
        this.setSize(1080, 1080);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        JMenuBar menu = new JMenuBar();

        JTextField messageBox = new JTextField();
        messageBox.setSize(800, 50);

        this.add(messageBox);
        this.add(menu);
        this.setVisible(true);
    }
}

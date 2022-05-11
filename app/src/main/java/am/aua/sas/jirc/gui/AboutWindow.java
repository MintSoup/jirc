package am.aua.sas.jirc.gui;

import am.aua.sas.jirc.gui.intl.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutWindow extends JFrame {
    private static final Dimension SIZE = new Dimension(300, 200);

    private static final GridBagConstraints constraints = new GridBagConstraints();

    public AboutWindow() {
        this.initFrame();

        JLabel text = new JLabel("<html> <center> Created by <center> <br> Suren Hakobyan <br> Areg Hovhannisyan <br> Samvel Davtyan <br><br><br> </html>");
        constraints.gridy = 0;
        this.add(text, constraints);

        JButton ok = new JButton(Strings.ABOUT_WINDOW_BUTTON_OKAY);
        ok.addActionListener((e) -> JircGui.hide(AboutWindow.this));
        constraints.gridy++;
        this.add(ok, constraints);
    }

    private void initFrame() {
        this.setSize(SIZE);
        this.setPreferredSize(SIZE);
        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(Strings.ABOUT_WINDOW_TITLE);

        LayoutManager layout = new GridBagLayout();
        this.setLayout(layout);
    }
}

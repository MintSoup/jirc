package am.aua.sas.jirc.gui;

import javax.swing.*;

public class JircGui {
    public JircGui() {
        ConnectionsWindow loginForm = new ConnectionsWindow();
        show(loginForm);
    }

    protected static void show(JFrame frame) {
        frame.pack();
        frame.setVisible(true);
    }

    protected static void hide(JFrame frame) {
        frame.setVisible(false);
        frame.dispose();
    }
}

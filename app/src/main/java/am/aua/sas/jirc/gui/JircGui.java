package am.aua.sas.jirc.gui;

import am.aua.sas.jirc.irc.IRCClient;

import javax.swing.*;

public class JircGui {
    public static IRCClient client;

    public JircGui() {
        ConnectionForm loginForm = new ConnectionForm();
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

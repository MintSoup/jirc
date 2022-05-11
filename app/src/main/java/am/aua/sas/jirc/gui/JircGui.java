package am.aua.sas.jirc.gui;

import am.aua.sas.jirc.gui.intl.Strings;

import javax.swing.*;
import java.awt.*;

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

    protected static void showErrorMessage(Component context, String message) {
        JOptionPane.showMessageDialog(context, message, Strings.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }
}

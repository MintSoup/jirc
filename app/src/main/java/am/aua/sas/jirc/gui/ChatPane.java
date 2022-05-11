package am.aua.sas.jirc.gui;

import am.aua.sas.jirc.irc.Message;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class ChatPane extends JTextPane {
    private static final Color INT_MESSAGE_COLOR = new Color(0x98c379);
    private static final Color SERVER_LINE_COLOR = new Color(0x767676);
    private static final Color DATE_COLOR = new Color(0x51afef);
    private static final Color MY_NICKNAME_COLOR = new Color(0xc678dd);
    private static final Color NICKNAME_COLOR = new Color(0xd19a66);

    public void append(String message, Color color, boolean bold) {
        StyledDocument doc = this.getStyledDocument();
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setFontSize(keyWord, 16);
        StyleConstants.setBold(keyWord, bold);
        StyleConstants.setForeground(keyWord, color);
        try {
            doc.insertString(doc.getLength(), message, keyWord);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
		setCaretPosition(doc.getLength());
    }

    public void append(Message m) {
        append("[" + Message.DATE_FORMAT.format(m.getTimestamp()) + "] ", DATE_COLOR, true);
        append("<" + m.getSender() + "> ", NICKNAME_COLOR, true);
        append(m.getContent() + "\n", Color.BLACK, false);
    }

    public void appendMine(Message m) {
        append("[" + Message.DATE_FORMAT.format(m.getTimestamp()) + "] ", DATE_COLOR, true);
        append("<" + m.getSender() + "> ", MY_NICKNAME_COLOR, true);
        append(m.getContent() + "\n", Color.BLACK, false);
    }

    public void appendServerLine(String line) {
        append(line, SERVER_LINE_COLOR, false);
    }

    public void appendInternalMessage(String line) {
        append(line, INT_MESSAGE_COLOR, true);
    }
}

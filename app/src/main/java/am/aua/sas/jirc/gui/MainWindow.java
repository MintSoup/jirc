package am.aua.sas.jirc.gui;

import am.aua.sas.jirc.irc.IRCClient;
import am.aua.sas.jirc.irc.IRCException;
import am.aua.sas.jirc.irc.Message;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.IOException;
import java.util.Date;

public class MainWindow extends JFrame {
	private static GridBagConstraints gbc = new GridBagConstraints();

	private IRCClient client;
	private Thread listenerThread;

	public MainWindow() {
		this.setSize(1080, 1080);
		this.setTitle("Jirc");
		// this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout(5, 5));

		JMenuBar menu = new JMenuBar();

		JMenu jirc = new JMenu(Strings.FILE_MENU_LABEL);

		JMenuItem about = new JMenuItem(Strings.ABOUT_MENU_ITEM_LABEL);
		about.addActionListener((e) -> new AboutWindow());
		jirc.add(about);

		menu.add(jirc);
		// menu.add(new JMenu("View"));
		this.setJMenuBar(menu);

		String[] buttons = new String[] { "Dummy1", "Dummy2", "Dummy3" };
		JList<String> channels = new JList<>(buttons);
		channels.setBackground(Color.GRAY);
		channels.setPreferredSize(new Dimension(100, 640));
		// channelsPrinter(channels, new String[]{"Dummy1", "Dummy2", "Dummy3"});
		this.add(channels, BorderLayout.WEST);

		JPanel center = new JPanel(new GridBagLayout());
		JTextPane chat = new JTextPane();
		chat.setContentType("text/html");
		chat.setEditable(false);
		chat.setPreferredSize(new Dimension(600, 800));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		center.add(chat, gbc);

		JTextField message = new JTextField();
		// add placeholder here
		message.addActionListener((e) -> {
			try {
				client.sendMessage("#test", message.getText());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			Message m = new Message(client.getNickname(), "#test", message.getText(), new Date());
			append(chat, m);
			message.setText("");
		});
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		center.add(message, gbc);

		/*
		 * JButton send = new JButton("Send"); send.addActionListener((e) -> {
		 * showMessage(chat, new String[]{"09.05.2022", "Suren2003ah",
		 * message.getText()}); message.setText(""); });
		 */
		// messageBox.add(send);
		this.add(center, BorderLayout.CENTER);

		/*
		 * String[] arr = new String[]{"Suren2003ah", "MintSoup", "Anton LaVel"};
		 * JList<String> status = new JList<>(arr); status.setPreferredSize(new
		 * Dimension(200, chat.getPreferredSize().height));
		 * status.setBackground(Color.GRAY);
		 *
		 * this.add(status, BorderLayout.EAST);
		 */

		listenerThread = new Thread(() -> {
			try {
				client = new IRCClient();
				client.open();
				client.join("#test");
				client.listenForMessages((r, m) -> {
					if (m != null)
						append(chat, m);
					else
						append(chat, r + "\n");
					return false;
				});
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (IRCException e1) {
				e1.printStackTrace();
			}
		}, "Message Receiver");
		listenerThread.start();
	}

	private void channelsPrinter(JPanel panel, String[] names) {
		for (int i = 0; i < names.length; i++) {
			JButton temp = new JButton(names[i]);
			panel.add(temp);
		}
	}

	private void append(JTextPane chat, Message m) {
		try {
			StyledDocument doc = chat.getStyledDocument();
			SimpleAttributeSet keyWord = new SimpleAttributeSet();
			StyleConstants.setBold(keyWord, true);
			StyleConstants.setForeground(keyWord, Color.BLUE);
			doc.insertString(doc.getLength(), "[" + Message.DATE_FORMAT.format(m.getTimestamp()) + "]", keyWord);
			StyleConstants.setForeground(keyWord, Color.ORANGE);
			doc.insertString(doc.getLength(), "<" + m.getSender() + ">", keyWord);
			StyleConstants.setForeground(keyWord, Color.BLACK);
			doc.insertString(doc.getLength(), m.getContent() + "\n", keyWord);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}

	private void append(JTextPane chat, String raw) {
		try {
			StyledDocument doc = chat.getStyledDocument();
			SimpleAttributeSet keyWord = new SimpleAttributeSet();
			StyleConstants.setBold(keyWord, true);
			StyleConstants.setForeground(keyWord, Color.GRAY);
			doc.insertString(doc.getLength(), raw, keyWord);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}

	private void peoplePrinter(JPanel panel, String[] people) {
		for (int i = 0; i < people.length; i++) {
			JLabel temp = new JLabel("<html> " + people[i] + " <br><br> </html>");
			panel.add(temp);
		}
	}
}

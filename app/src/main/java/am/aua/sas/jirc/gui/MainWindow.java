package am.aua.sas.jirc.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.Taskbar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import am.aua.sas.jirc.gui.intl.Strings;
import am.aua.sas.jirc.irc.IRCClient;
import am.aua.sas.jirc.irc.Message;
import am.aua.sas.jirc.irc.exceptions.IRCException;
import am.aua.sas.jirc.irc.exceptions.NicknameAlreadyInUseException;

public class MainWindow extends JFrame {
	private final IRCClient client;
	private final Thread listenerThread;
	private final HashMap<String, Channel> channels;
	private String currentChannel;

	public MainWindow(IRCClient client, String... autoJoin) {
		this.client = client;
		this.channels = new HashMap<>();
		this.currentChannel = autoJoin[0];

		this.initFrame();

		this.setUpMenuBar();

		JPanel center = new JPanel();
		center.setLayout(new CardLayout());

		DefaultListModel<String> model = new DefaultListModel<>();
		JList<String> channelList = new JList<>(model);
		channelList.setBackground(new Color(0x98c379));
		channelList.setPreferredSize(new Dimension(100, 640));
		channelList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);

				int i = channelList.locationToIndex(e.getPoint());
				if (i == -1)
					return;
				String s = model.getElementAt(i);
				currentChannel = s;
				((CardLayout) center.getLayout()).show(center, s);
			}
		});
		this.add(channelList, BorderLayout.WEST);

		this.add(center, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
				try {
					client.quit();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            }
        });


		listenerThread = new Thread(() -> {
			try {
				this.setTitle(Strings.LOADING);
				try {
					client.open();
				} catch (NicknameAlreadyInUseException e) {
					JircGui.showErrorMessage(this, e.getMessage());
					dispose();
					new JircGui();
					return;
				}
				for (int i = 0; i < autoJoin.length; i++) {
					client.join(autoJoin[i]);
					model.addElement(autoJoin[i]);
					Channel c = Channel.generateCenterBox(autoJoin[i], client);
					channels.put(autoJoin[i], c);
					center.add(c.getPanel(), autoJoin[i]);
				}
				this.setTitle(Strings.APP_NAME);
				// appendInternalMessage("Joined #test\n");
				client.listenForMessages((r, m) -> {
					if (m != null) {
						Channel c = channels.get(m.getChannel());
						c.getChat().append(m);
					} else {
						// channels.get(currentChannel).getChat().appendServerLine(r);
					}
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

	private void initFrame() {
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setLocationRelativeTo(null);

		try {
			File file = new File(Objects.requireNonNull(getClass().getResource("/jirc.png")).getFile());
			BufferedImage image = ImageIO.read(file);
			this.setIconImage(image);
			if (Taskbar.isTaskbarSupported()) {
				final Taskbar taskbar = Taskbar.getTaskbar();
				taskbar.setIconImage(image);
			}
		} catch (Exception ignored) {
		}

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(Strings.APP_NAME);

		LayoutManager layout = new BorderLayout(5, 5);
		this.setLayout(layout);
	}

	private void setUpMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(Strings.FILE_MENU_LABEL);
		JMenuItem about = new JMenuItem(Strings.ABOUT_WINDOW_TITLE);
		about.addActionListener((e) -> JircGui.show(new AboutWindow()));
		fileMenu.add(about);
		menuBar.add(fileMenu);

		JMenu exportMenu = new JMenu(Strings.EXPORT_MENU_LABEL);
		JMenuItem exportCurrent = new JMenuItem(Strings.EXPORT_CURRENT_MENU_ITEM_LABEL);
		exportCurrent.addActionListener((e) -> save());
		exportMenu.add(exportCurrent);
		menuBar.add(exportMenu);

		this.setJMenuBar(menuBar);
	}

	private void save() {
		try {
			Path path = Path.of(FileSystemView.getFileSystemView().getDefaultDirectory().getPath(), Strings.APP_NAME,
					Date.from(Instant.now()) + ".txt");
			Files.createDirectories(path.getParent());
			File file = path.toFile();
			file.createNewFile();

			PrintWriter writer = new PrintWriter(new FileOutputStream(file));
			String content = channels.get(currentChannel).getChat().getText();
			writer.println(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			JircGui.showErrorMessage(this, Strings.COULD_NOT_EXPORT_ERROR_MESSAGE);
		}

		JircGui.showSuccessMessage(this, Strings.SUCCESSFULLY_EXPORTED_MESSAGE);
	}

	private static class Channel {
		private static final GridBagConstraints constraints = new GridBagConstraints();
		private final JPanel panel;
		private final ChatPane chat;
		private final JScrollPane scrollPane;

		public Channel(JPanel panel, ChatPane chat, JScrollPane scrollPane) {
			this.panel = panel;
			this.chat = chat;
			this.scrollPane = scrollPane;
		}

		public JPanel getPanel() {
			return panel;
		}

		public ChatPane getChat() {
			return chat;
		}

		public JScrollPane getScrollPane() {
			return scrollPane;
		}

		public static Channel generateCenterBox(String channel, IRCClient client) {
			JPanel center = new JPanel(new GridBagLayout());

			ChatPane chat = new ChatPane();
			JScrollPane scrollPane = new JScrollPane(chat);
			chat.setEditable(false);
			chat.setPreferredSize(new Dimension(800, 800));

			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.weightx = 1;
			constraints.weighty = 1;
			constraints.fill = GridBagConstraints.BOTH;
			center.add(scrollPane, constraints);

			JTextField message = new JTextField();
			message.addActionListener((e) -> {
				try {
					client.sendMessage(channel, message.getText());
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
				Message m = new Message(client.getNickname(), channel, message.getText(), new Date());
				chat.appendMine(m);
				message.setText("");
			});
			constraints.gridx = 0;
			constraints.gridy = 1;
			constraints.weightx = 1;
			constraints.weighty = 0;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			center.add(message, constraints);

			return new Channel(center, chat, scrollPane);
		}
	}
}

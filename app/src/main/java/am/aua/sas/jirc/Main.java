package am.aua.sas.jirc;

import am.aua.sas.jirc.gui.JircGui;
import am.aua.sas.jirc.gui.MainWindow;
import am.aua.sas.jirc.irc.IRCClient;

public class Main {
	public static void main(String[] args) {
		new JircGui();
		//new MainWindow(new IRCClient());
	}
}

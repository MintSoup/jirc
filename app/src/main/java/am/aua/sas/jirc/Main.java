package am.aua.sas.jirc;

import am.aua.sas.jirc.gui.JircGui;
import am.aua.sas.jirc.irc.IRCException;

public class Main {
	public static void main(String[] args) throws IRCException {
		JircGui gui = new JircGui();

//		IRCClient c = new IRCClient("irc.libera.chat", 6667, "JIRC_bot");
//		try {
//			c.open();
//			c.join("#test");
//			c.sendMessage("#test", "Hello, world!");
//			c.listenForMessages((r, m) -> {
//				// System.out.println(r);
//				if (m != null) {
//					System.out.println(m);
//					if (m.getContent().equals("hello")) {
//						try {
//							c.sendMessage(m.getChannel(), "Hello, " + m.getSender() + "!");
//							return true;
//						} catch (IOException e) {
//						}
//					}
//				}
//				return false;
//			});
//			c.quit();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}

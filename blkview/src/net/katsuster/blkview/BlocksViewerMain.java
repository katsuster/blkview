/**
 * 
 */
package net.katsuster.blkview;

import javax.swing.*;

/**
 * @author katsuhiro
 */
public class BlocksViewerMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frm_main;
		JScrollPane scr_blocks;
		BlocksPanel pnl_blocks;
		HistorySender sender_inner;
		Thread sender;

		if (args.length < 1) {
			System.err.println(
					"usage:\n" + 
					"bv filename");
			return;
		}

		//ブロック表示領域を作成する
		pnl_blocks = new BlocksPanel();

		//スクロール領域を作成する
		scr_blocks = new JScrollPane(pnl_blocks);
		scr_blocks.setHorizontalScrollBarPolicy(
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scr_blocks.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		//メインウインドウを作成する
		frm_main = new JFrame();
		frm_main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm_main.setSize(640, 480);

		frm_main.add(scr_blocks);

		frm_main.setVisible(true);

		//履歴読み出しスレッドを作成する
		sender_inner = new HistorySender(args[0], pnl_blocks);
		sender = new Thread(sender_inner);
		sender.start();
	}
}

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
		JSplitPane spl_main;
		JScrollPane scr_blocks;
		LogRendererPanel pnl_blocks;
		JComponent pnl_settings;
		LogReader sender_inner;
		Thread sender;

		if (args.length < 1) {
			System.err.println(
					"usage:\n" + 
					"bv filename");
			return;
		}

		//アクセスログ表示領域を作成する
		pnl_blocks = new LogRendererPanel();

		//スクロール領域を作成する
		scr_blocks = new JScrollPane(pnl_blocks);
		scr_blocks.setHorizontalScrollBarPolicy(
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scr_blocks.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


		//表示設定領域を作成する
		pnl_settings = new JPanel();


		//分割領域を作成する
		spl_main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false);
		spl_main.setDividerSize(4);
		spl_main.setLeftComponent(scr_blocks);
		spl_main.setRightComponent(pnl_settings);

		//メインウインドウを作成する
		frm_main = new JFrame();
		frm_main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm_main.setSize(800, 480);

		frm_main.add(spl_main);

		frm_main.setVisible(true);

		//履歴読み出しスレッドを作成する
		sender_inner = new LogReader(args[0], pnl_blocks);
		sender = new Thread(sender_inner);
		sender.start();
	}
}

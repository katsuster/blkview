
package net.katsuster.blkview;

import javax.swing.*;

/**
 * <p>
 * メインウインドウを作成するクラスです。
 * </p>
 *
 * @author katsuhiro
 */
public class BlocksViewerMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LogStorage storage_r, storage_w;
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
					"blkview logfilename");
			return;
		}

		//Read/Write アクセスログの履歴を保存領域を作成します
		storage_r = new LogStorage(1800, 1);
		storage_w = new LogStorage(1800, 1);

		//アクセスログ表示領域を作成します
		pnl_blocks = new LogRendererPanel(storage_r, storage_w);

		//スクロール領域を作成します
		scr_blocks = new JScrollPane(pnl_blocks);
		scr_blocks.setHorizontalScrollBarPolicy(
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scr_blocks.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


		//表示設定領域を作成します
		pnl_settings = new RendererSettingPanel(pnl_blocks);


		//分割領域を作成します
		spl_main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false);
		spl_main.setDividerSize(4);
		spl_main.setLeftComponent(scr_blocks);
		spl_main.setRightComponent(pnl_settings);

		//メインウインドウを作成します
		frm_main = new JFrame();
		frm_main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm_main.setSize(1034, 530);

		frm_main.add(spl_main);

		frm_main.setVisible(true);

		//履歴読み出しスレッドを作成します
		sender_inner = new LogReader(args[0], storage_r, storage_w, pnl_blocks);
		sender = new Thread(sender_inner);
		sender.start();
	}
}

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

		//�A�N�Z�X���O�\���̈���쐬����
		pnl_blocks = new LogRendererPanel();

		//�X�N���[���̈���쐬����
		scr_blocks = new JScrollPane(pnl_blocks);
		scr_blocks.setHorizontalScrollBarPolicy(
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scr_blocks.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


		//�\���ݒ�̈���쐬����
		pnl_settings = new JPanel();


		//�����̈���쐬����
		spl_main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false);
		spl_main.setDividerSize(4);
		spl_main.setLeftComponent(scr_blocks);
		spl_main.setRightComponent(pnl_settings);

		//���C���E�C���h�E���쐬����
		frm_main = new JFrame();
		frm_main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm_main.setSize(800, 480);

		frm_main.add(spl_main);

		frm_main.setVisible(true);

		//����ǂݏo���X���b�h���쐬����
		sender_inner = new LogReader(args[0], pnl_blocks);
		sender = new Thread(sender_inner);
		sender.start();
	}
}

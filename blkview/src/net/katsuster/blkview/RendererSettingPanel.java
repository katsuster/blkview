
package net.katsuster.blkview;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * アクセスログ描画の設定画面を表示するクラスです。
 * </p>
 * 
 * @author katsuhiro
 */
public class RendererSettingPanel extends JPanel 
implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JTextField txt_count;
	private JButton btn_count;
	private JPanel pnl_count;

	private JTextField txt_cap;
	private JButton btn_cap;
	private JPanel pnl_cap;

	private JTextField txt_size;
	private JButton btn_size;
	private JPanel pnl_size;

	private JPanel pnl_list;

	private JScrollPane scr_settings;

	public RendererSettingPanel() {
		super();

		//
		txt_count = new JTextField(1);
		btn_count = new JButton("set cnt");
		pnl_count = new JPanel();
		pnl_count.setLayout(new BoxLayout(pnl_count, BoxLayout.LINE_AXIS));
		pnl_count.add(txt_count);
		pnl_count.add(btn_count);

		//
		txt_cap = new JTextField(1);
		btn_cap = new JButton("set cap");
		pnl_cap = new JPanel();
		pnl_cap.setLayout(new BoxLayout(pnl_cap, BoxLayout.LINE_AXIS));
		pnl_cap.add(txt_cap);
		pnl_cap.add(btn_cap);

		//
		txt_size = new JTextField(1);
		btn_size = new JButton("set size");
		pnl_size = new JPanel();
		pnl_size.setLayout(new BoxLayout(pnl_size, BoxLayout.LINE_AXIS));
		pnl_size.add(txt_size);
		pnl_size.add(btn_size);

		//
		pnl_list = new JPanel();
		pnl_list.setLayout(new BoxLayout(pnl_list, BoxLayout.PAGE_AXIS));
		pnl_list.add(pnl_count);
		pnl_list.add(pnl_cap);
		pnl_list.add(pnl_size);

		//
		scr_settings = new JScrollPane(pnl_list);

		//
		setLayout(new BorderLayout());
		add(scr_settings, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		//再描画します
		repaint();
	}
}

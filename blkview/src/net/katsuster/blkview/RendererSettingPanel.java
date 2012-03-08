
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

	private LogRendererPanel renderer;

	private JTextField txt_count;
	private JButton btn_count;
	private JPanel pnl_count;

	private JTextField txt_cap;
	private JButton btn_cap;
	private JPanel pnl_cap;

	private JTextField txt_size_w;
	private JTextField txt_size_h;
	private JButton btn_size;
	private JPanel pnl_size;

	private JPanel pnl_list;

	private JScrollPane scr_settings;

	public RendererSettingPanel(LogRendererPanel r) {
		super();

		renderer = r;

		//
		txt_count = new JTextField(1);
		btn_count = new JButton("set cnt");
		btn_count.setActionCommand(ACTION_COMMAND.SET_COUNT);
		btn_count.addActionListener(this);
		pnl_count = new JPanel();
		pnl_count.setLayout(new BoxLayout(pnl_count, BoxLayout.LINE_AXIS));
		pnl_count.add(txt_count);
		pnl_count.add(btn_count);

		//
		txt_cap = new JTextField(1);
		btn_cap = new JButton("set cap");
		btn_cap.setActionCommand(ACTION_COMMAND.SET_CAPACITY);
		btn_cap.addActionListener(this);
		pnl_cap = new JPanel();
		pnl_cap.setLayout(new BoxLayout(pnl_cap, BoxLayout.LINE_AXIS));
		pnl_cap.add(txt_cap);
		pnl_cap.add(btn_cap);

		//
		txt_size_w = new JTextField(1);
		txt_size_h = new JTextField(1);
		btn_size = new JButton("set size");
		btn_size.setActionCommand(ACTION_COMMAND.SET_SIZE);
		btn_size.addActionListener(this);
		pnl_size = new JPanel();
		pnl_size.setLayout(new BoxLayout(pnl_size, BoxLayout.LINE_AXIS));
		pnl_size.add(txt_size_w);
		pnl_size.add(txt_size_h);
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
		boolean correct;
		int count;
		int width, height;

		if (e.getActionCommand().equals(ACTION_COMMAND.SET_COUNT)) {
			correct = true;
			count = 0;

			try {
				count = Integer.valueOf(txt_count.getText());
			} catch (NumberFormatException ex) {
				//do nothing
			}
			if (count <= 0 || 1000000 < count) {
				correct = false;
				txt_count.setForeground(Color.RED);
			} else {
				txt_count.setForeground(Color.BLACK);
			}

			if (correct) {
				renderer.setBlockCount(count);
			}
		}
		if (e.getActionCommand().equals(ACTION_COMMAND.SET_CAPACITY)) {
			System.out.println("cap");
		}
		if (e.getActionCommand().equals(ACTION_COMMAND.SET_SIZE)) {
			correct = true;
			width = 0;
			height = 0;

			try {
				width = Integer.valueOf(txt_size_w.getText());
			} catch (NumberFormatException ex) {
				//do nothing
			}
			if (width <= 0 || renderer.getAreaSize().width < width) {
				correct = false;
				txt_size_w.setForeground(Color.RED);
			} else {
				txt_size_w.setForeground(Color.BLACK);
			}

			try {
				height = Integer.valueOf(txt_size_h.getText());
			} catch (NumberFormatException ex) {
				//do nothing
			}
			if (height <= 0 || renderer.getAreaSize().height < height) {
				correct = false;
				txt_size_h.setForeground(Color.RED);
			} else {
				txt_size_h.setForeground(Color.BLACK);
			}

			if (correct) {
				renderer.setBlockAreaSize(width, height);
			}
		}

		//再描画します
		//repaint();
	}

	public class ACTION_COMMAND {
		public static final String SET_COUNT = "set_cnt";
		public static final String SET_CAPACITY = "set_capacity";
		public static final String SET_SIZE = "set_size";
	}
}


package net.katsuster.blkview;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import net.katsuster.blkview.AccessLog.*;

/**
 * <p>
 * アクセスログの 2D描画、表示を行うクラスです。
 * </p>
 * 
 * @author katsuhiro
 */
public class LogRendererPanel extends JComponent 
implements LogRenderer, ActionListener {
	private static final long serialVersionUID = 1L;

	//読み込みアクセスログの履歴
	LogHistories hist_read;
	//書き込みアクセスログの履歴
	LogHistories hist_write;

	//全体の描画領域の最大値
	private Dimension content_area;
	//全体の描画領域
	//（コンポーネントの描画領域の左上を（0, 0）とする）
	private Rectangle content_rect;
	//ブロックの描画領域の最大値
	private Dimension block_area;
	//ブロックを描画する領域
	//（ブロックの描画領域の左上を (0, 0) とする）
	private Rectangle block_rect;

	//履歴を徐々に忘れさせていくタイマー
	private Timer leaper;

	public LogRendererPanel() {
		super();

		setSize(new Dimension(640, 480));
		setPreferredSize(getSize());
		setBackground(Color.WHITE);

		hist_read = new LogHistories(2000, 65536);
		hist_write = new LogHistories(2000, 66536);

		setAreaSize(getWidth(), getHeight());
		setContentMargin(5, 5, 5, 5);
		setBlockAreaSize(8, 16);
		setBlockContentMargin(1, 2, 2, 3);

		leaper = new Timer(100, this);
		startAnimation();
	}

	@Override
	public void setBlockCount(int n) {
		hist_read.setBlockCount(n);
		hist_write.setBlockCount(n);
	}

	@Override
	public void setCapacity(long n) {
		hist_read.setCapacity(n);
		hist_write.setCapacity(n);
	}

	public void addAccessLog(AccessLogRW log) {
		switch (log.getOp()) {
		case LogType.READ:
			//read
			hist_read.addAccessLog(log.getAddress(), log.getSize());
			break;
		case LogType.WRITE:
			//write
			hist_write.addAccessLog(log.getAddress(), log.getSize());
			break;
		}
	}

	public Dimension getAreaSize() {
		return (Dimension)content_area.clone();
	}

	public void setAreaSize(int width, int height) {
		content_area = new Dimension(width, height);
	}

	public Rectangle getContentBounds() {
		return (Rectangle)content_rect.clone();
	}

	public void setContentBounds(int x, int y, int width, int height) {
		content_rect = new Rectangle(x, y, width, height);
	}

	public void setContentMargin(int left, int top, int right, int bottom) {
		Dimension d = getAreaSize();

		setContentBounds(left, top, 
				d.width - left - right, d.height - top - bottom);
	}

	public Dimension getBlockAreaSize() {
		return (Dimension)block_area.clone();
	}

	public void setBlockAreaSize(int width, int height) {
		block_area = new Dimension(width, height);
	}

	public Rectangle getBlockContentBounds() {
		return (Rectangle)block_rect.clone();
	}

	public void setBlockContentBounds(int x, int y, int width, int height) {
		block_rect = new Rectangle(x, y, width, height);
	}

	public void setBlockContentMargin(int left, int top, int right, int bottom) {
		Dimension d = getBlockAreaSize();

		setBlockContentBounds(left, top, 
				d.width - left - right, d.height - top - bottom);
	}

	public void startAnimation() {
		leaper.start();
	}

	public void stopAnimation() {
		leaper.stop();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//アクセス履歴を忘れさせます
		hist_read.forgetHistories();
		hist_write.forgetHistories();

		//再描画します
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		int[] hr, hw;
		Dimension d_ba;
		Rectangle r_c, r_bc;
		int mx, my, x, y, bx, by;
		int i;

		super.paint(g);

		hr = hist_read.getHistories();
		hw = hist_write.getHistories();
		if (hr.length != hw.length) {
			//更新中と思われるため、何もしない
			return;
		}

		//d_c = getAreaSize();
		r_c = getContentBounds();
		d_ba = getBlockAreaSize();
		r_bc = getBlockContentBounds();
		mx = r_c.width / d_ba.width;
		my = r_c.height / d_ba.height;
		for (i = 0; i < hr.length; i++) {
			x = (int)(i % mx);
			y = (int)(i / mx);
			if (y > my) {
				//描画領域外まで行ったのでもう描画の必要はない
				break;
			}

			bx = r_c.x + x * d_ba.width;
			by = r_c.y + y * d_ba.height;

			//枠
			g.setColor(Color.GRAY);
			g.drawRect(bx + r_bc.x, by + r_bc.y, 
					r_bc.width, r_bc.height);
			//中身
			g.setColor(new Color(
					255 - hr[i] & 0xff, 
					255 - hw[i] & 0xff, 
					255));
			g.fillRect(bx + r_bc.x + 1, by + r_bc.y + 1, 
					r_bc.width - 1, r_bc.height - 1);
		}
	}
}

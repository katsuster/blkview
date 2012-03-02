/**
 * 
 */
package net.katsuster.blkview;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import net.katsuster.blkview.AccessLog.*;

/**
 * @author katsuhiro
 */
public class BlocksPanel extends JComponent 
implements HistoryReceiver, ActionListener {
	private static final long serialVersionUID = 1L;

	//全体の容量
	private long capacity;
	//ブロックの数
	private int block_count;
	//ブロックのバイト数
	private long block_size;
	//ブロックの読み込みアクセス履歴
	private int[] block_hist_read;
	//ブロックの書き込みアクセス履歴
	private int[] block_hist_write;

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

	public BlocksPanel() {
		super();

		setSize(new Dimension(640, 480));
		setPreferredSize(getSize());
		setBackground(Color.WHITE);

		setBlockCount(2000);
		setCapacity(65536);

		setAreaSize(getWidth(), getHeight());
		setContentMargin(5, 5, 5, 5);
		setBlockAreaSize(8, 16);
		setBlockContentMargin(1, 2, 2, 3);

		leaper = new Timer(100, this);
		startAnimation();
	}

	public long getCapacity() {
		return capacity;
	}

	public void setCapacity(long n) {
		if (n < 0) {
			throw new IllegalArgumentException(
					"capacity(" + n + ") is negative.");
		}

		capacity = n;
		setBlockSize(getCapacity() / (getBlockCount() - 1));
	}

	public int getBlockCount() {
		return block_count;
	}

	public void setBlockCount(int n) {
		if (n < 0) {
			throw new IllegalArgumentException(
					"block count(" + n + ") is negative.");
		}

		block_count = n;
		block_hist_read = new int[block_count];
		block_hist_write = new int[block_count];
	}

	public long getBlockSize() {
		return block_size;
	}

	protected void setBlockSize(long n) {
		if (n < 0) {
			throw new IllegalArgumentException(
					"block size(" + n + ") is negative.");
		}

		block_size = n;
	}

	public void addAccessLog(AccessLogRW log) {
		switch (log.getOp()) {
		case LogType.READ:
			//read
			addReadAccessLog(log.getAddress(), log.getSize());
			break;
		case LogType.WRITE:
			//write
			addWriteAccessLog(log.getAddress(), log.getSize());
			break;
		}
	}

	public void addReadAccessLog(long address, long size) {
		long p_s = address / block_size;
		long p_e = (address + size) / block_size;
		long i;

		if (p_s < 0 || getBlockCount() <= p_e) {
			throw new IllegalArgumentException(
					String.format("address:%08x(block:%d-%d) is illegal.", 
							address, p_s, p_e));
		}

		synchronized(this) {
			for (i = p_s; i < p_e + 1; i++) {
				block_hist_read[(int)i] = 0xff;
			}
		}
	}

	public void addWriteAccessLog(long address, long size) {
		long p_s = address / block_size;
		long p_e = (address + size) / block_size;
		long i;

		if (p_s < 0 || getBlockCount() <= p_e) {
			throw new IllegalArgumentException(
					String.format("address:%08x(block:%d-%d) is illegal.", 
							address, p_s, p_e));
		}

		synchronized(this) {
			for (i = p_s; i < p_e + 1; i++) {
				block_hist_write[(int)i] = 0xff;
			}
		}
	}

	public void forgetHistories() {
		long i;
		int t;

		synchronized(this) {
			for (i = 0; i < getBlockCount(); i++) {
				t = block_hist_read[(int)i];
				t = Math.max(t - 10, 0);
				block_hist_read[(int)i] = t;

				t = block_hist_write[(int)i];
				t = Math.max(t - 10, 0);
				block_hist_write[(int)i] = t;
			}
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
		//お試しさん
		//addReadHistory((long)(getBlockCount() * getBlockSize() * Math.random()));
		//addWriteHistory((long)(getBlockCount() * getBlockSize() * Math.random()));

		//アクセス履歴を忘れさせる
		forgetHistories();

		//再描画する
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		Dimension d_ba;
		Rectangle r_c, r_bc;
		int mx, my, x, y, bx, by;
		long i;

		super.paint(g);

		//d_c = getAreaSize();
		r_c = getContentBounds();
		d_ba = getBlockAreaSize();
		r_bc = getBlockContentBounds();
		mx = r_c.width / d_ba.width;
		my = r_c.height / d_ba.height;
		for (i = 0; i < getBlockCount(); i++) {
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
					255 - block_hist_read[(int)i] & 0xff, 
					255 - block_hist_write[(int)i] & 0xff, 
					255));
			g.fillRect(bx + r_bc.x + 1, by + r_bc.y + 1, 
					r_bc.width - 1, r_bc.height - 1);
		}
	}
}

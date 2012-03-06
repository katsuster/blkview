
package net.katsuster.blkview;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * ストレージに保持されているアクセスログの 2D描画、表示を行うクラスです。
 * </p>
 * 
 * @author katsuhiro
 */
public class LogRendererPanel extends JPanel 
implements LogRenderer, ActionListener {
	private static final long serialVersionUID = 1L;

	//読み込みアクセスログの履歴
	private LogStorage storage_r;
	//書き込みアクセスログの履歴
	private LogStorage storage_w;

	//全体の描画領域の大きさ
	private Dimension content_area;
	//全体の描画領域
	//（全体の描画領域の左上を（0, 0）とする）
	private Rectangle content_rect;
	//ブロックの描画領域の大きさ
	private Dimension block_area;
	//ブロックを描画する領域
	//（ブロックの描画領域の左上を (0, 0) とする）
	private Rectangle block_rect;

	//履歴を徐々に忘れさせていくタイマー
	private Timer leaper;

	public LogRendererPanel(LogStorage s_r, LogStorage s_w) {
		super();

		setSize(new Dimension(640, 480));
		setPreferredSize(getSize());
		setBackground(Color.WHITE);

		setReadLogStorage(s_r);
		setWriteLogStorage(s_w);
		setBlockCount(5500);

		setAreaSize(getWidth(), getHeight());
		setContentMargin(5, 5, 5, 5);
		setBlockAreaSize(5, 9);
		setBlockContentMargin(1, 1, 1, 1);

		leaper = new Timer(100, this);
		startRendering();
	}

	@Override
	public void setReadLogStorage(LogStorage s) {
		storage_r = s;
	}

	@Override
	public void setWriteLogStorage(LogStorage s) {
		storage_w = s;
	}

	public void setBlockCount(int n) {
		storage_r.setBlockCount(n);
		storage_w.setBlockCount(n);
	}

	public Dimension getAreaSize() {
		return (Dimension)content_area.clone();
	}

	/**
	 * <p>
	 * 全体の描画領域の境界の大きさを指定します。
	 * </p>
	 * 
	 * @param width 全体の描画領域の境界の幅
	 * @param height 全体の描画領域の境界の高さ
	 */
	public void setAreaSize(int width, int height) {
		content_area = new Dimension(width, height);
	}

	/**
	 * <p>
	 * 全体の描画領域を取得します。
	 * </p>
	 * 
	 * <p>
	 * 描画開始位置は、
	 * 全体の描画領域の境界の（setAreaSize() で指定する領域）
	 * 左上を (0, 0) とした、相対座標です。
	 * </p>
	 * 
	 * @return 全体の描画領域
	 */
	public Rectangle getContentBounds() {
		return (Rectangle)content_rect.clone();
	}

	/**
	 * <p>
	 * 開始座標、幅、高さから、全体の描画領域を設定します。
	 * </p>
	 * 
	 * <p>
	 * 描画開始位置は、
	 * 全体の描画領域の境界の（setAreaSize() で指定する領域）
	 * 左上を (0, 0) とした、相対座標です。
	 * </p>
	 * 
	 * @param x 全体の描画領域の開始 X 座標
	 * @param y 全体の描画領域の開始 Y 座標
	 * @param width 全体の描画領域の幅
	 * @param height 全体の描画領域の高さ
	 */
	public void setContentBounds(int x, int y, int width, int height) {
		content_rect = new Rectangle(x, y, width, height);
	}

	/**
	 * <p>
	 * マージンの大きさから、全体の描画領域を設定します。
	 * </p>
	 * 
	 * <p>
	 * 全体の描画領域は、
	 * 全体の描画領域の境界から、マージンを除いた領域となります。
	 * </p>
	 * 
	 * @param left マージンの左側幅
	 * @param top マージンの上側高さ
	 * @param right マージンの右側幅
	 * @param bottom マージンの下側高さ
	 */
	public void setContentMargin(int left, int top, int right, int bottom) {
		Dimension d = getAreaSize();

		setContentBounds(left, top, 
				d.width - left - right, d.height - top - bottom);
	}

	/**
	 * <p>
	 * 1ブロックの描画領域の境界の大きさを取得します。
	 * </p>
	 * 
	 * @return 1ブロックの描画領域の境界の大きさ
	 */
	public Dimension getBlockAreaSize() {
		return (Dimension)block_area.clone();
	}

	/**
	 * <p>
	 * 1ブロックの描画領域の境界の大きさを取得します。
	 * </p>
	 * 
	 * @param width 1ブロックの描画領域の境界の幅
	 * @param height 1ブロックの描画領域の境界の高さ
	 */
	public void setBlockAreaSize(int width, int height) {
		block_area = new Dimension(width, height);
	}

	/**
	 * <p>
	 * 1ブロックの描画領域を取得します。
	 * </p>
	 * 
	 * <p>
	 * 描画開始位置は、
	 * 1ブロックの描画領域の境界の（setBlockAreaSize() で指定する領域）
	 * 左上を (0, 0) とした相対座標です。
	 * </p>
	 * 
	 * @return 1ブロックの描画領域
	 */
	public Rectangle getBlockContentBounds() {
		return (Rectangle)block_rect.clone();
	}

	/**
	 * <p>
	 * 開始座標、幅、高さから、1ブロックの描画領域を設定します。
	 * </p>
	 * 
	 * <p>
	 * 描画開始位置は、
	 * 1ブロックの描画領域の境界の（setBlockAreaSize() で指定する領域）
	 * 左上を (0, 0) とした相対座標です。
	 * </p>
	 * 
	 * @param x 1ブロックの描画領域の開始 X 座標
	 * @param y 1ブロックの描画領域の開始 Y 座標
	 * @param width 1ブロックの描画領域の幅
	 * @param height 1ブロックの描画領域の高さ
	 */
	public void setBlockContentBounds(int x, int y, int width, int height) {
		block_rect = new Rectangle(x, y, width, height);
	}

	public void setBlockContentMargin(int left, int top, int right, int bottom) {
		Dimension d = getBlockAreaSize();

		setBlockContentBounds(left, top, 
				d.width - left - right, d.height - top - bottom);
	}

	@Override
	public void startRendering() {
		leaper.start();
	}

	@Override
	public void stopRendering() {
		leaper.stop();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//アクセス履歴を忘れさせます
		storage_r.forgetHistories();
		storage_w.forgetHistories();

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

		hr = storage_r.getHistories();
		hw = storage_w.getHistories();
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

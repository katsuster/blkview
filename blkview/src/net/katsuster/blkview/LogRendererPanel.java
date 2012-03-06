
package net.katsuster.blkview;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * �X�g���[�W�ɕێ�����Ă���A�N�Z�X���O�� 2D�`��A�\�����s���N���X�ł��B
 * </p>
 * 
 * @author katsuhiro
 */
public class LogRendererPanel extends JPanel 
implements LogRenderer, ActionListener {
	private static final long serialVersionUID = 1L;

	//�ǂݍ��݃A�N�Z�X���O�̗���
	private LogStorage storage_r;
	//�������݃A�N�Z�X���O�̗���
	private LogStorage storage_w;

	//�S�̂̕`��̈�̑傫��
	private Dimension content_area;
	//�S�̂̕`��̈�
	//�i�S�̂̕`��̈�̍�����i0, 0�j�Ƃ���j
	private Rectangle content_rect;
	//�u���b�N�̕`��̈�̑傫��
	private Dimension block_area;
	//�u���b�N��`�悷��̈�
	//�i�u���b�N�̕`��̈�̍���� (0, 0) �Ƃ���j
	private Rectangle block_rect;

	//���������X�ɖY�ꂳ���Ă����^�C�}�[
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
	 * �S�̂̕`��̈�̋��E�̑傫�����w�肵�܂��B
	 * </p>
	 * 
	 * @param width �S�̂̕`��̈�̋��E�̕�
	 * @param height �S�̂̕`��̈�̋��E�̍���
	 */
	public void setAreaSize(int width, int height) {
		content_area = new Dimension(width, height);
	}

	/**
	 * <p>
	 * �S�̂̕`��̈���擾���܂��B
	 * </p>
	 * 
	 * <p>
	 * �`��J�n�ʒu�́A
	 * �S�̂̕`��̈�̋��E�́isetAreaSize() �Ŏw�肷��̈�j
	 * ����� (0, 0) �Ƃ����A���΍��W�ł��B
	 * </p>
	 * 
	 * @return �S�̂̕`��̈�
	 */
	public Rectangle getContentBounds() {
		return (Rectangle)content_rect.clone();
	}

	/**
	 * <p>
	 * �J�n���W�A���A��������A�S�̂̕`��̈��ݒ肵�܂��B
	 * </p>
	 * 
	 * <p>
	 * �`��J�n�ʒu�́A
	 * �S�̂̕`��̈�̋��E�́isetAreaSize() �Ŏw�肷��̈�j
	 * ����� (0, 0) �Ƃ����A���΍��W�ł��B
	 * </p>
	 * 
	 * @param x �S�̂̕`��̈�̊J�n X ���W
	 * @param y �S�̂̕`��̈�̊J�n Y ���W
	 * @param width �S�̂̕`��̈�̕�
	 * @param height �S�̂̕`��̈�̍���
	 */
	public void setContentBounds(int x, int y, int width, int height) {
		content_rect = new Rectangle(x, y, width, height);
	}

	/**
	 * <p>
	 * �}�[�W���̑傫������A�S�̂̕`��̈��ݒ肵�܂��B
	 * </p>
	 * 
	 * <p>
	 * �S�̂̕`��̈�́A
	 * �S�̂̕`��̈�̋��E����A�}�[�W�����������̈�ƂȂ�܂��B
	 * </p>
	 * 
	 * @param left �}�[�W���̍�����
	 * @param top �}�[�W���̏㑤����
	 * @param right �}�[�W���̉E����
	 * @param bottom �}�[�W���̉�������
	 */
	public void setContentMargin(int left, int top, int right, int bottom) {
		Dimension d = getAreaSize();

		setContentBounds(left, top, 
				d.width - left - right, d.height - top - bottom);
	}

	/**
	 * <p>
	 * 1�u���b�N�̕`��̈�̋��E�̑傫�����擾���܂��B
	 * </p>
	 * 
	 * @return 1�u���b�N�̕`��̈�̋��E�̑傫��
	 */
	public Dimension getBlockAreaSize() {
		return (Dimension)block_area.clone();
	}

	/**
	 * <p>
	 * 1�u���b�N�̕`��̈�̋��E�̑傫�����擾���܂��B
	 * </p>
	 * 
	 * @param width 1�u���b�N�̕`��̈�̋��E�̕�
	 * @param height 1�u���b�N�̕`��̈�̋��E�̍���
	 */
	public void setBlockAreaSize(int width, int height) {
		block_area = new Dimension(width, height);
	}

	/**
	 * <p>
	 * 1�u���b�N�̕`��̈���擾���܂��B
	 * </p>
	 * 
	 * <p>
	 * �`��J�n�ʒu�́A
	 * 1�u���b�N�̕`��̈�̋��E�́isetBlockAreaSize() �Ŏw�肷��̈�j
	 * ����� (0, 0) �Ƃ������΍��W�ł��B
	 * </p>
	 * 
	 * @return 1�u���b�N�̕`��̈�
	 */
	public Rectangle getBlockContentBounds() {
		return (Rectangle)block_rect.clone();
	}

	/**
	 * <p>
	 * �J�n���W�A���A��������A1�u���b�N�̕`��̈��ݒ肵�܂��B
	 * </p>
	 * 
	 * <p>
	 * �`��J�n�ʒu�́A
	 * 1�u���b�N�̕`��̈�̋��E�́isetBlockAreaSize() �Ŏw�肷��̈�j
	 * ����� (0, 0) �Ƃ������΍��W�ł��B
	 * </p>
	 * 
	 * @param x 1�u���b�N�̕`��̈�̊J�n X ���W
	 * @param y 1�u���b�N�̕`��̈�̊J�n Y ���W
	 * @param width 1�u���b�N�̕`��̈�̕�
	 * @param height 1�u���b�N�̕`��̈�̍���
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
		//�A�N�Z�X������Y�ꂳ���܂�
		storage_r.forgetHistories();
		storage_w.forgetHistories();

		//�ĕ`�悵�܂�
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
			//�X�V���Ǝv���邽�߁A�������Ȃ�
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
				//�`��̈�O�܂ōs�����̂ł����`��̕K�v�͂Ȃ�
				break;
			}

			bx = r_c.x + x * d_ba.width;
			by = r_c.y + y * d_ba.height;

			//�g
			g.setColor(Color.GRAY);
			g.drawRect(bx + r_bc.x, by + r_bc.y, 
					r_bc.width, r_bc.height);
			//���g
			g.setColor(new Color(
					255 - hr[i] & 0xff, 
					255 - hw[i] & 0xff, 
					255));
			g.fillRect(bx + r_bc.x + 1, by + r_bc.y + 1, 
					r_bc.width - 1, r_bc.height - 1);
		}
	}
}

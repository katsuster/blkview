
package net.katsuster.blkview;

/**
 * <p>
 * �A�N�Z�X���O�̗�����ێ�����X�g���[�W�N���X�B
 * �����̓u���b�N���Ƃɕێ�����܂��B
 * </p>
 * 
 * <p>
 * �C���X�^���X�������� setBlockCount() �Ńu���b�N�����A
 * setCapacity() �őS�̗̂e�ʂ��w�肵�܂��B
 * 1 �u���b�N�̑傫���� (�S�̗̂e��) / (�u���b�N��) �ɐݒ肳��A
 * getBlockSize() �Ŏ擾�\�ł��B
 * </p>
 * 
 * <p>
 * �T�^�I�Ȏg�����͉��L�̂Ƃ���ł��B
 * </p>
 * 
 * <ul>
 * <li>addAccessLog() �ɂăA�N�Z�X���O��ǉ����܂��B</li>
 * <li>forgetAccessLog() �����I�ɌĂяo�����ƂŁA
 * �A�N�Z�X���O�����X�ɖY�ꂳ���܂��B</li>
 * </p>
 * 
 * @author katsuhiro
 */
public class LogHistories {
	//�S�̗̂e��
	private long capacity, capacity_real;
	//�u���b�N�̐�
	private int block_count, block_count_real;
	//�u���b�N�̃o�C�g��
	private long block_size_real;
	//�u���b�N�̓ǂݍ��݃A�N�Z�X����
	private int[] block_hist;

	/**
	 * <p>
	 * �u���b�N�� 1�A�e�� 1 �o�C�g�̃A�N�Z�X���O�������쐬���܂��B
	 * </p>
	 */
	public LogHistories() {
		this(1, 1);
	}

	/**
	 * <p>
	 * �w�肳�ꂽ�u���b�N���A�e�ʂ̃A�N�Z�X���O�������쐬���܂��B
	 * </p>
	 * 
	 * @param count �u���b�N��
	 * @param cap �e��
	 */
	public LogHistories(int count, long cap) {
		setBlockCount(count);
		setCapacity(cap);
	}

	/**
	 * <p>
	 * �S�̗̂e�ʁi�o�C�g���j���擾���܂��B
	 * </p>
	 * 
	 * @return �S�̗̂e��
	 */
	public long getCapacity() {
		return capacity_real;
	}

	/**
	 * <p>
	 * �S�̗̂e�ʁi�o�C�g���j��ݒ肵�܂��B
	 * </p>
	 * 
	 * @param n �S�̗̂e��
	 */
	public void setCapacity(long n) {
		if (n < 0) {
			throw new IllegalArgumentException(
					"capacity(" + n + ") is negative.");
		}

		capacity = n;
		resizeHistories();
	}

	/**
	 * <p>
	 * �u���b�N�����擾���܂��B
	 * </p>
	 * 
	 * @return �u���b�N��
	 */
	public int getBlockCount() {
		return block_count_real;
	}

	/**
	 * <p>
	 * �u���b�N����ݒ肵�܂��B
	 * </p>
	 * 
	 * @param n �u���b�N��
	 */
	public void setBlockCount(int n) {
		if (n < 0) {
			throw new IllegalArgumentException(
					"block count(" + n + ") is negative.");
		}

		block_count = n;
		resizeHistories();
	}

	/**
	 * <p>
	 * 1�u���b�N�̃T�C�Y���擾���܂��B
	 * </p>
	 * 
	 * @return 1�u���b�N�̃T�C�Y
	 */
	public long getBlockSize() {
		return block_size_real;
	}

	/**
	 * <p>
	 * ���݂̗e�ʁA�u���b�N�������A�N�Z�X���O�������č쐬���܂��B
	 * </p>
	 */
	protected void resizeHistories() {
		synchronized(this) {
			capacity_real = capacity;
			block_count_real = block_count;
			block_size_real = getCapacity() / getBlockCount() + 1;
			block_hist = new int[getBlockCount()];
		}
	}

	/**
	 * <p>
	 * �A�N�Z�X���O�𗚗��ɒǉ����܂��B
	 * </p>
	 * 
	 * @param address �A�N�Z�X��̃A�h���X
	 * @param size �A�N�Z�X�����T�C�Y
	 */
	public void addAccessLog(long address, long size) {
		long p_s, p_e;
		long i;

		synchronized(this) {
			p_s = address / getBlockSize();
			p_e = (address + size) / getBlockSize();

			if (p_s < 0 || getBlockCount() <= p_e) {
				throw new IllegalArgumentException(
						String.format("address:%08x(block:%d-%d) is illegal.", 
								address, p_s, p_e));
			}

			for (i = p_s; i < p_e + 1; i++) {
				block_hist[(int)i] = 0xff;
			}
		}
	}

	/**
	 * <p>
	 * �A�N�Z�X���O�̗������擾���܂��B
	 * </p>
	 * 
	 * @return �A�N�Z�X���O�̗���
	 */
	public int[] getHistories() {
		return block_hist;
	}

	/**
	 * <p>
	 * �A�N�Z�X���O�̗�����Y�ꂳ���܂��B
	 * </p>
	 */
	public void forgetHistories() {
		long i;
		int t;

		synchronized(this) {
			for (i = 0; i < getBlockCount(); i++) {
				t = block_hist[(int)i];
				t = Math.max(t - 10, 0);
				block_hist[(int)i] = t;
			}
		}
	}
}

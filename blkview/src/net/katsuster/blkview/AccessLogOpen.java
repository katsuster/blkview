
package net.katsuster.blkview;

import java.io.*;

/**
 * <p>
 * �A�N�Z�X���O�̊J�n������킷�N���X�B
 * </p>
 * 
 * @author katsuhiro
 */
public class AccessLogOpen extends AccessLog {
	//�u���b�N�f�o�C�X�̗e��
	private long capacity;

	public AccessLogOpen() {
		super();

		//do nothing
	}

	/**
	 * <p>
	 * �u���b�N�f�o�C�X�̗e�ʂ��擾����B
	 * </p>
	 * 
	 * @return �e��
	 */
	public long getCapacity() {
		return capacity;
	}

	@Override
	public void read(DataInputStream in) {
		read(in, this);
	}

	/**
	 * <p>
	 * �o�C�g�X�g���[�����烍�O��ǂݍ��݁A
	 * �A�N�Z�X���O�̊J�n�ɐݒ肷��B
	 * </p>
	 * 
	 * @param in �o�C�g�X�g���[��
	 * @param d �ǂݍ��񂾒l�̐ݒ��ƂȂ�A�N�Z�X���O
	 */
	public static void read(DataInputStream in, AccessLogOpen d) {
		AccessLog.read(in, d);

		try {
			d.capacity = in.readLong();
		} catch (IOException ex) {
			throw new IllegalStateException("I/O error.");
		}
	}
}

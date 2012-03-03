
package net.katsuster.blkview;

import java.io.*;

/**
 * <p>
 * Read/Write �̃A�N�Z�X���O������킷�N���X�ł��B
 * </p>
 * 
 * @author katsuhiro
 */
public class AccessLogRW extends AccessLog {
	//Read/Write ��̃A�h���X
	private long address;
	//Read/Write �̃T�C�Y
	private long size;

	public AccessLogRW() {
		super();

		//do nothing
	}

	/**
	 * <p>
	 * Read/Write �̃A�h���X���擾���܂��B
	 * </p>
	 * 
	 * @return �A�h���X
	 */
	public long getAddress() {
		return address;
	}

	/**
	 * <p>
	 * Read/Write �̃T�C�Y���擾���܂��B
	 * </p>
	 * 
	 * @return �T�C�Y
	 */
	public long getSize() {
		return size;
	}

	@Override
	public void read(DataInputStream in) {
		read(in, this);
	}

	/**
	 * <p>
	 * �o�C�g�X�g���[�����烍�O��ǂݍ��݁A
	 * Read/Write �̃A�N�Z�X���O�ɐݒ肵�܂��B
	 * </p>
	 * 
	 * @param in �o�C�g�X�g���[��
	 * @param d �ǂݍ��񂾒l�̐ݒ��ƂȂ�A�N�Z�X���O
	 */
	public static void read(DataInputStream in, AccessLogRW d) {
		AccessLog.read(in, d);

		try {
			d.address = in.readLong();
			d.size = in.readLong();
		} catch (IOException ex) {
			throw new IllegalStateException("I/O error.");
		}
	}
}

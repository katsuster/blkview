
package net.katsuster.blkview;

import java.io.*;

/**
 * <p>
 * �A�N�Z�X���O������킷�N���X�ł��B
 * </p>
 * 
 * @author katsuhiro
 */
public class AccessLog {
	//�A�N�Z�X���O�̎�ށiLogType �N���X���Q�Ƃ̂��Ɓj
	private int op;
	
	public AccessLog() {
		//do nothing
	}
	
	/**
	 * <p>
	 * ���O�̎�ނ��擾���܂��B
	 * </p>
	 * 
	 * @return ���O�̎��
	 */
	public int getOp() {
		return op;
	}
	
	/**
	 * <p>
	 * �o�C�g�X�g���[�����烍�O��ǂݍ��ށB
	 * </p>
	 * 
	 * @param in �o�C�g�X�g���[��
	 */
	public void read(DataInputStream in) {
		read(in, this);
	}
	
	/**
	 * <p>
	 * �o�C�g�X�g���[�����烍�O��ǂݍ��݁A
	 * �A�N�Z�X���O�ɐݒ肵�܂��B
	 * </p>
	 * 
	 * @param in �o�C�g�X�g���[��
	 * @param d �ǂݍ��񂾒l�̐ݒ��ƂȂ�A�N�Z�X���O
	 */
	public static void read(DataInputStream in, AccessLog d) {
		try {
			d.op = in.readInt();
		} catch (IOException ex) {
			throw new IllegalStateException("I/O error.");
		}
	}
	
	/**
	 * <p>
	 * �A�N�Z�X���O�̃t�@�N�g���֐��B
	 * </p>
	 * 
	 * <p>
	 * �A�N�Z�X���O�̎�ނɉ����ēK�؂ȃA�N�Z�X���O
	 * �i�p���N���X�̃C���X�^���X�j���쐬���܂��B
	 * </p>
	 * 
	 * @param h �A�N�Z�X���O
	 * @return �A�N�Z�X���O�̎�ނɉ������p���N���X�̃C���X�^���X
	 */
	public static AccessLog makeAccessLog(AccessLog h) {
		AccessLog result;
		
		switch (h.getOp()) {
		case LogType.OPEN:
			result = new AccessLogOpen();
			break;
		case LogType.READ:
		case LogType.WRITE:
			result = new AccessLogRW();
			break;
		default:
			result = new AccessLog();
			break;
		}
		
		return result;
	}
	
	/**
	 * <p>
	 * ���O�̎�ނ�����킷�N���X�B
	 * </p>
	 * 
	 * @author katsuhiro
	 */
	public class LogType {
		public static final int OPEN = 1;
		public static final int READ = 10;
		public static final int WRITE = 20;
	}
}

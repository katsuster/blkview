
package net.katsuster.blkview;

/**
 * <p>
 * �X�g���[�W�ɕێ�����Ă���A�N�Z�X���O�̕\�����s���C���^�t�F�[�X�ł��B
 * </p>
 * 
 * @author katsuhiro
 */
public interface LogRenderer {
	public void setBlockCount(int n);
	public void setCapacity(long n);

	public void addAccessLog(AccessLogRW log);
}

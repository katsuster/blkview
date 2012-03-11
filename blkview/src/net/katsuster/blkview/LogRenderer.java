
package net.katsuster.blkview;

/**
 * <p>
 * �X�g���[�W�ɕێ�����Ă���A�N�Z�X���O�̕\�����s���C���^�t�F�[�X�ł��B
 * </p>
 * 
 * @author katsuhiro
 */
public interface LogRenderer {
	public void setReadLogStorage(LogStorage s);
	public void setWriteLogStorage(LogStorage s);
	
	public void startRendering();
	public void stopRendering();
}

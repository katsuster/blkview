
package net.katsuster.blkview;

/**
 * <p>
 * ストレージに保持されているアクセスログの表示を行うインタフェースです。
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

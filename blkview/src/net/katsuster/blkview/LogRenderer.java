
package net.katsuster.blkview;

/**
 * <p>
 * ストレージに保持されているアクセスログの表示を行うインタフェースです。
 * </p>
 * 
 * @author katsuhiro
 */
public interface LogRenderer {
	public void setBlockCount(int n);
	public void setCapacity(long n);

	public void addAccessLog(AccessLogRW log);
}

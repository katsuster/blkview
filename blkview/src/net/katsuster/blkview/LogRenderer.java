
package net.katsuster.blkview;

/**
 * @author katsuhiro
 */
public interface LogRenderer {
	public void setBlockCount(int n);
	public void setCapacity(long n);

	public void addAccessLog(AccessLogRW log);
}


package net.katsuster.blkview;

/**
 * @author katsuhiro
 */
public interface LogRenderer {
	public void setCapacity(long n);

	public void addAccessLog(AccessLogRW log);
}

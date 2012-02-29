/**
 * 
 */
package net.katsuster.blkview;

/**
 * @author katsuhiro
 */
public interface HistoryReceiver {
	public void setCapacity(long n);
	
	public void addReadHistory(long address);
	public void addWriteHistory(long address);
}

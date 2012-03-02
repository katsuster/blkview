/**
 * 
 */
package net.katsuster.blkview;

import java.io.*;

/**
 * <p>
 * Read/Write のアクセスログをあらわすクラス。
 * </p>
 * 
 * @author katsuhiro
 */
public class AccessLogRW extends AccessLog {
	private long address;
	private long size;

	public AccessLogRW() {
		super();

		//do nothing
	}

	public long getAddress() {
		return address;
	}

	public long getSize() {
		return size;
	}

	public void read(DataInputStream in) {
		read(in, this);
	}

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

/**
 * 
 */
package net.katsuster.blkview;

import java.io.*;

/**
 * <p>
 * アクセスログをあらわすクラス。
 * </p>
 * 
 * @author katsuhiro
 */
public class AccessLog {
	private int op;

	public AccessLog() {
		//do nothing
	}

	public int getOp() {
		return op;
	}

	public void read(DataInputStream in) {
		read(in, this);
	}

	public static void read(DataInputStream in, AccessLog d) {
		try {
			d.op = in.readInt();
		} catch (IOException ex) {
			throw new IllegalStateException("I/O error.");
		}
	}

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

	public class LogType {
		public static final int OPEN = 1;
		public static final int READ = 10;
		public static final int WRITE = 20;
	}
}

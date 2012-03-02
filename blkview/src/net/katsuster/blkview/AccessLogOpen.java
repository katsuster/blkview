/**
 * 
 */
package net.katsuster.blkview;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * <p>
 * �A�N�Z�X���O�̊J�n������킷�N���X�B
 * </p>
 * 
 * @author katsuhiro
 */
public class AccessLogOpen extends AccessLog {
	private long capacity;

	public AccessLogOpen() {
		super();

		//do nothing
	}

	public long getCapacity() {
		return capacity;
	}

	public void read(DataInputStream in) {
		read(in, this);
	}

	public static void read(DataInputStream in, AccessLogOpen d) {
		AccessLog.read(in, d);

		try {
			d.capacity = in.readLong();
		} catch (IOException ex) {
			throw new IllegalStateException("I/O error.");
		}
	}
}

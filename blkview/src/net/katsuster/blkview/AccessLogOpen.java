
package net.katsuster.blkview;

import java.io.*;

/**
 * <p>
 * アクセスログの開始をあらわすクラス。
 * </p>
 * 
 * @author katsuhiro
 */
public class AccessLogOpen extends AccessLog {
	//ブロックデバイスの容量
	private long capacity;

	public AccessLogOpen() {
		super();

		//do nothing
	}

	/**
	 * <p>
	 * ブロックデバイスの容量を取得する。
	 * </p>
	 * 
	 * @return 容量
	 */
	public long getCapacity() {
		return capacity;
	}

	@Override
	public void read(DataInputStream in) {
		read(in, this);
	}

	/**
	 * <p>
	 * バイトストリームからログを読み込み、
	 * アクセスログの開始に設定する。
	 * </p>
	 * 
	 * @param in バイトストリーム
	 * @param d 読み込んだ値の設定先となるアクセスログ
	 */
	public static void read(DataInputStream in, AccessLogOpen d) {
		AccessLog.read(in, d);

		try {
			d.capacity = in.readLong();
		} catch (IOException ex) {
			throw new IllegalStateException("I/O error.");
		}
	}
}

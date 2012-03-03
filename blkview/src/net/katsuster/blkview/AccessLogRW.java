
package net.katsuster.blkview;

import java.io.*;

/**
 * <p>
 * Read/Write のアクセスログをあらわすクラスです。
 * </p>
 * 
 * @author katsuhiro
 */
public class AccessLogRW extends AccessLog {
	//Read/Write 先のアドレス
	private long address;
	//Read/Write のサイズ
	private long size;

	public AccessLogRW() {
		super();

		//do nothing
	}

	/**
	 * <p>
	 * Read/Write のアドレスを取得します。
	 * </p>
	 * 
	 * @return アドレス
	 */
	public long getAddress() {
		return address;
	}

	/**
	 * <p>
	 * Read/Write のサイズを取得します。
	 * </p>
	 * 
	 * @return サイズ
	 */
	public long getSize() {
		return size;
	}

	@Override
	public void read(DataInputStream in) {
		read(in, this);
	}

	/**
	 * <p>
	 * バイトストリームからログを読み込み、
	 * Read/Write のアクセスログに設定します。
	 * </p>
	 * 
	 * @param in バイトストリーム
	 * @param d 読み込んだ値の設定先となるアクセスログ
	 */
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

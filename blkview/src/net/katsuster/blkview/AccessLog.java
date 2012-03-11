
package net.katsuster.blkview;

import java.io.*;

/**
 * <p>
 * アクセスログをあらわすクラスです。
 * </p>
 * 
 * @author katsuhiro
 */
public class AccessLog {
	//アクセスログの種類（LogType クラスを参照のこと）
	private int op;
	
	public AccessLog() {
		//do nothing
	}
	
	/**
	 * <p>
	 * ログの種類を取得します。
	 * </p>
	 * 
	 * @return ログの種類
	 */
	public int getOp() {
		return op;
	}
	
	/**
	 * <p>
	 * バイトストリームからログを読み込む。
	 * </p>
	 * 
	 * @param in バイトストリーム
	 */
	public void read(DataInputStream in) {
		read(in, this);
	}
	
	/**
	 * <p>
	 * バイトストリームからログを読み込み、
	 * アクセスログに設定します。
	 * </p>
	 * 
	 * @param in バイトストリーム
	 * @param d 読み込んだ値の設定先となるアクセスログ
	 */
	public static void read(DataInputStream in, AccessLog d) {
		try {
			d.op = in.readInt();
		} catch (IOException ex) {
			throw new IllegalStateException("I/O error.");
		}
	}
	
	/**
	 * <p>
	 * アクセスログのファクトリ関数。
	 * </p>
	 * 
	 * <p>
	 * アクセスログの種類に応じて適切なアクセスログ
	 * （継承クラスのインスタンス）を作成します。
	 * </p>
	 * 
	 * @param h アクセスログ
	 * @return アクセスログの種類に応じた継承クラスのインスタンス
	 */
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
	
	/**
	 * <p>
	 * ログの種類をあらわすクラス。
	 * </p>
	 * 
	 * @author katsuhiro
	 */
	public class LogType {
		public static final int OPEN = 1;
		public static final int READ = 10;
		public static final int WRITE = 20;
	}
}


package net.katsuster.blkview;

import java.io.*;
import java.net.*;

import net.katsuster.blkview.AccessLog.LogType;

/**
 * <p>
 * アクセスログを読み取り、ストレージに送るクラス。
 * 独自のスレッドで動作します。
 * </p>
 * 
 * @author katsuhiro
 */
public class LogReader implements Runnable {
	//ログファイルのパス
	private String path;
	//Read アクセスログの履歴を保存するストレージ
	private LogStorage storage_r;
	//Write アクセスログの履歴を保存するストレージ
	private LogStorage storage_w;

	public LogReader(String p, LogStorage s_r, LogStorage s_w, LogRenderer r) {
		path = p;
		storage_r = s_r;
		storage_w = s_w;
	}

	public String getPath() {
		return path;
	}

	public LogStorage getReadLogStorage() {
		return storage_r;
	}

	public LogStorage getWriteLogStorage() {
		return storage_w;
	}

	@Override
	public void run() {
		try {
			run_safe();
		} catch (IOException ex) {
			System.err.println(ex);
			ex.printStackTrace(System.err);
		}
	}

	/**
	 * <p>
	 * IOException のキャッチをさぼるための run() メソッド。
	 * </p>
	 * 
	 * @throws IOException
	 */
	private void run_safe() throws IOException {
		ServerSocket ss;

		try {
			ss = new ServerSocket(10001, 5);
		} catch (IOException ex) {
			throw new IllegalArgumentException(
					"cannot bind to port '10001'.");
		}/**/

		while (true) {
			try {
				acceptClient(ss);
			} catch (IllegalStateException ex) {
				System.err.println(ex);
				ex.printStackTrace(System.err);
				break;
			}
		}
	}

	private void acceptClient(ServerSocket ss) {
		Socket s;
		InputStream str;
		DataInputStream in;
		AccessLogOpen logh = new AccessLogOpen();

		try {
			s = ss.accept();
			str = s.getInputStream();
			System.out.println("accepted.");
		} catch (IOException ex) {
			throw new IllegalStateException(
					"cannot accept.");
		}/**/

		/* try {
			str = new FileInputStream(path);
		} catch (IOException ex) {
			throw new IllegalArgumentException(
					"cannot access to '" + path + "'.");
		}/**/

		in = new DataInputStream(new BufferedInputStream(str));

		logh.read(in);
		storage_r.setCapacity(logh.getCapacity());
		storage_w.setCapacity(logh.getCapacity());

		System.out.println(
				"capacity " + (logh.getCapacity() / 1048576) + "MB" + 
						"(" + logh.getCapacity() + ")");

		while (true) {
			try {
				receiveLog(in);
			} catch (IllegalStateException ex) {
				System.err.println(ex);
				ex.printStackTrace(System.err);
				break;
			}
		}
	}

	private void receiveLog(DataInputStream in) {
		AccessLogRW log = new AccessLogRW();

		try {
			log.read(in);
		} catch (IllegalStateException ex) {
			throw new IllegalStateException(
					"cannot receive logs.");
		}

		switch (log.getOp()) {
		case LogType.READ:
			//read
			storage_r.addAccessLog(log.getAddress(), log.getSize());
			break;
		case LogType.WRITE:
			//write
			storage_w.addAccessLog(log.getAddress(), log.getSize());
			break;
		}
	}
}


package net.katsuster.blkview;

import java.io.*;
import java.net.*;

/**
 * <p>
 * アクセスログを読み取り、ストレージに送るクラス。
 * 独自のスレッドで動作します。
 * </p>
 * 
 * @author katsuhiro
 */
public class LogReader implements Runnable {
	//private String path;
	private LogRenderer renderer;

	public LogReader(String p, LogRenderer r) {
		//path = p;
		renderer = r;
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
	public void run_safe() throws IOException {
		ServerSocket ss;
		Socket s;
		InputStream str;
		DataInputStream in;
		AccessLogOpen logh = new AccessLogOpen();
		AccessLogRW log = new AccessLogRW();

		try {
			ss = new ServerSocket(10001, 5);
		} catch (IOException ex) {
			throw new IllegalArgumentException(
					"cannot bind to port '10001'.");
		}/**/

		while (true) {
			try {
				s = ss.accept();
				str = s.getInputStream();
				System.out.println("accepted.");
			} catch (IOException ex) {
				throw new IllegalArgumentException(
						"cannot accept.");
			}/**/

			/*try {
				str = new FileInputStream(path);
			} catch (IOException ex) {
				throw new IllegalArgumentException(
						"cannot access to '" + path + "'.");
			}/**/

			in = new DataInputStream(new BufferedInputStream(str));

			logh.read(in);
			renderer.setCapacity(logh.getCapacity());
			System.out.println(
					"capacity " + (logh.getCapacity() / 1048576) + "MB" + 
							"(" + logh.getCapacity() + ")");

			while (true) {
				try {
					log.read(in);
				} catch (IllegalStateException ex) {
					break;
				}

				renderer.addAccessLog(log);
			}
		}
	}
}

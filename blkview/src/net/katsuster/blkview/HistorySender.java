/**
 * 
 */
package net.katsuster.blkview;

import java.io.*;
import java.net.*;

/**
 * @author katsuhiro
 */
public class HistorySender implements Runnable {
	String path;
	private HistoryReceiver receiver;

	public HistorySender(String p, HistoryReceiver r) {
		path = p;
		receiver = r;
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
			receiver.setCapacity(logh.getCapacity());
			System.out.println(
					"capacity " + (logh.getCapacity() / 1048576) + "MB" + 
							"(" + logh.getCapacity() + ")");

			while (true) {
				try {
					log.read(in);
				} catch (IllegalStateException ex) {
					break;
				}

				receiver.addAccessLog(log);
			}
		}
	}
}

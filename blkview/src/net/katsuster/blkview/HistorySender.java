/**
 * 
 */
package net.katsuster.blkview;

import java.io.*;
import java.net.*;

/**
 * @author katsuhiro
 *
 */
public class HistorySender implements Runnable {
	private DataInputStream in;
	private HistoryReceiver receiver;
	
	public HistorySender(String path, HistoryReceiver r) {
		ServerSocket ss;
		Socket s;
		InputStream str;
		
		try {
			ss = new ServerSocket(10001, 5);
			s = ss.accept();
			str = s.getInputStream();
		} catch (IOException ex) {
			throw new IllegalArgumentException(
					"cannot bind to port '10001'.");
		}/**/
		
		/*try {
			str = new FileInputStream(path);
		} catch (IOException ex) {
			throw new IllegalArgumentException(
					"cannot access to '" + path + "'.");
		}/**/
		
		in = new DataInputStream(new BufferedInputStream(str));
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
		int op;
		long capacity, address;
		
		capacity = in.readLong();
		receiver.setCapacity(capacity);
		
		while (true) {
			try {
				op = in.readInt();
				address = in.readLong();
			} catch (EOFException ex) {
				break;
			}
			
			switch (op) {
			case 1:
				//read
				receiver.addReadHistory(address);
				break;
			case 2:
				//write
				receiver.addWriteHistory(address);
				break;
			}
		}
	}
}

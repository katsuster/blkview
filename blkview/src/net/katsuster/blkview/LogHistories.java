
package net.katsuster.blkview;

/**
 * <p>
 * アクセスログの履歴を保持するストレージクラス。
 * 履歴はブロックごとに保持されます。
 * </p>
 * 
 * <p>
 * インスタンス生成時に setBlockCount() でブロック数を、
 * setCapacity() で全体の容量を指定します。
 * 1 ブロックの大きさは (全体の容量) / (ブロック数) に設定され、
 * getBlockSize() で取得可能です。
 * </p>
 * 
 * <p>
 * 典型的な使い方は下記のとおりです。
 * </p>
 * 
 * <ul>
 * <li>addAccessLog() にてアクセスログを追加します。</li>
 * <li>forgetAccessLog() を定期的に呼び出すことで、
 * アクセスログを徐々に忘れさせます。</li>
 * </p>
 * 
 * @author katsuhiro
 */
public class LogHistories {
	//全体の容量
	private long capacity, capacity_real;
	//ブロックの数
	private int block_count, block_count_real;
	//ブロックのバイト数
	private long block_size_real;
	//ブロックの読み込みアクセス履歴
	private int[] block_hist;

	/**
	 * <p>
	 * ブロック数 1、容量 1 バイトのアクセスログ履歴を作成します。
	 * </p>
	 */
	public LogHistories() {
		this(1, 1);
	}

	/**
	 * <p>
	 * 指定されたブロック数、容量のアクセスログ履歴を作成します。
	 * </p>
	 * 
	 * @param count ブロック数
	 * @param cap 容量
	 */
	public LogHistories(int count, long cap) {
		setBlockCount(count);
		setCapacity(cap);
	}

	/**
	 * <p>
	 * 全体の容量（バイト数）を取得します。
	 * </p>
	 * 
	 * @return 全体の容量
	 */
	public long getCapacity() {
		return capacity_real;
	}

	/**
	 * <p>
	 * 全体の容量（バイト数）を設定します。
	 * </p>
	 * 
	 * @param n 全体の容量
	 */
	public void setCapacity(long n) {
		if (n < 0) {
			throw new IllegalArgumentException(
					"capacity(" + n + ") is negative.");
		}

		capacity = n;
		resizeHistories();
	}

	/**
	 * <p>
	 * ブロック数を取得します。
	 * </p>
	 * 
	 * @return ブロック数
	 */
	public int getBlockCount() {
		return block_count_real;
	}

	/**
	 * <p>
	 * ブロック数を設定します。
	 * </p>
	 * 
	 * @param n ブロック数
	 */
	public void setBlockCount(int n) {
		if (n < 0) {
			throw new IllegalArgumentException(
					"block count(" + n + ") is negative.");
		}

		block_count = n;
		resizeHistories();
	}

	/**
	 * <p>
	 * 1ブロックのサイズを取得します。
	 * </p>
	 * 
	 * @return 1ブロックのサイズ
	 */
	public long getBlockSize() {
		return block_size_real;
	}

	/**
	 * <p>
	 * 現在の容量、ブロック数を持つアクセスログ履歴を再作成します。
	 * </p>
	 */
	protected void resizeHistories() {
		synchronized(this) {
			capacity_real = capacity;
			block_count_real = block_count;
			block_size_real = getCapacity() / getBlockCount() + 1;
			block_hist = new int[getBlockCount()];
		}
	}

	/**
	 * <p>
	 * アクセスログを履歴に追加します。
	 * </p>
	 * 
	 * @param address アクセス先のアドレス
	 * @param size アクセスしたサイズ
	 */
	public void addAccessLog(long address, long size) {
		long p_s, p_e;
		long i;

		synchronized(this) {
			p_s = address / getBlockSize();
			p_e = (address + size) / getBlockSize();

			if (p_s < 0 || getBlockCount() <= p_e) {
				throw new IllegalArgumentException(
						String.format("address:%08x(block:%d-%d) is illegal.", 
								address, p_s, p_e));
			}

			for (i = p_s; i < p_e + 1; i++) {
				block_hist[(int)i] = 0xff;
			}
		}
	}

	/**
	 * <p>
	 * アクセスログの履歴を取得します。
	 * </p>
	 * 
	 * @return アクセスログの履歴
	 */
	public int[] getHistories() {
		return block_hist;
	}

	/**
	 * <p>
	 * アクセスログの履歴を忘れさせます。
	 * </p>
	 */
	public void forgetHistories() {
		long i;
		int t;

		synchronized(this) {
			for (i = 0; i < getBlockCount(); i++) {
				t = block_hist[(int)i];
				t = Math.max(t - 10, 0);
				block_hist[(int)i] = t;
			}
		}
	}
}

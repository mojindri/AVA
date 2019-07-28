package com.caprica.ava.db.bgl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import com.caprica.ava.BglInitiOperationTiming;
import com.caprica.ava.charset.CharsetDetector;
import com.caprica.ava.charset.CharsetMatch;
import com.caprica.ava.model.Index;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class OptimizeBgl extends BaseBgl {
	private BglWords words;
	private Context context;
	private InitAsync task;
	private static final String APP_DIRECTORY = Environment
			.getExternalStorageDirectory() + "/ava";
	private static final int BLOCK_SIZE = 1024 * 1024 * 1;

	private BglInitiOperationTiming handler;

	public BglWords getWords() {

		return words;
	}

	public BglInitiOperationTiming getInitHandler() {
		return handler;
	}

	public static Cursor getQueryCursor(BglWords words, Context contex,
			String word, String dbname) {
		Cursor c = null;
		if ("".equals(word) || word == null)
			c = words.queryAll();
		else
			c = words.queryLike(word);
		return c;
	}

	public   XDictEntry getEntry(int posex) {
		XDictEntry x = null;

		try {
			System.gc();

			System.gc();
			File f = new File(getWordsFilePath());
			if (!f.exists())
				return null;

			InputStream iss = new FileInputStream(f);
			System.gc();
			iss.skip(posex);
			BglBlock block = BglBlock.read(iss);
			iss.close();
			iss = null;
			System.gc();
			x = BglBlock.ParseEntry(block, getSrcEncoding(), getDstEncoding());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return x;
	}

	public   Index getIndex(Context context, String word, String dbname) {
		Index ind = null;

		BglWords words = new BglWords(context);
		words.setDATABASE_NAME(dbname);
		words.open();
		String[] x = words.queryObject(word);
		words.close();
		if (x == null)
			return null;
		ind = new Index(x[0], x[1]);
		return ind;

	}

	public OptimizeBgl(Context context, String filepath, String title) {
		this.Title = title;
		this.context = context;
		this.words = new BglWords(context, filepath);
		this.filePath = filepath;
	}

	public OptimizeBgl(BaseBgl bb, Context context) {
		super(bb.Id, bb.Author, bb.Title, bb.DstLng, bb.SrcLng, bb.DstEncName,
				bb.DstEnc, bb.SrcEncName, bb.SrcEnc, bb.DefCharset,
				bb.filePath, bb.about, bb.isNew);
		this.words = new BglWords(context, bb.filePath);
		this.context = context;

		setSrcEncoding(getSrcEnc());
		setDstEncoding(getDstEnc());
	}

	public void setInitHandler(BglInitiOperationTiming handler) {
		this.handler = handler;
	}

	public void startInitAsync() {
		if (task == null) {
			task = new InitAsync();
			task.execute();
			
		}
	}

	public void cancelInitAsync() {
		if (task != null) {
			task.cancel(true);
			task = null;
		}
	}

	private static boolean startWith(byte[] first, byte[] magic_bytes,
			int offset) {
		int len = magic_bytes.length;
		for (int i = 0; i < len; i++) {
			if (first[i + offset] != magic_bytes[i]) {
				return false;
			}
		}

		return true;
	}

	public static int getGzipheader(InputStream originalfile)
			throws IOException {
		byte[] buf = new byte[6];
		int posv = originalfile.read(buf, 0, buf.length);

		// First four bytes: BGL signature 0x12340001 or 0x12340002 (big-endian)
		if (posv < 6
				|| (buf[0] == 0x12 && buf[1] == 0x34 && buf[2] == 0x00 && (buf[4] == 0x01 || buf[4] == 0x02))) {
			return -1;
		}

		int gzipHeaderPos = buf[4] << 8 | buf[5];
		if (gzipHeaderPos < 6) {
			// System.err.println("No gzip ptr");
			return -1;
		}
		return gzipHeaderPos;
	}

	private static int getGzipSize(byte[] b) {
		int b4 = b[b.length - 4] < 0 ? 0xFF & b[b.length - 4] : b[b.length - 4];
		int b3 = b[b.length - 3] < 0 ? 0xFF & b[b.length - 3] : b[b.length - 3];
		int b2 = b[b.length - 2] < 0 ? 0xFF & b[b.length - 2] : b[b.length - 2];
		int b1 = b[b.length - 1] < 0 ? 0xFF & b[b.length - 1] : b[b.length - 1];
		int val = (b1 << 24) | (b2 << 16) + (b3 << 8) + b4;
		return val;
	}

	private static byte[] copyOfRange(byte[] data, int pos, int length) {
		byte[] bs = new byte[length - pos];
		for (int i = pos; i < data.length; i++) {
			bs[i - pos] = data[i];
		}
		return bs;
	}

	public String getWordsFilePath() {
		return APP_DIRECTORY + "/" + filePath.hashCode() + ".csv";
	}
	public static String getWordsFilePath(String path) {
		return APP_DIRECTORY + "/" + path.hashCode() + ".csv";
	}
	public void uninit() {
		File f = new File(String.valueOf(OptimizeBgl.this.getFilePath()
				.hashCode()));
		if (f.exists())
			f.delete();
		words.close();
		words.dropDB();

	}

	private class InitAsync extends AsyncTask<String, Integer, OptimizeBgl> {

		File f = null;

		int value = -1;

		private FileInputStream file;

		private int rem;

		@Override
		protected void onPreExecute() {
			try {
				System.gc();
				f = new File(filePath);
				if (!f.exists())
					return;
				value = getFileGzipSize(f);
				System.gc();
				file = new FileInputStream(f);

				int poss = getGzipheader(file);
				if (poss == -1)
					return;
				file.skip((int) poss - 6);
				rem = ((int) f.length()) - poss;

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (handler != null)
				handler.onPreExecution(value);

		}

		private int getFileGzipSize(File f2) {
			try {
				InputStream file = new FileInputStream(f2);
				file.skip(f2.length() - 4);
				byte[] b = new byte[4];
				file.read(b);
				file.close();
				return getGzipSize(b);

			} catch (FileNotFoundException e) {
			} catch (IOException e) {
				e.printStackTrace();
			}

			return -1;
		}

		@Override
		protected OptimizeBgl doInBackground(String... params) {
			try {
				if (f != null && !f.exists())
					return null;

				InputStream iss = new GZIPInputStream(file, rem);
				crrateDirectory();
				OutputStream out = new FileOutputStream(getWordsFilePath());
				

				System.gc();
				String headword = null;

				ByteArrayOutputStream buff = new ByteArrayOutputStream();

				ArrayList<Index> sb = new ArrayList<Index>();
				words.open();
				System.gc();
				byte[] aboutS = null;

				int type = -1, si = 0, buffsize = 0;
				while (!isCancelled()) {
					headword = "";
					BglBlock block = BglBlock.read(buff,iss);
					if (block == null) {
						break;
					}
					if (block.type == 0 && block.data[0] == 8) {
						type = block.data[1];
						if (type > 64) {
							type -= 65;
						}
						OptimizeBgl.this.DefCharset = BabylonConsts.Bgl_charset[type];
					} else if (block.type == 3) {
						int pos = 2;
						switch (block.data[1]) {
						case 1:
							for (int a = 0; a < block.length - 2; a++) {
								headword += (char) block.data[pos++];
							}
							OptimizeBgl.this.Title = headword;
							break;
						case 2:
							for (int a = 0; a < block.length - 2; a++) {
								headword += (char) block.data[pos++];
							}
							OptimizeBgl.this.Author = headword;
							break;
						case 7:
							OptimizeBgl.this.SrcLng = BabylonConsts.Bgl_language[block.data[5]];
							break;
						case 8:
							OptimizeBgl.this.DstLng = BabylonConsts.Bgl_language[block.data[5]];
							break;
						case 9: {
							aboutS = new byte[block.length];
							aboutS = copyOfRange(block.data, pos, block.length);
						}
							break;
						case 11:
							FileOutputStream fos = context.openFileOutput(
									String.valueOf(OptimizeBgl.this
											.getFilePath().hashCode()),
									Context.MODE_PRIVATE);
							fos.write(copyOfRange(block.data, pos, block.length));
							fos.close();

							break;
						case 26:
							type = block.data[2];
							if (type > 64) {
								type -= 65;
							}
							OptimizeBgl.this.SrcEnc = BabylonConsts.Bgl_charset[type];
							OptimizeBgl.this.SrcEncName = BabylonConsts.Bgl_charsetname[type];
							OptimizeBgl.this.SrcEncoding = (OptimizeBgl.this.SrcEnc);

							break;
						case 27:
							type = block.data[2];
							if (type > 64) {
								type -= 65;
							}
							OptimizeBgl.this.DstEnc = BabylonConsts.Bgl_charset[type];
							OptimizeBgl.this.DstEncName = BabylonConsts.Bgl_charsetname[type];
							OptimizeBgl.this.DstEncoding = (OptimizeBgl.this.DstEnc); // Encoding.GetEncoding
							break;
						}
					} else if (block.type == 1 || block.type == 10) {
						int len = 0;
						int pos = 0;
						// Head
						byte d = block.data[pos++];
						len = d < 0 ? d & 0xFF : d;
						if (SrcEncoding == null) {
							CharsetDetector cd = new CharsetDetector();
							cd.setText(copyOfRange(block.data, pos, len));
							CharsetMatch ma = cd.detect();
							if (ma != null) {
								SrcEnc = ma.getName();
								SrcEncoding = SrcEnc;
							}

						}

						String h = block.GetString(pos, len, SrcEncoding)
								.trim();
						sb.add(new Index(h, String.valueOf(si)));
						if (sb.size() == 5000) {
							words.MultipleInsert(sb);
							sb.clear();
							publishProgress(si);
							
						}
					
					} else if (block.type == 2 && block.data[0] == 12) {
						StringBuilder sba = new StringBuilder();
						for (int i = 0; i < block.length
								&& !(startWith(block.data, BabylonConsts.BMP, i)
										|| startWith(block.data,
												BabylonConsts.JPG, i) || startWith(
											block.data, BabylonConsts.PNG, i)); i++) {
							sba.append((char) block.data[i]);
						}

						FileOutputStream fos = OptimizeBgl.this.context
								.openFileOutput(sba.toString(),
										Context.MODE_PRIVATE);
						fos.write(copyOfRange(block.data, sba.length(),
								block.length));
						fos.close();
						publishProgress(si);
					}
					if (buffsize > BLOCK_SIZE) {
						out.write(buff.toByteArray());
						buff.close();
						buff = null;
						System.gc();
						buff = new ByteArrayOutputStream();
						buffsize = 0;
					}
					si += block.realSize;
					buffsize += block.realSize;
				 

				}
				if (buffsize > 0) {
					out.write(buff.toByteArray());
					buff.close();
					buff = null;
					System.gc();
					buffsize = 0;
				}
				out.close();
				iss.close();
				file.close();
				file = null;
				iss = null;
				System.gc();

				OptimizeBgl.this.about = new String(aboutS, getDstEncoding());

				if (sb.size() != 0) {
					words.MultipleInsert(sb);
					publishProgress(si);
					sb.clear();
				}
				System.gc();

				publishProgress(value);
			} catch (Exception e) {
				Log.i("", e.toString());
				publishProgress(0);
				return null;
			}
			words.close();
	 
			return  OptimizeBgl.this;
		}

		private void crrateDirectory() {
			File f = new File(APP_DIRECTORY);
			if (!f.exists())
				f.mkdir();
		}

		public byte[] copyOfRange(byte[] original, int start, int end) {
			if (start > end) {
				throw new IllegalArgumentException();
			}
			int originalLength = original.length;
			if (start < 0 || start > originalLength) {
				throw new ArrayIndexOutOfBoundsException();
			}
			int resultLength = end - start;
			int copyLength = Math.min(resultLength, originalLength - start);
			byte[] result = new byte[resultLength];
			System.arraycopy(original, start, result, 0, copyLength);
			return result;
		}

		@Override
		protected void onCancelled() {
			uninit();
			f = null;
			file = null;
			System.gc();
			if (handler != null)
				handler.onCancelInstalling();

		}

		@Override
		protected void onPostExecute(OptimizeBgl result) {
			System.gc();

			if (handler != null)
				handler.onPostExecution(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if (handler != null)
				handler.onExecuting(values[0]);

		}
	}
}

package com.caprica.ava.db.bgl;

import android.annotation.SuppressLint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author moji
 */
class BglBlock {

	public int type;
	public int length;
	public byte[] data = new byte[0];
	public ArrayList<Object> tagx = new ArrayList<Object>();
	public int realSize;
	private static String[] PartOfSpeech = new String[] { "n", "adj", "v",
			"adv", "interj", "pron", "prep", "conj", "suff", "pref", "art" };

	private static int readNum(OutputStream out, InputStream s, int bytes) {
		byte[] buf = new byte[4];
		if (bytes < 1 || bytes > 4) {
			System.out.println("Must be between 1 and 4" + "bytes");
		}

		try {
			s.read(buf, 0, bytes);
			out.write(buf, 0, bytes);
		} catch (IOException e) {

			e.printStackTrace();
		}

		int val = 0;
		for (int i = 0; i < bytes; i++) {
			val = (val << 8) | (buf[i] > 0 ? buf[i] : buf[i] & 0xFF);
		}

		return val;
	}

	private static int readNum(InputStream s, int bytes) {
		byte[] buf = new byte[4];
		if (bytes < 1 || bytes > 4) {
			System.out.println("Must be between 1 and 4" + "bytes");
		}

		try {
			s.read(buf, 0, bytes);

		} catch (IOException e) {

			e.printStackTrace();
		}

		int val = 0;
		for (int i = 0; i < bytes; i++) {
			val = (val << 8) | (buf[i] > 0 ? buf[i] : buf[i] & 0xFF);
		}

		return val;
	}

	public static BglBlock read(OutputStream out, InputStream s) {

		BglBlock block = new BglBlock();
		block.length = readNum(out, s, 1);
		block.realSize = 1;
		block.type = (block.length & 0xf);
		if (block.type == 4) {
			return null;
		} // end-of- file marker

		block.length >>= 4;
		if (block.length < 4) {
			block.realSize += block.length + 1;
			block.length = readNum(out, s, block.length + 1);

		} else {

			block.length = block.length - 4;
		}

		if (block.length > 0) {
			block.data = new byte[block.length];
			try {
				s.read(block.data, 0, block.data.length);
				out.write(block.data, 0, block.data.length);
			} catch (IOException ex) {
				Logger.getLogger(BglBlock.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
		block.realSize += block.length;
		return block;
	}

	public static BglBlock read(InputStream s) {

		BglBlock block = new BglBlock();
		block.length = readNum(s, 1);
		block.realSize = 1;
		block.type = (block.length & 0xf);
		if (block.type == 4) {
			return null;
		} // end-of- file marker

		block.length >>= 4;
		if (block.length < 4) {
			block.realSize += block.length + 1;
			block.length = readNum(s, block.length + 1);

		} else {

			block.length = block.length - 4;
		}

		if (block.length > 0) {
			block.data = new byte[block.length];
			try {
				s.read(block.data, 0, block.data.length);

			} catch (IOException ex) {
				Logger.getLogger(BglBlock.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
		block.realSize += block.length;
		return block;
	}

	public static BglBlock uselessRead(InputStream s) {

		BglBlock block = new BglBlock();
		block.length = readNum(s, 1);
		block.realSize = 1;
		block.type = (block.length & 0xf);
		if (block.type == 4) {
			return null;
		} // end-of- file marker

		block.length >>= 4;
		if (block.length < 4) {
			block.realSize += block.length + 1;
			block.length = readNum(s, block.length + 1);

		} else {

			block.length = block.length - 4;
		}

		if (block.length > 0) {
			block.data = new byte[block.length];
			try {
				s.read(block.data, 0, block.data.length);
			} catch (IOException ex) {
				Logger.getLogger(BglBlock.class.getName()).log(Level.SEVERE,
						null, ex);
			}

		}
		block.realSize += block.length;
		return block;
	}

	@Override
	public String toString() {
		try {
			return "BglBlock: t=" + type + " len=" + length + "\r\n" + "---"
					+ new String(data, "US-ASCII") + "\r\n";
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(BglBlock.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return null;
	}

	public String GetString(int offset, int length, String enc) {
		ByteArrayOutputStream ms = new ByteArrayOutputStream();
		ms.write(data, offset, length);
		return GetString(ms, enc);
	}

	public String GetDefString(int offset, int length, String enc) {
		ByteArrayOutputStream ms = new ByteArrayOutputStream();

		for (int i = offset; i < offset + length; i++) {
			int b = data[i] < 0 ? data[i] & 0xff : data[i];

			if (b == 0x0a) {
				ms.write((byte) '\n');
			} else if (b < 0x20) {
				int bn = data[i + 1] < 0 ? data[i + 1] & 0xff : data[i + 1];

				if (i < (offset + length - 2) && b == 0x14 && bn == 0x12) {
					bn = data[i + 1] < 0 ? data[i + 2] & 0xff : data[i + 2];

					int posIndex = bn - 0x30;
					String posS = "/POS:" + PartOfSpeech[posIndex] + ". ";

					byte[] posBytes = null;
					try {

						posBytes = posS.getBytes("us-ascii");
					} catch (UnsupportedEncodingException ex) {
						Logger.getLogger(BglBlock.class.getName()).log(
								Level.SEVERE, null, ex);
					}
					ms.write(posBytes, 0, posBytes.length);
				}
				i += 2;
			} else {
				ms.write(b);
			}
		}

		return GetString(ms, enc);
	}

	@SuppressLint("DefaultLocale")
	static String GetString(ByteArrayOutputStream ms, String enc) {
		String s = null;
		try {

			s = new String(ms.toByteArray(), enc.toLowerCase());
		} catch (UnsupportedEncodingException ex) {
			System.err.println(ex);
		}

		return s;
	}

	static XDictEntry ParseEntry(BglBlock block, String SrcEncoding,
			String DstEncoding) {
		int len = 0;
		int pos = 0;
		// Head
		byte d = block.data[pos++];
		len = d < 0 ? d & 0xFF : d;
		String headWord = block.GetString(pos, len, SrcEncoding.toLowerCase())
				.trim();
		pos += len;

		// Definition
		d = block.data[pos++];
		len = (d < 0 ? d & 0xff : d) << 8;
		d = block.data[pos++];
		len |= (d < 0 ? d & 0xff : d);
		String def = block.GetDefString(pos, len, DstEncoding).trim();
		pos += len;

		// Alternates
		ArrayList<String> alternates = new ArrayList<String>();
		while (pos < block.length) {
			d = block.data[pos++];
			len = (d < 0 ? d & 0xff : d);
			String alternate = block.GetString(pos, len,
					SrcEncoding.toLowerCase()).trim();
			pos += len;
			alternates.add(alternate);
		}

		return CreateEntry(headWord, def, alternates);
	}

	static String getWord(BglBlock block, String SrcEncoding, String DstEncoding) {
		int len = 0;
		int pos = 0;
		// Head
		byte d = block.data[pos++];
		len = d < 0 ? d & 0xFF : d;
		String headWord = block.GetString(pos, len, SrcEncoding).trim();
		return headWord;
	}

	static XDictEntry CreateEntry(String head, String def,
			ArrayList<String> alternates) {
		XDictEntry entry = new XDictEntry(new XWordInfo(head));

		if (!("".equals(def) || def == null)) {
			XWordInfo defWi = new XWordInfo(def);
			entry.getDefinitions().add(defWi);
		}

		for (String alt : alternates) {
			// These are odd. In one of the sources,
			// for "Austen", alt is "Jane" for "hang", it's "draw",
			// "and quarter"
			// Might depend on the source.
			XWordInfo wi = new XWordInfo(alt);
			entry.getComments().add(wi);
		}
		return entry;
	}

}

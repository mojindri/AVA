package com.caprica.ava.db.bgl;
 
public class BaseBgl {

	protected String DefCharset;
	protected String SrcEnc;
	protected String SrcEncName;
	protected String DstEnc;
	protected String DstEncName;
	protected String SrcLng;
	protected String DstLng;
	protected String Title;
	protected String Author;
	protected String Id;
	protected String filePath;
	protected String about;
	protected boolean isNew;

	// progressbar
	protected int progress = -1;
	protected int maxV;
	protected int btnState=BTN_INSTALL;
 
	public static final int BTN_CANCELL = 1;
	public static final int BTN_INSTALL = 2;
	public static final int BTN_UNINSTALL = 3;

	public void setBtnState(int btnState) {
		this.btnState = btnState;
	}

	public int getBtnState() {
		return btnState;
	}

	public int getMaxV() {
		return maxV;
	}

	public void setMaxV(int maxV) {
		this.maxV = maxV;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getProgress() {
		return progress;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean isNew() {
		return isNew;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public BaseBgl(String id, String author, String title, String dstlng,
			String srclng, String dstencname, String dstenc, String srcencname,
			String srcenc, String defcharset, String filename, String about,
			boolean isnew) {

		setId(id);
		setAuthor(author);
		setTitle(title);
		setDstLng(dstlng);
		setSrcLng(srclng);
		setDstEncName(dstencname);
		setDstEnc(dstenc);
		setSrcEncName(srcencname);
		setSrcEnc(srcenc);
		setDefCharset(defcharset);
		setFilePath(filename);
		setAbout(about);
		setNew(isnew);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public BaseBgl() {
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getAuthor() {
		return Author;
	}

	public String getDefCharset() {
		return DefCharset;
	}

	public String getDstEnc() {
		return DstEnc;
	}

	public String getDstEncName() {
		return DstEncName;
	}

	public String getDstEncoding() {
		return DstEncoding;
	}

	public String getDstLng() {
		return DstLng;
	}

	public static String[] getPartOfSpeech() {
		return PartOfSpeech;
	}

	public String getSrcEnc() {
		return SrcEnc;
	}

	public String getSrcEncName() {
		return SrcEncName;
	}

	public String getSrcEncoding() {
		return SrcEncoding;
	}

	public String getSrcLng() {
		return SrcLng;
	}

	public String getTitle() {
		return Title;
	}

	public void setAuthor(String Author) {
		this.Author = Author;
	}

	public void setDefCharset(String DefCharset) {
		this.DefCharset = DefCharset;
	}

	public void setDstEnc(String DstEnc) {
		this.DstEnc = DstEnc;
	}

	public void setDstEncName(String DstEncName) {
		this.DstEncName = DstEncName;
	}

	public void setDstEncoding(String DstEncoding) {
		this.DstEncoding = DstEncoding;
	}

	public void setDstLng(String DstLng) {
		this.DstLng = DstLng;
	}

	public void setSrcEnc(String SrcEnc) {
		this.SrcEnc = SrcEnc;
	}

	public void setSrcEncName(String SrcEncName) {
		this.SrcEncName = SrcEncName;
	}

	public void setSrcEncoding(String SrcEncoding) {
		this.SrcEncoding = SrcEncoding;
	}

	public void setSrcLng(String SrcLng) {
		this.SrcLng = SrcLng;
	}

	public void setTitle(String Title) {
		this.Title = Title;
	}

	protected String SrcEncoding;
	protected String DstEncoding;
	public static String[] PartOfSpeech = new String[] { "n", "adj", "v",
			"adv", "interj", "pron", "prep", "conj", "suff", "pref", "art" };

}

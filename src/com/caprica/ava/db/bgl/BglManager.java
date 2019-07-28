package com.caprica.ava.db.bgl;

import java.io.File;
import java.util.ArrayList;
import com.caprica.ava.BglInitOperationTimingListView;
import com.caprica.ava.BglInitiOperationTiming;
import android.content.Context;
import android.database.Cursor;

public class BglManager implements BglInitiOperationTiming {
	BglHeader header;
	Context context;
	OptimizeBgl ob = null;
	int position = -1;
	private BglInitOperationTimingListView myhandler;

	public void setInithandler(BglInitOperationTimingListView myhandler) {
		this.myhandler = myhandler;
	}

	public BglManager(Context context) {
		header = new BglHeader(context);
		this.context = context;
	}

	public OptimizeBgl getOptimizeBgl(String title) {
		OptimizeBgl ob = null;
		header.open();
		BaseBgl bb = header.select(title);
		if (bb != null) {
			ob = new OptimizeBgl(bb, context);
			ob.setInitHandler(this);
		}
		header.close();
		return ob;
	}

	public void install(String title, String bglpath, int pos) {
		this.position = pos;
		ob = new OptimizeBgl(context, bglpath, title);
		ob.setInitHandler(this);
		ob.startInitAsync();
	}

	public void installOnce(ArrayList<OptimizeBgl> bglPaths) {
		header.open();
		for (OptimizeBgl optimizeBgl : bglPaths) {
			header.insert(optimizeBgl);
		}
		header.close();
	}

	public void uninstall(String title) {
		header.open();

		if (title != null) {
			BaseBgl bb = header.select(title);
			if (bb != null) {
				OptimizeBgl ob = new OptimizeBgl(bb, context);
				ob.uninit();
				header.delete(title);
			}
		}

		header.close();

	}

	@Override
	public void onPreExecution(int progress) {
		if (myhandler != null)
			myhandler.onPreExecution(position, progress);

	}

	@Override
	public void onExecuting(int progress) {
		if (myhandler != null)
			myhandler.onExecuting(position, progress);

	}

	@Override
	public void onPostExecution(OptimizeBgl progress) {
		header.open();

		if (ob != null) {
			String oldTitle = new File(ob.getFilePath()).getName();
			ob.setNew(false);
			header.updateNewColumn(oldTitle, ob, ob.isNew);
			header.close();
		
		}
		if (myhandler != null)
			myhandler.onPostExecution(position, ob);
 
	}

	public ArrayList<OptimizeBgl> getAllBgls(boolean all) {
		ArrayList<OptimizeBgl> bgl = null;
		header.open();
		ArrayList<BaseBgl> b = header.queryAlls(all);
		if (b == null || b.size() == 0)
			return bgl;
		bgl = new ArrayList<OptimizeBgl>();
		for (int i = 0; i < b.size(); i++) {
			bgl.add(new OptimizeBgl(b.get(i), context));

		}
		header.close();
		return bgl;
	}
	public ArrayList<String> getAllBglPaths(boolean all) {
		header.open();
		ArrayList<String> b = header.queryAllBglsByKey(BglHeader.KEY_FILEPATH,all);
		if (b == null || b.size() == 0)
			return null;
		header.close();
		return b;
	}

	public ArrayList<String> getAllBglNames(boolean all) {
		header.open();
		ArrayList<String> b = header.queryAllBglsByKey(BglHeader.KEY_TITLE,all);
		if (b == null || b.size() == 0)
			return null;
		header.close();
		return b;
	}

	@Override
	public void onCancelInstalling() {
		if (myhandler != null)
			myhandler.onCancelInstalling(position);
		ob = null;

	}

	public void cancelInstalling() {
		if (ob != null) {
			ob.cancelInitAsync();
		}
		ob = null;

	}

	public BglHeader getHeader() {
		return header;
	}

	public Cursor getHeadersQuery() {

		return header.queryAllCursor();
	}

}

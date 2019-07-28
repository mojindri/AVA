package com.caprica.ava;

import java.io.File;
import java.util.ArrayList;
import com.caprica.ava.adaptor.LibraryExpandableListAdaptor;
import com.caprica.ava.db.bgl.BglManager;
import com.caprica.ava.db.bgl.OptimizeBgl;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

public class LibraryFragment extends Fragment implements OnClickListener,
		DialogFragmentResultCallBackListener {
	private Context context;
	private BglManager bm;
	private Button btn;
	private ExpandableListView ela;

	private ArrayList<OptimizeBgl> oba;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.context = getActivity();
		if (!(context instanceof MainActivity))
			return null;

		View v = inflater.inflate(R.layout.library_fragment, container, false);
		btn = (Button) v.findViewById(R.id.btn_library_scan);
		ela = (ExpandableListView) v.findViewById(R.id.elv_library_fragment);

		btn.setOnClickListener(this);

		bm = new BglManager(context);
		oba = new ArrayList<OptimizeBgl>();

		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		refresh();
	}

	public void refresh() {
		if (bm == null)
			return;

		oba.clear();

		ArrayList<OptimizeBgl> installedBgls = bm.getAllBgls(true);
		if (installedBgls == null)
			return;
		oba.addAll(installedBgls);
		LibraryExpandableListAdaptor l = new LibraryExpandableListAdaptor(
				context, oba, this);
		setGroupIndicatorToRight();
		ela.setChoiceMode(ExpandableListView.CHOICE_MODE_NONE);
		ela.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				for (int i = 0; i < ela.getCount(); i++) {
					if (i != groupPosition)
						ela.collapseGroup(i);
				}

			}
		});

		ela.setAdapter(l);

	}

	private void setGroupIndicatorToRight() {
		/* Get the screen width */
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			ela.setIndicatorBounds(width - getDipsFromPixel(35), width
					- getDipsFromPixel(5));

		} else {
			ela.setIndicatorBoundsRelative(width - getDipsFromPixel(35), width
					- getDipsFromPixel(5));
		}
	}

	// Convert pixel to dip
	public int getDipsFromPixel(float pixels) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (pixels * scale + 0.5f);
	}

	ProgressBar pb;

	public ArrayList<String> getArraylistofAvailableBgls() {
		if (oba != null) {
			ArrayList<String> al = new ArrayList<String>();
			for (int i = 0; i < oba.size(); i++) {
				al.add(oba.get(i).getFilePath());
			}
			return al;
		}
		return null;
	}

	@Override
	public void onClick(View view) {
		LibraryScannerDialog s = LibraryScannerDialog
				.newInstance(DialogFragment.STYLE_NO_FRAME);
		s.setOnMessageHandler(this);
		if (oba != null && oba.size() > 0) {

			Bundle bundle = new Bundle();
			bundle.putStringArrayList("paths", getArraylistofAvailableBgls());
			s.setArguments(bundle);
		}
		s.show(getFragmentManager(), "dialog");

	}

	@Override
	public void onMessage(Object... values) {
		ArrayList<String> paths = (ArrayList<String>) values[0];

		headerAnaylzorTask header = new headerAnaylzorTask();
		header.execute(paths);
	}

	private class headerAnaylzorTask extends
			AsyncTask<ArrayList<String>, Integer, Integer> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {

			pd = new ProgressDialog(context);
			pd.setTitle("anaylyzing...");
			pd.setMessage("Please Wait....");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();

		}

		@Override
		protected Integer doInBackground(ArrayList<String>... params) {

			ArrayList<String> array = params[0];
			ArrayList<OptimizeBgl> ops = new ArrayList<OptimizeBgl>();
			pd.setMax(array.size());
			for (int i = 0; i < array.size(); i++) {
				String path = array.get(i);
				System.gc();
				File f= new File(path);
				OptimizeBgl bgl = new OptimizeBgl(context,f.getAbsolutePath(),f.getName());
				bgl.setNew(true);
				bgl.setAbout("Please install this dictionary to see details");
				ops.add(bgl);
				pd.setProgress(i);
			}
			bm.installOnce(ops);
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			pd.setProgress(pd.getMax());
			pd.dismiss();
			refresh();

		}
	}

}

 package com.caprica.ava;

import java.io.File;
import java.util.ArrayList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LibraryScannerDialog extends DialogFragment {
	int mNum;
	private ArrayList<String> foundFiles = new ArrayList<String>();
	private ArrayList<String> RealPaths = new ArrayList<String>();
	private DialogFragmentResultCallBackListener onMessageHandler;

	public void setOnMessageHandler(
			DialogFragmentResultCallBackListener onMessageHandler) {
		this.onMessageHandler = onMessageHandler;
	}

	static LibraryScannerDialog newInstance(int num) {
		LibraryScannerDialog f = new LibraryScannerDialog();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setCancelable(false);
		f.setArguments(args);
		return f;
	}

	ProgressBar pb;
	TextView tvDir;
	ListView lvResult;
	private ArrayAdapter<String> adaptor;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.library_scanner_dialog, container,
				false);
		Button btnOk = (Button) v.findViewById(R.id.btn_library_scanner_ok);
		lvResult = (ListView) v.findViewById(R.id.lv_library_scanner_result);
		adaptor = new ArrayAdapter<String>(getActivity().getBaseContext(),
				android.R.layout.simple_list_item_1, foundFiles);
		lvResult.setAdapter(adaptor);

		tvDir = (TextView) v.findViewById(R.id.tv_library_scanner_dir);
		Button btnCancel = (Button) v
				.findViewById(R.id.btn_library_scanner_cancel);
		pb = (ProgressBar) v.findViewById(R.id.pg_library_scanner_progress);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (taskSearch!=null)
					taskSearch.cancel(true);
				if (onMessageHandler != null)
					onMessageHandler.onMessage(RealPaths);// returns the result
															// to library
															// fragment
				dismiss();
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (taskSearch != null)
					taskSearch.cancel(true);
				dismiss();
			}
		});

		return v;

	}

	searchFile taskSearch;

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		taskSearch = new searchFile();
		taskSearch.execute(Environment.getExternalStorageDirectory()
				.getAbsolutePath(), "0");
	}

	private class searchFile extends AsyncTask<String, Object, File> {
		ArrayList<String> installedBgls;

		@Override
		protected void onPreExecute() {
			pb.setProgress(0);
			if (getArguments().containsKey("paths"))
				installedBgls = getArguments().getStringArrayList("paths");
		}

		protected void onPostExecute(File result) {

			pb.setProgress(firstchildrencount);
		}

		File[] children;
		int firstchildrencount = -1;

		int x = 0;

		@Override
		protected File doInBackground(String... params) {

			children = new File(params[0]).listFiles();
			if (firstchildrencount == -1) {
				firstchildrencount = children.length;
				publishProgress(3, firstchildrencount);
			}
			for (File child : children) {
				if (isCancelled())
					return null;
				if (child.isDirectory()) {
					doInBackground(new String[] { child.getAbsolutePath(), "1" });
				} else {
					if (child.getAbsolutePath().toLowerCase().endsWith(".bgl")) {
						publishProgress(0, child);
					}

					publishProgress(1, child);
				 
				}

				if ("0".equals(params[1]))
					publishProgress(2, ++x);
				publishProgress(2, 0);
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			int choice = Integer.parseInt(values[0].toString());
			if (choice == 0) { // result found for bgls
				File child = (File) values[1];
				if (adaptor != null) {
					if (installedBgls == null
							|| (installedBgls != null && !installedBgls
									.contains(child.getAbsolutePath()))) {
						foundFiles.add(child.getName());
						RealPaths.add(child.getAbsolutePath());
						adaptor.notifyDataSetChanged();
					}

				}
			}
			if (choice == 1) { // update text view for dirs
				File child = (File) values[1];
				if (!"".equals(child.getAbsoluteFile()))
					tvDir.setText(child.getAbsolutePath());
			}
			if (choice == 2) { // updates progressbar of the dialog
				pb.setProgress(x);
			}
			if (choice == 3) { // set the maximum possibile value for the
								// progressbar
				pb.setMax(firstchildrencount);
			}

			//

		}

		public String splitPath(String path) {
			int len = path.length();
			if (len > 20)
				return path.substring(len - 20);
			else
				return path;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments().getInt("num");

		// Pick a style based on the num.
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		switch ((mNum - 1) % 6) {
		case 1:
			style = DialogFragment.STYLE_NO_TITLE;
			break;
		case 2:
			style = DialogFragment.STYLE_NO_FRAME;
			break;
		case 3:
			style = DialogFragment.STYLE_NO_INPUT;
			break;
		case 4:
			style = DialogFragment.STYLE_NORMAL;
			break;
		case 5:
			style = DialogFragment.STYLE_NORMAL;
			break;
		case 6:
			style = DialogFragment.STYLE_NO_TITLE;
			break;
		case 7:
			style = DialogFragment.STYLE_NO_FRAME;
			break;
		case 8:
			style = DialogFragment.STYLE_NORMAL;
			break;
		}
		switch ((mNum - 1) % 6) {
		case 4:
			theme = android.R.style.Theme_Holo;
			break;
		case 5:
			theme = android.R.style.Theme_Holo_Light_Dialog;
			break;
		case 6:
			theme = android.R.style.Theme_Holo_Light;
			break;
		case 7:
			theme = android.R.style.Theme_Holo_Light_Panel;
			break;
		case 8:
			theme = android.R.style.Theme_Holo_Light;
			break;
		}
		setStyle(style, theme);
	}

}

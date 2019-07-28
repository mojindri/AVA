package com.caprica.ava;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import com.caprica.ava.db.bgl.BglHeader;
import com.caprica.ava.db.bgl.BglManager;
import com.caprica.ava.db.bgl.BglWords;
import com.caprica.ava.db.bgl.OptimizeBgl;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class DictionaryFragment extends Fragment implements
		OnItemClickListener, SearchViewTextChangeListener {
	ListView lv;
	SimpleCursorAdapter sca;
	SimpleCursorAdapter headerAdaptor;
	static final String[] FROM = new String[] { BglWords.KEY_WORD };
	static final int[] TO = new int[] { com.caprica.ava.R.id.tv_item_word };

	static final String[] FROMSPINNER = new String[] { BglHeader.KEY_FILEPATH,
			BglHeader.KEY_TITLE };
	static final int[] TOSPINNER = new int[] {
			com.caprica.ava.R.id.iv_library_list_group_icon,
			com.caprica.ava.R.id.tv_library_list_group_caption };
	final HashMap<Long, Bitmap> cacheMap = new HashMap<Long, Bitmap>();
	private final ViewBinder VIEW_BINDER = new ViewBinder() {

		public boolean setViewValue(View view, Cursor cursor, int column) {

			if (view.getId() == R.id.iv_library_list_group_icon) {

				long filename = cursor.getString(
						cursor.getColumnIndex(BglHeader.KEY_FILEPATH))
						.hashCode();
				if (!cacheMap.containsKey(filename)) {
					File path = new File(getActivity().getBaseContext()
							.getFilesDir(), String.valueOf(filename));
					Bitmap bit = BitmapFactory.decodeFile(path
							.getAbsolutePath());
					((ImageView) view).setImageBitmap(bit);
					cacheMap.put(filename, bit);
				} else {
					((ImageView) view).setImageBitmap(cacheMap.get(filename));
				}
				return true;
			}

			return false;

		};
	};
	MainActivity curActivity;
	private Spinner spinner;

	private BglManager bm;
	private ArrayList<OptimizeBgl> bgls;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof ListViewFragmentItemClick) {
			this.curActivity = (MainActivity) activity;

		}
	}

	@Override
	public void onDetach() {

		super.onDetach();

	}

	void refresh(String word, String dbname) {
		refreshAsync ref = new refreshAsync();
		ref.execute(word, dbname);

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dictionary_fragment, container,
				false);
		lv = (ListView) view.findViewById(R.id.lv_dictionary_items);
		bm = new BglManager(getActivity().getBaseContext());

		DictionaryDetailFragment.bglsList =  bm.getAllBgls(false);
		bm.getHeader().open();
		headerAdaptor = new SimpleCursorAdapter(getActivity().getBaseContext(),
				R.layout.library_list_group_fragment, bm.getHeadersQuery(),
				FROMSPINNER, TOSPINNER);
		spinner = (Spinner) view.findViewById(R.id.s_dictionary_items);

		headerAdaptor.setViewBinder(VIEW_BINDER);
		spinner.setAdapter(headerAdaptor);
		bm.getHeader().close();
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (spinner.getSelectedItem() != null) {
					SQLiteCursor cur = (SQLiteCursor) spinner.getSelectedItem();
					String dbname = cur.getString(cur
							.getColumnIndex(BglHeader.KEY_FILEPATH));
					refresh("", dbname);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		lv.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {// exapandale
																					// click
																					// event
		if (curActivity != null) {
			TextView tv = (TextView) view.findViewById(R.id.tv_item_word);
			curActivity.onClick(tv.getText());
		}
	}

	@Override
	public void onTextChanged(String text) {
		SQLiteCursor cursor = (SQLiteCursor) spinner.getSelectedItem();
		String dbname = cursor.getString(cursor
				.getColumnIndex(BglHeader.KEY_FILEPATH));

		refresh(text, dbname);

	}

	private class refreshAsync extends AsyncTask<String, Integer, String> {
		private String dbname;
		private String word;
		private BglWords words;


		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			word = params[0];
			dbname = params[1];

			words = new BglWords(getActivity().getBaseContext());
			words.setDATABASE_NAME(dbname);
		
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			words.open();
			sca = new SimpleCursorAdapter(getActivity().getBaseContext(),
					R.layout.dicionary_list_item, OptimizeBgl.getQueryCursor(words, getActivity().getBaseContext(), word, dbname),DictionaryFragment.FROM, DictionaryFragment.TO);
			lv.setAdapter(sca);
			words.close();
			
		}
	}

}

package com.caprica.ava;

import java.util.ArrayList;

import com.caprica.ava.db.bgl.OptimizeBgl;
import com.caprica.ava.db.bgl.XDictEntry;
import com.caprica.ava.model.Index;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class DictionaryDetailFragment extends Fragment {
	private TextView tv;
	public static ArrayList<OptimizeBgl> bglsList;

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dictionary_detail_fragment,
				container, false);
		tv = (TextView) view.findViewById(R.id.tv_html);

		Bundle b = getArguments();
		if (b != null) {
			String val = b.getString("word");
			databasesAsync da = new databasesAsync();
			da.execute(val);
		}
		return view;
	}

	private class databasesAsync extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPostExecute(String result) {
			tv.setText(Html.fromHtml(result.replace('\0', ' ')));
		}
 
		@Override
		protected String doInBackground(String... params) {
			StringBuilder sb = new StringBuilder();

			for (OptimizeBgl val : bglsList) {

				Index i = val.getIndex(DictionaryDetailFragment.this
						.getActivity().getBaseContext(), params[0],val.getFilePath());
				if (i != null) {
					XDictEntry e = val.getEntry(Integer.parseInt(i.getSize()));
					for (int j = 0; j < e.getDefinitions().size(); j++) {
						sb.append(e.getDefinitions().get(j).getText() + "<br/>");
					}
					sb.append(Html.fromHtml("<b/><b/><b/>"));
				}
			}
			return sb.toString();
		}

		@Override
		protected void onPreExecute() {

		}
	}
}

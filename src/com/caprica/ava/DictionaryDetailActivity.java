package com.caprica.ava;
 
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
 

public class DictionaryDetailActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary_detail_activity);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			finish();
		}
		Fragment detailFrag = new DictionaryDetailFragment();
		Bundle bundle = getIntent().getBundleExtra("bundle");
		detailFrag.setArguments(bundle);
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		trans.replace(R.id.fl_main_dicionary, detailFrag);

		trans.commit();
	}
}

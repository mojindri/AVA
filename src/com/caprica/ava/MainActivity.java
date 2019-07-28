package com.caprica.ava;

import java.util.ArrayList;
import com.caprica.ava.adaptor.NavDrawerListAdapter;
import com.caprica.ava.model.NavDrawerItem;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity implements
		ListViewFragmentItemClick {
	DrawerLayout drawerlayout;
	ListView drawerList;
	ActionBarDrawerToggle mDrawerToggle;
	String[] navTitles;
	int[] navIcons;
	public ArrayList<NavDrawerItem> navItems;
	public NavDrawerListAdapter navAdaptor;
	boolean dualpane;
	private Fragment currentFrag;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_acitivty);
		// initilze
		inits();

		FrameLayout thisFrameFragment = (FrameLayout) findViewById(R.id.frame_Fragment);
		dualpane = (thisFrameFragment != null)
				&& (thisFrameFragment.getVisibility() == FrameLayout.VISIBLE);
		if (savedInstanceState == null) {
			display(0);
		}
		handleIntent(getIntent());

	}

	private void inits() {
		drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.list_slidermenu);
		navTitles = getResources().getStringArray(R.array.nav_titles);
		navIcons = getResources().getIntArray(R.array.nav_icons);

		navItems = new ArrayList<NavDrawerItem>();
		for (int i = 0; i < navTitles.length; i++) {
			navItems.add(new NavDrawerItem(navTitles[i], navIcons[i]));
		}

		drawerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		drawerList.setItemChecked(0, true);
		drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				display(arg2);
			}

		});
		navAdaptor = new NavDrawerListAdapter(getApplicationContext(), navItems);
		drawerList.setAdapter(navAdaptor);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View drawerView) {
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			};

			public void onDrawerOpened(View drawerView) {
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			};
		};
		drawerlayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		boolean drawerOpen = drawerlayout.isDrawerOpen(drawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		menu.findItem(R.id.action_search).setVisible(!drawerOpen);

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
		super.onNewIntent(intent);
	}

	private void display(int pos) {
		Fragment fragment = null;

		switch (pos) {
		case 0:
			fragment = new DictionaryFragment();
			break;
		case 1:
			fragment = new LibraryFragment();
			break;
		}
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
			for (int i = 0; i < navAdaptor.getCount(); i++) {
				((NavDrawerItem) navAdaptor.getItem(i)).setChecked(false);

			}
			((NavDrawerItem) navAdaptor.getItem(pos)).setChecked(true);

		}
		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_Fragment, fragment).commit();

		}
		currentFrag = fragment;
		drawerList.setItemChecked(pos, true);
		drawerList.setSelection(pos);
		drawerlayout.closeDrawer(drawerList);

	}

	private void handleIntent(Intent intent) {

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			// use the query to search your data somehow
			// Toast.makeText(this, query, Toast.LENGTH_LONG).show();

			if (currentFrag != null) {

				if (currentFrag instanceof DictionaryFragment) {
					((DictionaryFragment) currentFrag).onTextChanged(query);
				}
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
			SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
			//searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
			searchView.setIconifiedByDefault(false);
		} else {
			SearchView searchView = (SearchView) MenuItemCompat
					.getActionView(menu.findItem(R.id.action_search));
			searchView
					.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

						@Override
						public boolean onQueryTextSubmit(String arg0) {
							Intent i = new Intent(Intent.ACTION_SEARCH);
							i.putExtra(SearchManager.QUERY, arg0);
							handleIntent(i);
							return true;
						}

						@Override
						public boolean onQueryTextChange(String arg0) {
							return true;
						}
					});
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item))
			return true;

		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;

		case R.id.action_search:
			// onSearchRequested();
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onClick(Object... value0) {
		// Fragment detailFrag = new DictionaryDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putString("word", String.valueOf(value0[0]));

		// detailFrag.setArguments(bundle);
		// if (dualpane) {
		// FragmentTransaction trans = getSupportFragmentManager()
		// .beginTransaction();
		// trans.replace(R.id.frame_Fragment, detailFrag);
		// trans.addToBackStack(null);
		// trans.commit();
		// /
		// } else {
		Intent i = new Intent(MainActivity.this, DictionaryDetailActivity.class);
		i.putExtra("bundle", bundle);
		startActivity(i);

		// }
	}
}
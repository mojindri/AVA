package com.caprica.ava.adaptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import com.caprica.ava.BglInitOperationTimingListView;
import com.caprica.ava.LibraryFragment;
import com.caprica.ava.R;
import com.caprica.ava.db.bgl.BaseBgl;
import com.caprica.ava.db.bgl.BglManager;
import com.caprica.ava.db.bgl.OptimizeBgl;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LibraryExpandableListAdaptor extends BaseExpandableListAdapter {
	BglManager m;

	private List<OptimizeBgl> bglList;
	private Context context;
	private Fragment frag;

	HashMap<Integer, byte[]> cacheIcon = new HashMap<Integer, byte[]>();

	int active = -1;

	public LibraryExpandableListAdaptor(Context context,
			List<OptimizeBgl> bglList, LibraryFragment frag) {
		this.context = context;
		this.bglList = bglList;
		this.frag = frag;
		m = new BglManager(context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return bglList.get(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final OptimizeBgl bgl = (OptimizeBgl) getChild(groupPosition,
				childPosition);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.library_list_item_fragment,parent,false);
		}
		TextView tvDescription = (TextView) convertView
				.findViewById(R.id.tv_library_list_item_description);
		final ProgressBar pba = (ProgressBar) convertView
				.findViewById(R.id.pg_library_list_item_install_progress);
		final Button btnInstall = (Button) convertView
				.findViewById(R.id.btn_library_list_item_install);
		pba.setMax(bgl.getMaxV());
		pba.setProgress(bgl.getProgress());
		pba.setVisibility(bgl.getProgress() == -1 ? ProgressBar.INVISIBLE
				: ProgressBar.VISIBLE);
		if (bgl.getBtnState() != BaseBgl.BTN_CANCELL) {
			if (!bgl.isNew())
				bgl.setBtnState(BaseBgl.BTN_UNINSTALL);
		}

		switch (bgl.getBtnState()) {
		case BaseBgl.BTN_CANCELL:
			btnInstall.setText("cancel");
			break;
		case BaseBgl.BTN_INSTALL:
			btnInstall.setText("install");
			break;
		case BaseBgl.BTN_UNINSTALL:
			btnInstall.setText("unintall");
			break;

		default:
			break;
		}

		btnInstall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (bgl.getBtnState()) {
				case BaseBgl.BTN_CANCELL:
					if (m != null) {
						m.cancelInstalling();
					}
					break;
				case BaseBgl.BTN_INSTALL:

					installDialog(groupPosition, childPosition);
					break;
				case BaseBgl.BTN_UNINSTALL:
					uninstallDialog(groupPosition, childPosition);
					break;
				default:
					break;
				}
			}
		});
		tvDescription.setText(Html.fromHtml(bgl.getAbout()));
	 
		return convertView;
	}

	public void disableAllBut() {

	}

	public void ensableAllBut() {

	}

	void uninstallDialog(final int groupPosition, final int childPosition) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setMessage("Wanna delete this dicionary?");
		dialog.setCancelable(false);
		dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				m.uninstall(bglList.get(groupPosition).getTitle());
				bglList.remove(groupPosition);
				notifyDataSetChanged();
			}
		});
		dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog ad = dialog.create();
		ad.show();

	}

	void installDialog(final int groupPosition, final int childPosition) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setMessage("Wanna install this dicionary?");
		dialog.setCancelable(false);
		dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				m.setInithandler(new BglInitOperationTimingListView() {

					@Override
					public void onPreExecution(int g, int progress) {
						bglList.get(g).setBtnState(BaseBgl.BTN_CANCELL);
						bglList.get(g).setMaxV(progress);
						bglList.get(g).setProgress(0);
						notifyDataSetChanged();

					}

					@Override
					public void onExecuting(int g, int progress) {
						bglList.get(g).setProgress(progress);
						notifyDataSetChanged();
					}

					@Override
					public void onCancelInstalling(int g) {
						bglList.get(g).setBtnState(BaseBgl.BTN_INSTALL);
						bglList.get(g).setNew(true);
						bglList.get(g).setProgress(-1);
						bglList.get(g).setMaxV(0);
						notifyDataSetChanged();
						notifyDataSetChanged();
					}

					@Override
					public void onPostExecution(int g, OptimizeBgl progress) {
						bglList.get(g).setBtnState(BaseBgl.BTN_UNINSTALL);
						bglList.get(g).setNew(false);
						bglList.get(g).setProgress(-1);
						bglList.get(g).setAbout(progress.getAbout());
						bglList.get(g).setMaxV(0);
						
						notifyDataSetChanged();
					}
				});
				m.install(bglList.get(groupPosition).getTitle(),
						bglList.get(groupPosition).getFilePath(), groupPosition);
				dialog.dismiss();

			}
		});
		dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				bglList.get(groupPosition).setBtnState(BaseBgl.BTN_INSTALL);
				bglList.get(groupPosition).setNew(true);
				bglList.get(groupPosition).setProgress(-1);
				bglList.get(groupPosition).setMaxV(0);
				notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		AlertDialog ad = dialog.create();
		ad.show();

	}

	@Override
	public int getChildrenCount(int groupPosition) {

		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {

		return bglList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {

		return bglList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(
					R.layout.library_list_group_fragment, null);
		}
		ImageView ivIcon = (ImageView) convertView
				.findViewById(R.id.iv_library_list_group_icon);

		TextView TvTitle = (TextView) convertView
				.findViewById(R.id.tv_library_list_group_caption);
	 
		OptimizeBgl ob = bglList.get(groupPosition);
		TvTitle.setText(ob.getTitle());
		int hash = ob.getFilePath().hashCode();
		if (ob.isNew())
			ivIcon.setImageResource(R.drawable.iv_plus);
		else {
			byte[] b = null;
			if (!cacheIcon.containsKey(hash)) {
				//

				FileInputStream fis = null;
				File f = new File(context.getFilesDir() + "/"
						+ String.valueOf(hash));
				if (f.exists()) {
					try {

						fis = context.openFileInput(String.valueOf(hash));
						int size = (int) f.length();
						b = new byte[size];
						fis.read(b);
						fis.close();

					} catch (FileNotFoundException e) {

					} catch (IOException e) {

					} finally {
					}
				}
				cacheIcon.put(hash, b);
			}

			if (!cacheIcon.containsKey(hash))
				ivIcon.setImageBitmap(null);
			else
				ivIcon.setImageBitmap(BitmapFactory.decodeByteArray(
						cacheIcon.get(hash), 0, cacheIcon.get(hash).length));
		}
	 
		return convertView;
	}

	@Override
	public boolean hasStableIds() {

		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {

		return true;
	}

}

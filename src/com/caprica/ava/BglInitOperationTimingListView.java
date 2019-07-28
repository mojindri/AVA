package com.caprica.ava;

import com.caprica.ava.db.bgl.OptimizeBgl;

public interface BglInitOperationTimingListView {
	public void onPreExecution(int groupPos,int progress);

	public void onExecuting(int groupPos,int progress);

	public void onCancelInstalling(int groupPos);

	public void onPostExecution(int groupPos,OptimizeBgl progress);
}

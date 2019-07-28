package com.caprica.ava;

import com.caprica.ava.db.bgl.OptimizeBgl;

public interface BglInitiOperationTiming {
	public void onPreExecution(int progress);

	public void onExecuting(int progress);

	public void onCancelInstalling();

	public void onPostExecution(OptimizeBgl progress);

}

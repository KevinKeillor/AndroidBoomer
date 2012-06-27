package com.groggypirate.boomer;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 22/06/12
 * Time: 01:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class IgnitedAsyncTaskDefaultHandler<ContextT extends Context, ProgressT, ReturnT>
        implements IgnitedAsyncTaskHandler<ContextT, ProgressT, ReturnT> {

    private ContextT context;

    public IgnitedAsyncTaskDefaultHandler(ContextT context) {
        this.context = context;
    }

    @Override
    public final ContextT getContext() {
        return context;
    }

    @Override
    public final void setContext(ContextT context) {
        this.context = context;
    }

    @Override
    public boolean onTaskStarted(ContextT context) {
        return false;
    }

    @Override
    public boolean onTaskProgress(ContextT context, ProgressT... progress) {
        return false;
    }

    @Override
    public boolean onTaskCompleted(ContextT context, ReturnT result) {
        return false;
    }

    @Override
    public boolean onTaskSuccess(ContextT context, ReturnT result) {
        return false;
    }

    @Override
    public boolean onTaskFailed(ContextT context, Exception error) {
        return false;
    }

}
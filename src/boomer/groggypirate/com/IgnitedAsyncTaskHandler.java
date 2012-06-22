package boomer.groggypirate.com;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 22/06/12
 * Time: 01:31
 * To change this template use File | Settings | File Templates.
 */
public interface IgnitedAsyncTaskHandler<ContextT extends Context, ProgressT, ReturnT> {

    ContextT getContext();

    void setContext(ContextT context);

    /**
     * Return true from this method if you want to swallow the event; it will then not be passed on
     * to the task itself.
     */
    boolean onTaskStarted(ContextT context);

    /**
     * Return true from this method if you want to swallow the event; it will then not be passed on
     * to the task itself.
     */
    boolean onTaskProgress(ContextT context, ProgressT... progress);

    /**
     * Return true from this method if you want to swallow the event; it will then not be passed on
     * to the task itself.
     */
    boolean onTaskCompleted(ContextT context, ReturnT result);

    /**
     * Return true from this method if you want to swallow the event; it will then not be passed on
     * to the task itself.
     */
    boolean onTaskSuccess(ContextT context, ReturnT result);

    /**
     * Return true from this method if you want to swallow the event; it will then not be passed on
     * to the task itself.
     */
    boolean onTaskFailed(ContextT context, Exception error);
}

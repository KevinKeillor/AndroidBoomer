package boomer.groggypirate.com;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 22/06/12
 * Time: 01:45
 * To change this template use File | Settings | File Templates.
 */
public interface IgnitedAsyncTaskContextHandler<ProgressT, ReturnT> {

    /**
     * Return true from this method if you want to swallow the event; it will then not be passed on
     * to the task itself.
     */
    boolean onTaskStarted();

    /**
     * Return true from this method if you want to swallow the event; it will then not be passed on
     * to the task itself.
     */
    boolean onTaskProgress(ProgressT... progress);

    /**
     * Return true from this method if you want to swallow the event; it will then not be passed on
     * to the task itself.
     */
    boolean onTaskCompleted(ReturnT result);

    /**
     * Return true from this method if you want to swallow the event; it will then not be passed on
     * to the task itself.
     */
    boolean onTaskSuccess(ReturnT result);

    /**
     * Return true from this method if you want to swallow the event; it will then not be passed on
     * to the task itself.
     */
    boolean onTaskFailed(Exception error);
}

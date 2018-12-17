package Application;

/**
 * Encapsulates runnable object to allow reuse of threads
 * in thread pull
 */
public class TaskHandler implements Runnable {

    private Task task;

    public TaskHandler() {
        task = null;
    }

    /**
     * @param task Task to complete in separate thread
     * @throws NullPointerException
     */
    public void setTask(Task task) throws NullPointerException {
        if (task != null) {
            this.task = task;
        }
        else {
            throw new NullPointerException("Null task pointer");
        }
    }

    public Task getTask() {
        return task;
    }

    public boolean validate() {
        return (task != null);
    }

    @Override
    public void run() {
        task.run();
    }
}

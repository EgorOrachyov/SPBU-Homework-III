package Server;

import Filter.FilterBehavior;
import Misc.PluginLoader;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;

public class Configuration {

    private volatile int port;

    private volatile int numOfClients = 0;
    private volatile int maxNumOfClients = -1;
    private volatile boolean isNumOfClientsLimited = false;

    private volatile int timeOut;
    private volatile boolean isServerShutDown;

    private ConnectionPool connectionPool;
    private TaskPool taskPool;

    private String filtersBehaviors;
    private ArrayList<Class> filters;

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setFiltersBehaviors(String filtersBehaviors) {
        PluginLoader<FilterBehavior> loader = new PluginLoader<>(filtersBehaviors);

        this.filters = loader.getElements();
        this.filtersBehaviors = filtersBehaviors;
    }

    public String getFiltersBehaviors() {
        return filtersBehaviors;
    }

    public ArrayList<Class> getFilters() {
        return filters;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public synchronized void incrementClients() {
        numOfClients += 1;
    }

    public synchronized void decrementClients() {
        if (numOfClients > 0) numOfClients -= 1;
    }

    public int getNumOfClients() {
        return numOfClients;
    }

    public void setMaxNumOfClients(int maxNumOfClients) {
        this.maxNumOfClients = maxNumOfClients;
    }

    public int getMaxNumOfClients() {
        return maxNumOfClients;
    }

    public boolean isNumOfClientsLimited() {
        return isNumOfClientsLimited;
    }

    public void setNumOfClientsLimited(boolean numOfClientsLimited) {
        isNumOfClientsLimited = numOfClientsLimited;
    }

    public boolean isServerShutDown() {
        return isServerShutDown;
    }

    public synchronized void setServerShutDown(boolean serverShutDown) {
        isServerShutDown = serverShutDown;
    }

    public void setConnectionPoolProperties(long initialDelay, long period, int threadsCount) {
        connectionPool = new ConnectionPool(initialDelay, period, threadsCount);
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public void setTaskPoolProperties(int threadsCount) {
        taskPool = new TaskPool(threadsCount);
    }

    public TaskPool getTaskPool() {
        return taskPool;
    }
}

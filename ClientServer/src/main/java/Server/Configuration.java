package Server;

public class Configuration {

    private volatile int port;

    private volatile int numOfClients = 0;
    private volatile int maxNumOfClients = -1;
    private volatile boolean isNumOfClientsLimited = false;

    private volatile boolean isServerShutDown;

    private long connectionPoolInitialDelay;
    private long connectionPoolRatePeriod;
    private int  connectionPoolThreadsCount;

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

    public int getMaxNumOfClients() {
        return maxNumOfClients;
    }

    public boolean isNumOfClientsLimited() {
        return isNumOfClientsLimited;
    }

    public boolean isServerShutDown() {
        return isServerShutDown;
    }

    public synchronized void setServerShutDown(boolean serverShutDown) {
        isServerShutDown = serverShutDown;
    }

    public void setConnectionPoolProperties(long initialDelay, long period, int threadsCount) {
        connectionPoolInitialDelay = initialDelay;
        connectionPoolRatePeriod = period;
        connectionPoolThreadsCount = threadsCount;
    }

    public int getConnectionPoolThreadsCount() {
        return connectionPoolThreadsCount;
    }

    public long getConnectionPoolInitialDelay() {
        return connectionPoolInitialDelay;
    }

    public long getConnectionPoolRatePeriod() {
        return connectionPoolRatePeriod;
    }

}

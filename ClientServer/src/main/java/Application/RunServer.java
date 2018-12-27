package Application;

import Server.Configuration;
import Server.ConnectionFactory;

/**
 * Run new instance of server side of application with
 * desired connection properties
 */
public class RunServer {

    public static void main(String ... args) {

        Configuration configuration = new Configuration();
        configuration.setFiltersBehaviors("src/main/java/Plugins/Filter.FilterBehavior");
        configuration.setTimeOut(100000);
        configuration.setTimeOutEnabled(false);
        configuration.setPort(40000);
        configuration.setMaxNumOfClients(1000);
        configuration.setNumOfClientsLimited(false);
        configuration.setTaskPoolProperties(1);
        configuration.setConnectionPoolProperties(1, 1, 1);

        ConnectionFactory connectionFactory = new ConnectionFactory(configuration);
        connectionFactory.run();

    }

}

package Server;

public class Server {

    public static void main(String ... args) {

        Configuration configuration = new Configuration();
        configuration.setFiltersBehaviors("src/main/java/Plugins/Filter.FilterBehavior");
        configuration.setTimeOut(100000);
        configuration.setPort(8813);
        configuration.setMaxNumOfClients(1);
        configuration.setNumOfClientsLimited(true);
        configuration.setTaskPoolProperties(2);
        configuration.setConnectionPoolProperties(1000, 10, 2);

        ConnectionFactory connectionFactory = new ConnectionFactory(configuration);
        connectionFactory.run();

    }

}

package Server;

public class Server {

    public static void main(String ... args) {

        Configuration configuration = new Configuration();
        configuration.setFiltersBehaviors("src/main/java/Plugins/Filter.FilterBehavior");
        configuration.setTimeOut(100000);
        configuration.setPort(40000);
        configuration.setMaxNumOfClients(1000);
        configuration.setNumOfClientsLimited(false);
        configuration.setTaskPoolProperties(4);
        configuration.setConnectionPoolProperties(1000, 10, 4);

        ConnectionFactory connectionFactory = new ConnectionFactory(configuration);
        connectionFactory.run();

    }

}

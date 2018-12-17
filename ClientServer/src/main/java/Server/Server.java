package Server;

public class Server {

    public static void main(String ... args) {

        Configuration configuration = new Configuration();
        configuration.setFiltersBehaviors("src/main/java/Plugins/Filter.FilterBehavior");
        configuration.setTimeOut(1000);
        configuration.setPort(8813);
        configuration.setTaskPoolProperties(2);
        configuration.setConnectionPoolProperties(2000, 1000, 2);

        ConnectionFactory connectionFactory = new ConnectionFactory(configuration);
        connectionFactory.run();

    }

}

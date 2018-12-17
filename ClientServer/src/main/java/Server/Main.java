package Server;

public class Main {

    public static void main(String ... args) {

        Configuration configuration = new Configuration();
        configuration.setFiltersBehaviors("src/main/java/Plugins/Filter.FilterBehavior");
        configuration.setPort(8813);
        configuration.setConnectionPoolProperties(2000, 1000, 2);

        ConnectionFactory connectionFactory = new ConnectionFactory(configuration);
        connectionFactory.run();

    }

}

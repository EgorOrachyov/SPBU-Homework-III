package Server;

public class Main {

    public static void main(String ... args) {

        Configuration configuration = new Configuration();
        configuration.setPort(3345);
        configuration.setConnectionPoolProperties(2000, 1000, 4);

        ConnectionFactory connectionFactory = new ConnectionFactory(configuration);
        connectionFactory.run();

    }

}

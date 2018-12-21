package Filter;

public class FilterInfo {

    private final int id;
    private final String name;
    private final String description;

    public FilterInfo(String name, String description, int id) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}

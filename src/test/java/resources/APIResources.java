package resources;
public enum APIResources {
    BOARD("/boards"),
    LIST("/lists"),
    CARD("/cards"),
    CHECKLIST("/checklists");

    private final String resource;

    APIResources(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

    public String getResourceWithId() {
        return resource + "/{id}";
    }
    public String getResourceArchive() {
        return resource + "/{id}/closed";
    }

    public static APIResources fromString(String text) {
        for (APIResources b : APIResources.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
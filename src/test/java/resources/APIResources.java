package resources;

public enum APIResources {
    // Board
    CreateBoardAPI("/boards"),
    GetBoardAPI("/members/me/boards"),
    DeleteBoardAPI("/boards/{id}"),
    UPDATEBoardAPI("/boards/{id}"),
    // List
    CreateListAPI("/lists"),
    GetListAPI("/lists/{id}"),
    UpdateListAPI("/lists/{id}"),
    ArchiveListAPI("/lists/{id}"),
    // Label
    CreateLabelAPI("/labels"),
    GetLabelAPI("/labels/{id}"),
    UpdateLabelAPI("/labels/{id}"),
    DeleteLabelAPI("/labels/{id}");

    private String resource;

    APIResources(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}


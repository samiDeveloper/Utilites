package bs;

public class DemoObject {
    private Long id;

    private String title;

    public DemoObject() {
    }

    public DemoObject(long id) {
        this.id= id;
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

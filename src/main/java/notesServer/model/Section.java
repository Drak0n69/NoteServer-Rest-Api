package notesServer.model;

import java.util.Objects;

public class Section {

    private long id;
    private String name;
    private User creator;

    public Section() {
    }

    public Section(String name, User creator) {
        this.name = name;
        this.creator = creator;
    }

    public Section(long id, String name, User creator) {
        this(name, creator);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Section)) return false;
        Section section = (Section) o;
        return id == section.id &&
                Objects.equals(name, section.name) &&
                Objects.equals(creator, section.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, creator);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creator=" + creator +
                '}';
    }
}

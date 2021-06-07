package notesServer.dto.response.section;

public class SectionInfoResponseDto {

    private long id;
    private String name;

    public SectionInfoResponseDto(long id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "SectionInfoResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

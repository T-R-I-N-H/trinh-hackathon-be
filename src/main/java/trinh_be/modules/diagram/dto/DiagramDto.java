package trinh_be.modules.diagram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import trinh_be.modules.diagram.model.Diagram;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DiagramDto {

    @JsonProperty("diagram_id")
    private String id;

    @JsonProperty("diagram_data")
    private String data;

    @JsonProperty("diagram_name")
    private String name;

    @JsonProperty("diagram_description")
    private String description;

    @JsonProperty("files")
    private List<Diagram.FileDto> files;

    public DiagramDto(Diagram diagram) {
        this.id = diagram.getId();
        this.data = diagram.getData();
        this.name = diagram.getName();
        this.description = diagram.getDescription();
        this.files = new ArrayList<>(diagram.getFiles());
    }
}

package trinh_be.modules.diagram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ModifyDiagramRequest {

    @JsonProperty("diagram_id")
    private String diagramId;

    @JsonProperty("diagram_data")
    private String data;
}

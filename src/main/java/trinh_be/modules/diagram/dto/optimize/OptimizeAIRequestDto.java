package trinh_be.modules.diagram.dto.optimize;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

@Setter
public class OptimizeAIRequestDto {

    @JsonProperty("diagram_data")
    private String diagramData;

    @JsonProperty("memory")
    private String memory;
}

package trinh_be.modules.diagram.dto.optimize;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OptimizeRequestDto {

    @JsonProperty("diagram_id")
    private String diagramId;
}

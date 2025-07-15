package trinh_be.modules.diagram.dto.benchmark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BenchmarkRequestDto {

    @JsonProperty("diagram_id")
    private String diagramId;
}

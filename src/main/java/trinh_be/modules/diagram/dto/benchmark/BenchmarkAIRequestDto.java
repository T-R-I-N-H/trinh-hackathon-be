package trinh_be.modules.diagram.dto.benchmark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BenchmarkAIRequestDto {

    @JsonProperty("diagram_data")
    private String diagramData;

    @JsonProperty("memory")
    private String memory;
}

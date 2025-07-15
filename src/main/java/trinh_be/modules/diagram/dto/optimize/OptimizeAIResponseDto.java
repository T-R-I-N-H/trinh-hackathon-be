package trinh_be.modules.diagram.dto.optimize;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@Getter
public class OptimizeAIResponseDto {

    @JsonProperty("diagram_data")
    private String data;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("memory")
    private String memory;

    @JsonProperty("detail_descriptions")
    private Map<String, String> nodeDescriptions;

    @JsonProperty("optimization_detail")
    private Map<String, String> optimizationDetail;
}

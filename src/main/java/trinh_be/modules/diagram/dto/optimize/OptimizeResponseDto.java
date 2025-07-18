package trinh_be.modules.diagram.dto.optimize;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

import java.util.Map;

@Setter
public class OptimizeResponseDto {

    @JsonProperty("data")
    private String diagramData;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("optimization_detail")
    private Map<String, String> optimizationDetail;

    public OptimizeResponseDto(OptimizeAIResponseDto response, String diagramData) {
        this.diagramData = response.getData();
        this.answer = response.getAnswer();
        this.optimizationDetail = response.getOptimizationDetail();
    }
}

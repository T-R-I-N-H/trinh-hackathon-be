package trinh_be.modules.diagram.conversation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AIChatResponse {

    @JsonProperty("action")
    private String action;

    @JsonProperty("diagram_data")
    private String diagramData;

    @JsonProperty("detail_descriptions")
    private Map<String, String> nodeDescriptions;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("memory")
    private String memory;
}

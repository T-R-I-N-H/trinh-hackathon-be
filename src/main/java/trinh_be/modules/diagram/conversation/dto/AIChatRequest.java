package trinh_be.modules.diagram.conversation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIChatRequest {

    @JsonProperty("prompt")
    private String prompt;

    @JsonProperty("diagram_data")
    private String diagramData;

    @JsonProperty("memory")
    private String memory;
}

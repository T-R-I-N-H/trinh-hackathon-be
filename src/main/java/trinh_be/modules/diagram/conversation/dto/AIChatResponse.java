package trinh_be.modules.diagram.conversation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIChatResponse {

    @JsonProperty("action")
    private String action;

    @JsonProperty("data")
    private String diagramData;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("memory")
    private String memory;
}

package trinh_be.modules.diagram.conversation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequestDto {

    @JsonProperty("diagram_id")
    private String diagramId;

    @JsonProperty("prompt")
    private String prompt;
}

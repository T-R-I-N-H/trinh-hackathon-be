package trinh_be.modules.diagram.conversation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatResponseDto {

    @JsonProperty("action")
    private String action;

    @JsonProperty("data")
    private String diagramData;

    @JsonProperty("answer")
    private String answer;

    public ChatResponseDto(AIChatResponse response) {
        this.action = response.getAction();
        this.diagramData = response.getDiagramData();
        this.answer = response.getAnswer();
    }
}

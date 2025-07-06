package trinh_be.modules.diagram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class VisualizeAndDescriptionAIRequest {

    @JsonProperty("prompt")
    private String prompt;

    @JsonProperty("file_texts")
    private List<String> fileTexts;
}

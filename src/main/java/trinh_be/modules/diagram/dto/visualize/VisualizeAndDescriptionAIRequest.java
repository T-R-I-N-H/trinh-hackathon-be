package trinh_be.modules.diagram.dto.visualize;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
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
    private List<AIFileDto> files;

    @AllArgsConstructor
    public static class AIFileDto {
        @JsonProperty("file_type")
        private String fileType;

        @JsonProperty("file_content")
        private String fileContent;
    }
}

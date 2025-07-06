package trinh_be.modules.diagram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class VisualizeAndDescriptionAIResponse {

    @JsonProperty("diagram_data")
    private String data;

    @JsonProperty("diagram_name")
    private String name;

    @JsonProperty("diagram_description")
    private String description;

    @JsonProperty("memory")
    private String memory;

    @JsonProperty("detail_descriptions")
    private Map<String, String> nodeDescriptions;
}

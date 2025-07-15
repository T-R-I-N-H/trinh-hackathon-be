package trinh_be.modules.diagram.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import trinh_be.modules.diagram.dto.optimize.OptimizeAIResponseDto;

import java.util.List;
import java.util.Map;

@Document("diagrams")
@Getter
@Setter
@Builder
public class Diagram {

    @Id
    private String id;

    private String name;

    private String ownerEmail;

    private String ownerName;

    private String data;

    private String description;

    private Map<String, String> nodeDescriptions;

    private Map<String, String> optimizationDetail;

    private List<FileDto> files;

    private String memory;

    public String getMemory() {
        return memory == null ? "" : memory;
    }

    public static Diagram optimize(Diagram diagram, OptimizeAIResponseDto response) {
        diagram.setData(response.getData());
        diagram.setNodeDescriptions(response.getNodeDescriptions());
        diagram.setOptimizationDetail(response.getOptimizationDetail());
        diagram.setMemory(diagram.getMemory() + ";\n" + response.getMemory());

        return diagram;
    }

    @Getter
    @Setter
    public static class FileDto {
        private String name;
        private String type;
        private String url;
    }
}



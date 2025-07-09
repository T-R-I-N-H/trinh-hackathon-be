package trinh_be.modules.diagram.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private List<FileDto> files;

    private String memory;

    @Getter
    @Setter
    public static class FileDto {
        private String name;
        private String type;
        private String url;
    }
}



package trinh_be.modules.diagram.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trinh_be.common.api.ApiCaller;
import trinh_be.common.s3.S3FileService;
import trinh_be.config.ServerConfig;
import trinh_be.modules.diagram.dto.DiagramDto;
import trinh_be.modules.diagram.dto.VisualizeAndDescriptionAIRequest;
import trinh_be.modules.diagram.dto.VisualizeAndDescriptionAIResponse;
import trinh_be.modules.diagram.model.Diagram;
import trinh_be.modules.user.model.User;
import trinh_be.utils.FileUtils;
import trinh_be.utils.SpringContextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiagramService {

    private final MongoTemplate mongoTemplate;

    public List<DiagramDto> getUserDiagrams(User user) {
        return mongoTemplate
                .find(new Query(Criteria.where("ownerEmail").is(user.getEmail())), Diagram.class)
                .stream()
                .map(DiagramDto::new)
                .toList();
    }

    public DiagramDto getDiagramById(String id) throws BadRequestException {
        Diagram diagram = mongoTemplate.findById(id, Diagram.class);
        if (diagram == null) {
            throw new BadRequestException("Diagram not found");
        }
        return new DiagramDto(diagram);
    }

    public DiagramDto createNewDiagram(User user, String prompt, List<MultipartFile> files) throws IOException, InterruptedException {
        //get response from AI
//        VisualizeAndDescriptionAIResponse response = getAIDiagramData(prompt, files);
        VisualizeAndDescriptionAIResponse response = new VisualizeAndDescriptionAIResponse();
        response.setName("Diagram 01");
        response.setData(parseBPMN(MOCK_BPMN));
        response.setDescription("Here is your diagram");
        response.setMemory("Memory 1 2 3");
        response.setNodeDescriptions(new HashMap<>());

        //save diagram (file handling included)
        Diagram diagram = initDiagram(user, response.getName(), response.getData(), response.getDescription(), response.getNodeDescriptions(), response.getMemory(), files);
        mongoTemplate.save(diagram);

        //TODO: conversation handling: chat history

        return new DiagramDto(diagram);
    }

    private Diagram initDiagram(User user, String name, String data, String description, Map<String, String> nodeDescriptions, String memory, List<MultipartFile> files) throws IOException, InterruptedException {
        List<String> fileUrls = new ArrayList<>();
        files.forEach(file -> {
            String url;
            try {
                url = S3FileService.getInstance().uploadFile(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fileUrls.add(url);
        });

        return Diagram.builder()
                .name(name)
                .ownerEmail(user.getEmail())
                .ownerName(user.getName())
                .data(parseBPMN(data))
                .description(description)
                .nodeDescriptions(new HashMap<>(nodeDescriptions))
                .memory(memory)
                .fileUrls(new ArrayList<>(fileUrls))
                .build();
    }

    private VisualizeAndDescriptionAIResponse getAIDiagramData(String prompt, List<MultipartFile> files) throws IOException, InterruptedException {
        VisualizeAndDescriptionAIRequest request = VisualizeAndDescriptionAIRequest.builder()
                .prompt(prompt)
                .fileTexts(files.stream().map(file -> {
                    try {
                        return FileUtils.convertFileToString(file);
                    } catch (BadRequestException e) {
                        throw new RuntimeException(e);
                    }
                }).toList())
                .build();

        return ApiCaller.post(ServerConfig.AI_URL + "/diagram/visualize-and-description", request, VisualizeAndDescriptionAIResponse.class);
    }

    private String parseBPMN(String data) throws IOException, InterruptedException {
        Map<String, String> payload = new HashMap<>();
        payload.put("data", data);
        return ApiCaller.post(ServerConfig.PARSER_URL + "/parse", payload, String.class);
    }

    private final String MOCK_BPMN = """
            <?xml version="1.0" encoding="UTF-8"?>
            <bpmn:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                              xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL"
                              xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
                              xmlns:dc="http://www.omg.org/spec/DD/20100524/DC"
                              xmlns:di="http://www.omg.org/spec/DD/20100524/DI"
                              id="Definitions_1"
                              targetNamespace="http://bpmn.io/schema/bpmn">
              <bpmn:process id="Process_1" isExecutable="true">
                <bpmn:startEvent id="StartEvent_1" name="Start"/>
                <bpmn:task id="Task_1" name="Do Something"/>
                <bpmn:endEvent id="EndEvent_1" name="End"/>
                <bpmn:sequenceFlow id="Flow_1" sourceRef="StartEvent_1" targetRef="Task_1"/>
                <bpmn:sequenceFlow id="Flow_2" sourceRef="Task_1" targetRef="EndEvent_1"/>
              </bpmn:process>
            </bpmn:definitions>
            """;

    public static DiagramService getInstance() {
        return SpringContextUtils.getSingleton(DiagramService.class);
    }
}

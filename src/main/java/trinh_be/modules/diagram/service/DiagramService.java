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
import trinh_be.modules.diagram.conversation.ConversationService;
import trinh_be.modules.diagram.dto.DiagramDto;
import trinh_be.modules.diagram.dto.benchmark.BenchmarkAIRequestDto;
import trinh_be.modules.diagram.dto.benchmark.BenchmarkAIResponseDto;
import trinh_be.modules.diagram.dto.benchmark.BenchmarkResponseDto;
import trinh_be.modules.diagram.dto.optimize.OptimizeAIRequestDto;
import trinh_be.modules.diagram.dto.optimize.OptimizeAIResponseDto;
import trinh_be.modules.diagram.dto.optimize.OptimizeResponseDto;
import trinh_be.modules.diagram.dto.visualize.VisualizeAndDescriptionAIRequest;
import trinh_be.modules.diagram.dto.visualize.VisualizeAndDescriptionAIResponse;
import trinh_be.modules.diagram.model.Diagram;
import trinh_be.modules.diagram.utils.DiagramUtils;
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
        VisualizeAndDescriptionAIResponse response = getAIDiagramData(prompt, files);

        //save diagram (file handling included)
        Diagram diagram = initDiagram(user, response.getName(), response.getData(), response.getDescription(), response.getNodeDescriptions(), response.getMemory(), files);
        mongoTemplate.save(diagram);

        //TODO: conversation handling: chat history
        ConversationService.getInstance().addMessage(true, diagram.getId(), prompt);
        ConversationService.getInstance().addMessage(false, diagram.getId(), response.getDescription());

        return new DiagramDto(diagram);
    }

    public String getNodeDescription(String diagramId, String nodeId) throws BadRequestException {
        Diagram diagram = mongoTemplate.findById(diagramId, Diagram.class);
        if (diagram == null) {
            throw new BadRequestException("Diagram not found");
        }
        return diagram.getNodeDescriptions().get(nodeId);
    }

    public DiagramDto modifyDiagram(User user, String diagramId, String data) throws BadRequestException {
        Diagram diagram = mongoTemplate.findById(diagramId, Diagram.class);
        if (!DiagramUtils.validDiagram(user, diagram)) {
            throw new BadRequestException("Invalid diagram: Wrong owner");
        }

        diagram.setData(data);
        mongoTemplate.save(diagram);
        return new DiagramDto(diagram);
    }

    public DiagramDto removeDiagram(User user, String diagramId) throws BadRequestException {
        Diagram diagram = mongoTemplate.findById(diagramId, Diagram.class);
        if (!DiagramUtils.validDiagram(user, diagram)) {
            throw new BadRequestException("Invalid diagram: Wrong owner");
        }

        mongoTemplate.remove(diagram);
        return new DiagramDto(diagram);
    }

    public OptimizeResponseDto optimize(User user, String diagramId) throws IOException, InterruptedException {
        Diagram diagram = mongoTemplate.findById(diagramId, Diagram.class);
        if (!DiagramUtils.validDiagram(user, diagram)) {
            throw new BadRequestException("Invalid diagram: Wrong owner");
        }

        OptimizeAIRequestDto request = new OptimizeAIRequestDto();
        request.setDiagramData(diagram.getData());
        request.setMemory(diagram.getMemory());

        OptimizeAIResponseDto response = ApiCaller.post(
                ServerConfig.AI_VISUALIZE_URL + "/interaction/optimize",
                request,
                OptimizeAIResponseDto.class
        );

        Diagram.optimize(diagram, response);
        diagram.setData(parseBPMN(diagram.getData()));
        mongoTemplate.save(diagram);

        return new OptimizeResponseDto(response, diagram.getData());
    }

    public BenchmarkResponseDto benchmark(User user, String diagramId) throws IOException, InterruptedException {
        Diagram diagram = mongoTemplate.findById(diagramId, Diagram.class);
        if (!DiagramUtils.validDiagram(user, diagram)) {
            throw new BadRequestException("Invalid diagram: Wrong owner");
        }

        BenchmarkAIRequestDto request = new BenchmarkAIRequestDto(diagram.getData(), diagram.getMemory());
        BenchmarkAIResponseDto response = ApiCaller.post(
                ServerConfig.AI_VISUALIZE_URL + "/interaction/benchmark",
                request,
                BenchmarkAIResponseDto.class
        );

        return new BenchmarkResponseDto(response);
    }

    private Diagram initDiagram(User user, String name, String data, String description, Map<String, String> nodeDescriptions, String memory, List<MultipartFile> files) throws IOException, InterruptedException {
        List<Diagram.FileDto> fileDtos = new ArrayList<>();
        files.forEach(file -> {
            String url;
            try {
                url = S3FileService.getInstance().uploadFile(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Diagram.FileDto fileDto = new Diagram.FileDto();
            fileDto.setName(file.getOriginalFilename());
            fileDto.setType(FileUtils.getFileExtension(file));
            fileDto.setUrl(url);
            fileDtos.add(fileDto);
        });

        return Diagram.builder()
                .name(name)
                .ownerEmail(user.getEmail())
                .ownerName(user.getName())
                .data(parseBPMN(data))
                .description(description)
                .nodeDescriptions(new HashMap<>(nodeDescriptions))
                .memory(memory)
                .files(new ArrayList<>(fileDtos))
                .build();
    }

    private VisualizeAndDescriptionAIResponse getAIDiagramData(String prompt, List<MultipartFile> attachedFiles) throws IOException, InterruptedException {
        VisualizeAndDescriptionAIRequest request = VisualizeAndDescriptionAIRequest.builder()
                .prompt(prompt)
                .files(attachedFiles.stream().map(file -> {
                    String content;
                    try {
                        content = FileUtils.convertFileToString(file);
                    } catch (BadRequestException e) {
                        throw new RuntimeException(e);
                    }

                    return new VisualizeAndDescriptionAIRequest.AIFileDto(
                            FileUtils.getFileExtension(file),
                            content
                    );
                }).toList())
                .build();

        return ApiCaller.post(ServerConfig.AI_VISUALIZE_URL + "/visualize/visualize", request, VisualizeAndDescriptionAIResponse.class);
    }

    public String parseBPMN(String data) throws IOException, InterruptedException {
//        Map<String, String> payload = new HashMap<>();
//        payload.put("data", data);
//        return ApiCaller.post(ServerConfig.PARSER_URL + "/parse", payload, String.class);
        return data;
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

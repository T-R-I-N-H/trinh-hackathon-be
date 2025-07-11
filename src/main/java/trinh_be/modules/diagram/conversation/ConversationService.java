package trinh_be.modules.diagram.conversation;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import trinh_be.common.api.ApiCaller;
import trinh_be.config.ServerConfig;
import trinh_be.modules.diagram.conversation.dto.AIChatRequest;
import trinh_be.modules.diagram.conversation.dto.AIChatResponse;
import trinh_be.modules.diagram.conversation.dto.ChatResponseDto;
import trinh_be.modules.diagram.conversation.model.DiagramConversation;
import trinh_be.modules.diagram.model.Diagram;
import trinh_be.modules.diagram.service.DiagramService;
import trinh_be.modules.user.model.User;
import trinh_be.utils.SpringContextUtils;

import java.io.IOException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final MongoTemplate mongoTemplate;

    public DiagramConversation getUserConversation(User user, String diagramId) throws BadRequestException {
        Diagram diagram = mongoTemplate.find(new Query(Criteria.where("_id").is(diagramId)), Diagram.class)
                .stream().findFirst().orElse(null);
        if (!validDiagram(user, diagram)) {
            throw new BadRequestException("Invalid diagram");
        }

        return getConversation(diagramId);
    }

    public ChatResponseDto chat(User user, String diagramId, String message) throws IOException, InterruptedException {
        Diagram diagram = mongoTemplate.find(new Query(Criteria.where("_id").is(diagramId)), Diagram.class)
                .stream().findFirst().orElse(null);
        if (!validDiagram(user, diagram)) {
            throw new BadRequestException("Invalid diagram");
        }

        //call AI
        AIChatRequest request = new AIChatRequest();
        request.setPrompt(message);
        request.setDiagramData(diagram.getData());
        request.setMemory(diagram.getMemory());
        AIChatResponse response = chatAI(request);

        //modify diagram (if needed)
        handleAIChatResponse(response, diagram);

        //add messages
        addMessage(true, diagramId, message);
        addMessage(false, diagramId, response.getAnswer());

        return new ChatResponseDto(response);
    }

    public void addMessage(boolean fromUser, String diagramId, String message) {
        DiagramConversation conversation = getConversation(diagramId);
        if (conversation == null) {
            conversation = new DiagramConversation();
            conversation.setDiagramId(diagramId);
            conversation.setMessages(new ArrayList<>());
        }

        String sender = fromUser ? "user" : "system";
        conversation.getMessages().add(new DiagramConversation.ConversationMessage(sender, message, System.currentTimeMillis()));
        mongoTemplate.save(conversation);
    }

    private void handleAIChatResponse(AIChatResponse response, Diagram diagram) throws IOException, InterruptedException {
        response.setDiagramData(DiagramService.getInstance().parseBPMN(response.getDiagramData()));

        if (response.getAction().equals("modify_diagram")) {
            diagram.setData(response.getDiagramData());
            diagram.setNodeDescriptions(response.getNodeDescriptions());
        }

        if (!response.getMemory().isEmpty()) {
            diagram.setMemory(diagram.getMemory() + ";\n" + response.getMemory());
        }

        mongoTemplate.save(diagram);
    }

    private AIChatResponse chatAI(AIChatRequest request) throws IOException, InterruptedException {
        return ApiCaller.post(ServerConfig.AI_CONVERSATION_URL + "/conversation", request, AIChatResponse.class);
    }

    private boolean validDiagram(User user, Diagram diagram) {
        if (diagram == null) {
            return false;
        }
        return diagram.getOwnerEmail().equals(user.getEmail());
    }

    private DiagramConversation getConversation(String diagramId) {
        return mongoTemplate.find(new Query(Criteria.where("diagramId").is(diagramId)), DiagramConversation.class)
                .stream().findFirst().orElse(null);
    }

    public static ConversationService getInstance() {
        return SpringContextUtils.getSingleton(ConversationService.class);
    }
}

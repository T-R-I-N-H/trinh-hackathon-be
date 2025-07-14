package trinh_be.modules.diagram.conversation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("diagram_conversation")
@Getter
@Setter
public class DiagramConversation {

    @Id
    private String diagramId;

    private List<ConversationMessage> messages;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ConversationMessage {
        private String sender;
        private String message;
        private Date createdAt;

        public ConversationMessage(String sender, String message) {
            this.sender = sender;
            this.message = message;
            this.createdAt = new Date();
        }
    }
}

package trinh_be.modules.diagram.conversation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import trinh_be.common.api.ApiResponse;
import trinh_be.modules.diagram.conversation.dto.ChatRequestDto;
import trinh_be.modules.diagram.conversation.dto.ChatResponseDto;
import trinh_be.modules.diagram.conversation.model.DiagramConversation;
import trinh_be.modules.user.model.User;
import trinh_be.modules.user.service.UserService;

import java.io.IOException;

@Tag(name = "Conversation", description = "Conversation API")
@RestController
@RequestMapping(("/api/conversation"))
public class ConversationController {

    @GetMapping("/{diagram_id}")
    @Operation(description = "Get diagram conversation")
    public ApiResponse<DiagramConversation> getConversation(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String diagram_id
    ) throws BadRequestException {
        User user = UserService.getInstance().getByEmail(userDetails.getUsername());
        return ApiResponse.success(ConversationService.getInstance().getUserConversation(
                user,
                diagram_id
                )
        );
    }

    @PostMapping("")
    @Operation(description = "Chat with AI system")
    public ApiResponse<ChatResponseDto> chat(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChatRequestDto request
    ) throws IOException, InterruptedException {
        User user = UserService.getInstance().getByEmail(userDetails.getUsername());
        return ApiResponse.success(ConversationService.getInstance().chat(user, request.getDiagramId(), request.getPrompt()));
    }
}

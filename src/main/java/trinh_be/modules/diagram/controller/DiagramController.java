package trinh_be.modules.diagram.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trinh_be.common.api.ApiResponse;
import trinh_be.modules.auth.config.CustomUserDetails;
import trinh_be.modules.diagram.dto.DiagramDto;
import trinh_be.modules.diagram.dto.ModifyDiagramRequest;
import trinh_be.modules.diagram.dto.benchmark.BenchmarkRequestDto;
import trinh_be.modules.diagram.dto.benchmark.BenchmarkResponseDto;
import trinh_be.modules.diagram.dto.optimize.OptimizeRequestDto;
import trinh_be.modules.diagram.dto.optimize.OptimizeResponseDto;
import trinh_be.modules.diagram.service.DiagramService;
import trinh_be.modules.user.model.User;
import trinh_be.modules.user.service.UserService;

import java.io.IOException;
import java.util.List;

@Tag(name = "Diagram", description = "Diagram APIs")
@RestController
@RequestMapping("/api/diagrams")
public class DiagramController {

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<DiagramDto>>> getMyDiagrams(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = UserService.getInstance().getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(DiagramService.getInstance().getUserDiagrams(user)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DiagramDto>> getDiagramById(
            @PathVariable String id
    ) throws BadRequestException {
        return ResponseEntity.ok(ApiResponse.success(DiagramService.getInstance().getDiagramById(id)));
    }

    @PostMapping("")
    @Operation(description = "Initialize a new diagram")
    public ResponseEntity<ApiResponse<DiagramDto>> createNewDiagram(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            String prompt,
            List<MultipartFile> files
    ) throws IOException, InterruptedException {
        User user = UserService.getInstance().getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(
                DiagramService.getInstance().createNewDiagram(user, prompt, files)
        ));
    }

    @GetMapping("/{diagram_id}/{node_id}")
    @Operation(description = "Get specific node description")
    public ResponseEntity<ApiResponse<String>> getNodeDescription(
            @PathVariable String diagram_id,
            @PathVariable String node_id
    ) throws BadRequestException {
        return ResponseEntity.ok(ApiResponse.success(DiagramService.getInstance().getNodeDescription(diagram_id, node_id)));
    }

    @PutMapping("")
    @Operation(description = "Modify a diagram")
    public ResponseEntity<ApiResponse<DiagramDto>> modifyDiagram(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ModifyDiagramRequest request
            ) throws BadRequestException {
        User user = UserService.getInstance().getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(DiagramService.getInstance().
                modifyDiagram(user, request.getDiagramId(), request.getData()))
        );
    }

    @DeleteMapping("")
    @Operation(description = "Delete a diagram")
    public ResponseEntity<ApiResponse<DiagramDto>> deleteDiagram(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String diagram_id
    ) throws BadRequestException {
        User user = UserService.getInstance().getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(DiagramService.getInstance().removeDiagram(user, diagram_id)));
    }

    @PostMapping("/optimize")
    @Operation(description = "Optimize a diagram")
    public ResponseEntity<ApiResponse<OptimizeResponseDto>> optimizeDiagram(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody OptimizeRequestDto request
            ) throws IOException, InterruptedException {
        User user = UserService.getInstance().getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(DiagramService.getInstance().optimize(user, request.getDiagramId())));
    }

    @PostMapping("/benchmark")
    @Operation(description = "Benchmark a diagram")
    public ResponseEntity<ApiResponse<BenchmarkResponseDto>> benchmarkDiagram(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BenchmarkRequestDto request
            ) throws IOException, InterruptedException {
        User user = UserService.getInstance().getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(DiagramService.getInstance().benchmark(user, request.getDiagramId())));
    }
}

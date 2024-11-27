package org.dannyjelll.bpm_validator_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bpmn")
@Tag(name = "BPMN Validation", description = "Endpoints for validating BPMN files")
public class BpmnValidationController {

    @PostMapping(value = "/validate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Validate BPMN File",
            description = "Validates a BPMN file using Camunda's validation mechanism",
            requestBody = @RequestBody(
                    description = "BPMN file to be validated",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "object", format = "binary")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "BPMN file validated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ValidationResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "BPMN validation failed",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ValidationResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Map<String, Object>> validateBpmn(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        System.out.println(file.getOriginalFilename());

        if (file.isEmpty()) {
            response.put("result", false);
            response.put("error", "File is empty");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            BpmnModelInstance modelInstance = Bpmn.readModelFromStream(file.getInputStream());
            Bpmn.validateModel(modelInstance);

            response.put("result", true);
            response.put("error", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Helper schema class for Swagger documentation
    private static class ValidationResponse {
        public Boolean result;
        public String error;
    }
}
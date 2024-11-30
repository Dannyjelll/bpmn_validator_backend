package org.dannyjelll.bpm_validator_backend.config;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Tag(name = "BPMN Validation", description = "Endpoints for validating BPMN files")
    public @interface BpmnValidationTag {}

    @RequestBody(
            description = "BPMN XML string to be validated",
            content = @Content(
                    mediaType = "application/xml",
                    schema = @Schema(type = "string")
            )
    )
    public @interface BpmnXmlRequestBody {}

    @ApiResponse(
            responseCode = "200",
            description = "BPMN XML validated successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ValidationResponse.class)
            )
    )
    public @interface BpmnXmlValidationSuccessResponse {}

    @ApiResponse(
            responseCode = "400",
            description = "BPMN validation failed",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ValidationResponse.class)
            )
    )
    public @interface BpmnXmlValidationErrorResponse {}

    public static class ValidationResponse {
        public Boolean result;
        public String error;
    }
}
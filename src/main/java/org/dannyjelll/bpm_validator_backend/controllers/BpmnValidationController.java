package org.dannyjelll.bpm_validator_backend.controllers;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for BPMN validation.
 */
@RestController
@RequestMapping("/api/bpmn")
public class BpmnValidationController {
    /**
     * Validates a BPMN file.
     *
     * @param file the BPMN file to validate
     * @return a ResponseEntity containing the validation result and any error messages
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateBpmn(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        System.out.println(file.getOriginalFilename());
        // Check if the file is empty
        if (file.isEmpty()) {
            response.put("result", false);
            response.put("error", "File is empty");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Read the BPMN model from the file input stream
            BpmnModelInstance modelInstance = Bpmn.readModelFromStream(file.getInputStream());

            // Validate the BPMN model
            Bpmn.validateModel(modelInstance);

            // If validation succeeds
            response.put("result", true);
            response.put("error", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // If validation fails
            response.put("result", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
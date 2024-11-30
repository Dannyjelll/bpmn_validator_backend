package org.dannyjelll.bpm_validator_backend.controllers;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")

public class BpmnValidationController {

    @PostMapping(value = "/validate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    @PostMapping(value = "/validate/xml", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<Map<String, Object>> validateBpmnXml(@RequestBody String bpmnXml) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("XML: " + bpmnXml);
        if (bpmnXml == null || bpmnXml.isEmpty()) {
            response.put("result", false);
            response.put("error", "XML content is empty");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            BpmnModelInstance modelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream(bpmnXml.getBytes()));
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
}
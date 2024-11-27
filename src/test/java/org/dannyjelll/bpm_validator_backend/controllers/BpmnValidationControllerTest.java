package org.dannyjelll.bpm_validator_backend.controllers;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BpmnValidationControllerTest {

    private BpmnValidationController controller;

    @BeforeEach
    void setUp() {
        controller = new BpmnValidationController();
    }

    @Test
    void validateBpmnWithValidFile() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "valid.bpmn", "text/xml", "<bpmn></bpmn>".getBytes());
        BpmnModelInstance modelInstance = mock(BpmnModelInstance.class);
        try (var mockedBpmn = Mockito.mockStatic(Bpmn.class)) {
            mockedBpmn.when(() -> Bpmn.readModelFromStream(Mockito.any(InputStream.class))).thenReturn(modelInstance);

            ResponseEntity<Map<String, Object>> response = controller.validateBpmn(file);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue((Boolean) response.getBody().get("result"));
            assertEquals(null, response.getBody().get("error"));
        }
    }

    @Test
    void validateBpmnWithInvalidFile() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "invalid.bpmn", "text/xml", "<bpmn>".getBytes());
        try (var mockedBpmn = Mockito.mockStatic(Bpmn.class)) {
            mockedBpmn.when(() -> Bpmn.readModelFromStream(Mockito.any(InputStream.class))).thenThrow(new RuntimeException("Invalid BPMN file"));

            ResponseEntity<Map<String, Object>> response = controller.validateBpmn(file);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse((Boolean) response.getBody().get("result"));
            assertEquals("Invalid BPMN file", response.getBody().get("error"));
        }
    }
    @Test
    void validateBpmnWithEmptyFile() {
        MultipartFile file = new MockMultipartFile("file", "empty.bpmn", "text/xml", new byte[0]);

        ResponseEntity<Map<String, Object>> response = controller.validateBpmn(file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("result"));
        assertEquals("File is empty", response.getBody().get("error"));
    }

}
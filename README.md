# BPMN Validator Backend

This backend provides a REST API for validating BPMN (Business Process Model and Notation) files. It uses the Camunda BPMN model API to read and validate BPMN files uploaded by users.

## Endpoints

### Validate BPMN File

- **URL:** `/api/bpmn/validate`
- **Method:** `POST`
- **Description:** Validates a BPMN file uploaded by the user.
- **Request Parameters:**
  - `file` (MultipartFile): The BPMN file to validate.
- **Response:**
  - `result` (boolean): `true` if the validation is successful, `false` otherwise.
  - `error` (string): Error message if the validation fails, `null` if the validation is successful.

## Sample cURL Command

To validate a BPMN file using the API, you can use the following cURL command:

```sh
curl -X POST http://localhost:8080/api/bpmn/validate \
  -F "file=@path/to/your/bpmnfile.bpmn"
```

Replace `path/to/your/bpmnfile.bpmn` with the actual path to your BPMN file.

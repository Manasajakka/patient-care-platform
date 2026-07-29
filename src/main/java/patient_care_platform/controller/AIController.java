package patient_care_platform.controller;

import patient_care_platform.model.AIRequest;
import patient_care_platform.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/ask")
    public ResponseEntity<?> ask(@RequestBody AIRequest request) {
        try {
            String answer = aiService.askQuestion(request.getQuestion());
            return ResponseEntity.ok(answer);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("AI service error: " + e.getMessage());
        }
    }
}

package com.popzii.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @PostMapping("/initiate")
    public ResponseEntity<Map<String, String>> initiatePayment(@RequestBody Map<String, Object> paymentRequest) {
        // Mock payment initiation
        return ResponseEntity.ok(Map.of("status", "success", "transactionId", "mock_txn_123", "gateway", "stripe"));
    }

    @PostMapping("/confirm")
    public ResponseEntity<Map<String, String>> confirmPayment(@RequestBody Map<String, Object> confirmRequest) {
        // Mock payment confirmation
        return ResponseEntity.ok(Map.of("status", "success", "message", "Payment processed successfully"));
    }

    @PostMapping("/refund")
    public ResponseEntity<Map<String, String>> refundPayment(@RequestBody Map<String, Object> refundRequest) {
        // Mock refund
        return ResponseEntity.ok(Map.of("status", "success", "message", "Refund processed"));
    }
}
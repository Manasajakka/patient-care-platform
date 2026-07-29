package patient_care_platform.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import patient_care_platform.model.Appointment;
import patient_care_platform.model.Payment;
import patient_care_platform.model.PaymentRequest;
import patient_care_platform.repository.AppointmentRepository;
import patient_care_platform.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping("/create-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequest request) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(request.getAppointmentId());

        if (appointmentOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Appointment not found");
        }

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(request.getAmountInCents())
                    .setCurrency("usd")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            Payment payment = new Payment();
            payment.setAppointment(appointmentOptional.get());
            payment.setAmountInCents(request.getAmountInCents());
            payment.setStripePaymentIntentId(paymentIntent.getId());
            payment.setStatus(Payment.PaymentStatus.PENDING);

            paymentRepository.save(payment);

            return ResponseEntity.ok(paymentIntent.getClientSecret());

        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Stripe error: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/{paymentIntentId}")
    public ResponseEntity<?> confirmPayment(@PathVariable String paymentIntentId) {
        Optional<Payment> paymentOptional = paymentRepository.findByStripePaymentIntentId(paymentIntentId);

        if (paymentOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Payment not found");
        }

        Payment payment = paymentOptional.get();
        payment.setStatus(Payment.PaymentStatus.SUCCEEDED);
        paymentRepository.save(payment);

        return ResponseEntity.ok("Payment marked as succeeded");
    }
}
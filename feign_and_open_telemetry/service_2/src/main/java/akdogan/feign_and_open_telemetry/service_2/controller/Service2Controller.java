package akdogan.feign_and_open_telemetry.service_2.controller;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Service2Controller {

    @Autowired
    private Tracer tracer;
    
    
    @GetMapping("/hello/{name}")
    public String sayHello(@PathVariable String name) {
        return "Service 2 says: Hello " + name + "!";
    }
    
    @GetMapping("/status")
    public String getStatus() {
        return "Service 2 is running and healthy!";
    }
    
    @GetMapping("/validate-order/{orderId}")
    public String validateOrder(@PathVariable String orderId) {
        // Create a custom span for order validation
        Span validationSpan = tracer.nextSpan()
                .name("validate-order")
                .tag("order.id", orderId)
                .tag("service", "service-2")
                .tag("operation", "validation")
                .start();

        try (Tracer.SpanInScope ws = tracer.withSpan(validationSpan)) {
            // Simulate validation logic
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Create a nested span for database check
            Span dbSpan = tracer.nextSpan()
                    .name("check-order-database")
                    .tag("operation", "database-query")
                    .start();

            try (Tracer.SpanInScope ws2 = tracer.withSpan(dbSpan)) {
                // Simulate database check
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Simulate validation result
                boolean isValid = orderId.length() > 3 && orderId.matches("\\d+");

                if (isValid) {
                    validationSpan.tag("validation.result", "valid");
                    return "Order " + orderId + " is valid and approved";
                } else {
                    validationSpan.tag("validation.result", "invalid");
                    return "Order " + orderId + " is invalid - must be at least 4 digits";
                }
            } finally {
                dbSpan.end();
            }
        } catch (Exception e) {
            validationSpan.tag("error", "true");
            validationSpan.tag("error.message", e.getMessage());
            return "Error validating order " + orderId + ": " + e.getMessage();
        } finally {
            validationSpan.end();
        }
    }
}

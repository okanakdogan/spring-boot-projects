package akdogan.feign_and_open_telemetry.service_1.controller;

import akdogan.feign_and_open_telemetry.service_1.client.Service2Client;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Service1Controller {
    
    @Autowired
    private Service2Client service2Client;

    @Autowired
    private Tracer tracer;
    
    
    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return "Service 1 says: Hello " + name + "!";
    }
    
    @GetMapping("/call-service2/{name}")
    public String callService2(@PathVariable String name) {
        try {
            String response = service2Client.sayHello(name);
            return "Service 1 calling Service 2: " + response;
        } catch (Exception e) {
            return "Error calling Service 2: " + e.getMessage();
        }
    }
    
    @GetMapping("/service2-status")
    public String getService2Status() {
        try {
            return service2Client.getStatus();
        } catch (Exception e) {
            return "Error getting Service 2 status: " + e.getMessage();
        }
    }
    
    @GetMapping("/process-order/{orderId}")
    public String processOrder(@PathVariable String orderId) {
        // Create a custom span for order processing
        Span orderSpan = tracer.nextSpan()
                .name("process-order")
                .tag("order.id", orderId)
                .tag("service", "service-1")
                .start();

        try (Tracer.SpanInScope ws = tracer.withSpan(orderSpan)) {
            // Simulate some processing time
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Create a nested span for calling service 2
            Span service2Span = tracer.nextSpan()
                    .name("call-service-2")
                    .tag("operation", "validate-order")
                    .start();

            try (Tracer.SpanInScope ws2 = tracer.withSpan(service2Span)) {
                // Call service 2 to validate the order
                String validationResult = service2Client.validateOrder(orderId);

                // Add more processing
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                return "Order " + orderId + " processed successfully. Validation: " + validationResult;
            } finally {
                service2Span.end();
            }
        } catch (Exception e) {
            orderSpan.tag("error", "true");
            orderSpan.tag("error.message", e.getMessage());
            return "Error processing order " + orderId + ": " + e.getMessage();
        } finally {
            orderSpan.end();
        }
    }
}

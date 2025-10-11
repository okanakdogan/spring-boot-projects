package akdogan.feign_and_open_telemetry.service_1.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SERVICE-2")
public interface Service2Client {
    
    @GetMapping("/api/hello/{name}")
    String sayHello(@PathVariable("name") String name);
    
    @GetMapping("/api/status")
    String getStatus();
    
    @GetMapping("/api/validate-order/{orderId}")
    String validateOrder(@PathVariable("orderId") String orderId);
}

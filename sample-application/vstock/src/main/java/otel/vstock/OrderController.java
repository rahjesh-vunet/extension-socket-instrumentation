package otel.vstock;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private SocketService socketService;

    @PostMapping("/place-order")
    public ResponseEntity<Map<String, String>> placeOrder(@RequestBody Order order) {
        // Send order to backend using socket
        String ackMessage = socketService.sendOrderAndGetAck(order);

        Map<String, String> response = new HashMap<>();
        response.put("message", ackMessage);

        return ResponseEntity.ok(response);
    }
}

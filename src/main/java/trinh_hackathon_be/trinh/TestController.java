package trinh_hackathon_be.trinh;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Test endpoint is working! NEW CI-CD IS HERE! HEHEHHE HHHH";
    }
}

package com.github.abhishek_rabidas.Hotel_Integration_API.routes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/checkServerStatus")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Server is live");
    }

}

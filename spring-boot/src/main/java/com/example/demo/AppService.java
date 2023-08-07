package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.vault.core.VaultTemplate;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
public class AppService{

    public static void main(String[] args) {
        SpringApplication.run(AppService.class, args);
    }
}

@Slf4j
@RestController
class VaultController {

    @Autowired
    private Repository messageRepository;

    @Autowired
    private VaultTemplate vaultTemplate;

    private final String TRANSIT_PATH = "transit";
    private final String KEY_NAME = "my-key";

    public String encrypt(String data) {
        return vaultTemplate.opsForTransit(TRANSIT_PATH).encrypt(KEY_NAME, data);
    }

    public String decryptIfNecessary(String data) {
    if (data.startsWith("vault:v1")) {
        return vaultTemplate.opsForTransit(TRANSIT_PATH).decrypt(KEY_NAME, data);
    }
    return data;
}

    @RequestMapping("/user/{userId}")
    public String getUserById(@PathVariable String userId) {
        return "User ID: " + userId;
    }

    @RequestMapping("/insert")
    public String insertMessage(@RequestParam String content) {
        Message message = new Message();
        String encrypted = encrypt(content);

        log.info("=========== Insert with Encrypt - Start ===========");
        log.info("original content: {}", content);
        log.info("encrypted content: {}", encrypted);
        log.info("=========== Insert with Encrypt - End   ===========");

        message.setContent(encrypted);
        messageRepository.save(message);
        return "Message saved!";
    }

    @RequestMapping("/select-encrypted")
    public List<Message> getEncrypted() {
        return messageRepository.findAll();
    }

    @RequestMapping("/select-decrypt")
    public List<Message> getDecrypt() {
        List<Message> messages = messageRepository.findAll();

        for (Message message : messages) {
            String decryptedContent = decryptIfNecessary(message.getContent());
            message.setContent(decryptedContent);
        }

        return messages;
    }
}

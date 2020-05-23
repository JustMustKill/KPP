package com.bsuir.dorox.KPP_Project.Controller;

import com.bsuir.dorox.KPP_Project.domain.Message;
import com.bsuir.dorox.KPP_Project.repo.MessageRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("message")
@RestController
public class message {
    private final MessageRepo messageRepo;

    @Autowired
    public message(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping
    public List<Message> list() {
        return messageRepo.findAll();
    }

    @GetMapping("{id}")
    public Message getOne(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    public Message create(@RequestBody Message message) { return messageRepo.save(message);
    }

    @PutMapping("{id}")
    public Message update(@RequestBody Message message,
                          @PathVariable("id") Message messageFromDB){
        BeanUtils.copyProperties(message, messageFromDB,  "id");
        return messageRepo.save(messageFromDB);
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message){ messageRepo.delete(message);}
//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() {
//        messages.add(new HashMap<String, String>() {{put("name","Third"); put("id", String.valueOf(counter++)); }});;
//    }
}


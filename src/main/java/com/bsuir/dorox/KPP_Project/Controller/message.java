package com.bsuir.dorox.KPP_Project.Controller;

import com.bsuir.dorox.KPP_Project.domain.Message;
import com.bsuir.dorox.KPP_Project.repo.MessageRepo;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("message")
@RestController
@EnableCaching
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
    @Cacheable(cacheNames = "name")
    public Message update(@RequestBody Message message,
                          @PathVariable("id") Message messageFromDB){
        BeanUtils.copyProperties(message, messageFromDB,  "id");
        return messageRepo.save(messageFromDB);
    }
    @DeleteMapping("{id}")
    @org.springframework.transaction.annotation.Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void delete(@PathVariable("id") Message message) {
        Logger log = null;
            try {
            messageRepo.delete(message);
            doExpensiveWork();
        }catch(EmptyResultDataAccessException | InterruptedException e)
        {
            e.getMessage();
        }
}

    private void doExpensiveWork() throws InterruptedException{
        Thread.sleep(5000);
        throw new RuntimeException();
    }
//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() {
//        messages.add(new HashMap<String, String>() {{put("name","Third"); put("id", String.valueOf(counter++)); }});;
//    }
}


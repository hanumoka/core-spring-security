package hanu.example.corespringsecurity.controller;

import hanu.example.corespringsecurity.domain.Test;
import hanu.example.corespringsecurity.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestRepository testRepository;

    @PostMapping("/test")
    public void addTest(){
        testRepository.save(new Test("name"));
    }
}

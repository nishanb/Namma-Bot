package com.example.workflow.controllers;

import com.example.workflow.dto.TemplateRequestDto;
import com.example.workflow.models.Template;
import com.example.workflow.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

// Use this controller to test service / anything :( anything .... :()
//TODO: Remove this before release
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    TemplateRepository templateRepository;

    @PostMapping("/template-seed")
    public void test(@RequestBody TemplateRequestDto templateRequestDto) throws IOException {
        templateRepository.save(new Template(templateRequestDto.getTemplateId(), new HashMap<>() {{
            put("english", templateRequestDto.getMessage().get("english"));
            put("kannada", templateRequestDto.getMessage().get("kannada"));
            put("hindi", templateRequestDto.getMessage().get("hindi"));
        }}));
    }
}

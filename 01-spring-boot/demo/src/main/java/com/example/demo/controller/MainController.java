package com.example.demo.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    private final AtomicInteger requestCounter = new AtomicInteger();

    private final ConfigurableApplicationContext context;

    public MainController(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @GetMapping("/shutdown")
    @ResponseBody
    public String shutdown() {

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            context.close();
        }).start();

        return "Application is shutting down...";
    }

    @GetMapping("/")
    public String getIndexPage(Model model) throws UnknownHostException {

        model.addAttribute("appName", "Spring Boot Kubernetes Demo");
        model.addAttribute("podName", InetAddress.getLocalHost().getHostName());
        model.addAttribute("time", LocalDateTime.now());
        model.addAttribute("requestCount", requestCounter.incrementAndGet());

        return "index";
    }
}
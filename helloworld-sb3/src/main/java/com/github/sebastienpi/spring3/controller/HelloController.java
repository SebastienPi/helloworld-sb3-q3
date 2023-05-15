package com.github.sebastienpi.spring3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.github.sebastienpi.spring3.service.HelloService;

@RestController
public class HelloController {
	private HelloService helloService;

	public HelloController(HelloService helloService) {
		this.helloService=helloService;
	}

	@GetMapping(path = "/hello/{name}")
	public String sayHello(@PathVariable String name) {
		return this.helloService.sayHello(name);
	}
}
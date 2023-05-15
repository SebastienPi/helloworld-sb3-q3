package com.github.sebastienpi.quarkus3.service;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HelloService {
	public String sayHello(String name) {
		return "Hello "+name+"!";
	}
}
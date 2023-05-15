package com.github.sebastienpi.quarkus3.controller;

import org.jboss.resteasy.reactive.RestPath;

import com.github.sebastienpi.quarkus3.service.HelloService;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("hello")
public class HelloResource {
	private HelloService helloService;

	public HelloResource(HelloService helloService) {
		this.helloService=helloService;
	}

    @GET
    @Path("{name}")
    public String hello(@RestPath String name) {
		return this.helloService.sayHello(name);
    }
}
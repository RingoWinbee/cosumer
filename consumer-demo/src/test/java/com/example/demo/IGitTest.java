package com.example.demo;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IGitTest {


	@Autowired
	private RestTemplate restTemplate;

	@Test
	public void test() {
		System.out.println(restTemplate.equals(new String("sss")));
	}

}

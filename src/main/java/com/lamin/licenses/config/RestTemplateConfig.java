package com.lamin.licenses.config;

import java.util.Collections;
import java.util.List;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import com.lamin.licenses.util.UserContextInterceptor;

//@Configuration
//public class RestTemplateConfig {
//
//	@Primary
//	@Bean
//	@LoadBalanced
//	public RestTemplate restTemplate() {
//		
//		RestTemplate template = new RestTemplate();
//		List interceptors = template.getInterceptors();
//		
//		if (interceptors == null) {
//			template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
//		} else {
//			interceptors.add(new UserContextInterceptor());
//			template.setInterceptors(interceptors);
//		}
//		return template;
//	}
//}

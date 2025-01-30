package com.nexmind3.octoaqua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"com.nexmind3.octoaqua.service","com.nexmind3.octoaqua.controller","com.nexmind3.octoaqua.util","com.nexmind3.octoaqua","com.nexmind3.octoaqua.dto"})
@EntityScan({"com.nexmind3.octoaqua.entity","com.nexmind3.octoaqua.*"})
@EnableScheduling
public class OctoAquaBackend {
	public static void main(String[] args) {
		SpringApplication.run(OctoAquaBackend.class, args);
	}
}

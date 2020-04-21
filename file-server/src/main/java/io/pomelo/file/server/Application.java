package io.pomelo.file.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 文件管理服务
 */
@EntityScan(basePackages = { "io.pomelo" })
@EnableJpaRepositories(basePackages = { "io.pomelo" })
@SpringBootApplication(scanBasePackages = { "io.pomelo" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

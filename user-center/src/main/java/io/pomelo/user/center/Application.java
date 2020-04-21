package io.pomelo.user.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 人员服务（用户，角色，菜单）
 */
@EnableFeignClients(basePackages = "io.pomelo")
@EntityScan(basePackages = { "io.pomelo" })
@EnableJpaRepositories(basePackages = { "io.pomelo" })
@SpringBootApplication(scanBasePackages = { "io.pomelo" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
//		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
	}
}

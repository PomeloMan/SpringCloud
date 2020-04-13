package io.pomelo.user.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 人员服务（用户，角色，菜单）
 */
@EnableFeignClients(basePackages = "io.pomelo")
@SpringBootApplication(scanBasePackages = { "io.pomelo" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
//		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
	}
}

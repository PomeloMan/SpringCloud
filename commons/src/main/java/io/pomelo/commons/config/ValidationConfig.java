package io.pomelo.commons.config;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidationConfig {

	@Value("${spring.messages.encoding}")
	private String encoding = "UTF-8";
	@Value("${spring.messages.basename}")
	private String basename = "ValidationMessages";

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setDefaultEncoding(encoding);
		source.setCacheMillis(-1);
		source.setBasename(basename);
		return source;
	}

	@Bean
	public Validator validator() {
		LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
		MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
		factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
		factoryBean.setValidationMessageSource(messageSource());
		return factoryBean;
	}
}

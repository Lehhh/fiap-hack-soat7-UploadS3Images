package br.com.fiap.soat7.infrastructure.config;

import br.com.fiap.soat7.domain.enums.Step;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("br.com.fiap.soat7")
@Getter
@Setter
public class UploadToS3Properties {
	private String redisMidUrl;
	private String step;
}
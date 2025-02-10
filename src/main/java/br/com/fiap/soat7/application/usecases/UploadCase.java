package br.com.fiap.soat7.application.usecases;

import br.com.fiap.soat7.application.services.UploadS3Service;
import br.com.fiap.soat7.domain.enums.Step;
import br.com.fiap.soat7.infrastructure.config.UploadToS3Properties;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class UploadCase implements CommandLineRunner {

	private final UploadToS3Properties props;
	private final UploadS3Service uploadS3Service;

	@Override
	public void run(String... args) throws Exception {
		if (props.getStep().equals(Step.VIDEO.name())) {
			log.info("Iniciando execução da fila de upload videos");
			uploadS3Service.uploadVideo();
		}
		else if (props.getStep().equals(Step.IMAGE)) {
			log.info("Iniciando execução da fila de upload images");
			uploadS3Service.uploadImage();
		}
		else {
			log.error("STEP Invalido: opcoes validas IMAGE,VIDEO");
		}
	}
}

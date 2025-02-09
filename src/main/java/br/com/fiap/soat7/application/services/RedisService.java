package br.com.fiap.soat7.application.services;


import br.com.fiap.soat7.domain.enums.StatusRequest;
import br.com.fiap.soat7.domain.dto.InfoVideo;
import br.com.fiap.soat7.infrastructure.config.UploadToS3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class RedisService {

	private final RestTemplate restTemplate;
	private final UploadToS3Properties props;


	public Boolean sendStatus(InfoVideo infoVideo, StatusRequest statusRequest){
		try{
			ResponseEntity<String> exchange = restTemplate.exchange(props.getRedisMidUrl() + statusRequest.getEndPoint(),
					HttpMethod.POST,
					new HttpEntity<>(infoVideo,null),
					String.class);
			log.info(exchange.getBody());
			return true;
		}
		catch (Exception e){
			log.error(e.getMessage());
			e.getMessage();
			return false;
		}
	}

	public List<String> fetchQueueVideoS3(){
		ResponseEntity<List<String>> exchange = restTemplate.exchange(props.getRedisMidUrl() + StatusRequest.UPLOAD_S3_QUEUE.getEndPoint(),
				HttpMethod.GET,
				new HttpEntity<>(null),
				new ParameterizedTypeReference<List<String>>() {}
		);
		return exchange.getBody();
	}

	public List<String> fetchQueueImageS3(){
		ResponseEntity<List<String>> exchange = restTemplate.exchange(props.getRedisMidUrl() + StatusRequest.UPLOAD_S3_IMAGES_QUEUE.getEndPoint(),
				HttpMethod.GET,
				new HttpEntity<>(null),
				new ParameterizedTypeReference<List<String>>() {}
		);
		return exchange.getBody();
	}

}

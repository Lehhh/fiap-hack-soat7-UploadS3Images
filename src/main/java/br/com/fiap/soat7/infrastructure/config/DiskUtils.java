package br.com.fiap.soat7.infrastructure.config;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class DiskUtils {

	public List<MultipartFile> listFilesAsMultipartFile(Path folderPath) {
		List<MultipartFile> multipartFiles = new ArrayList<>();

		try {
			List<Path> files = Files.list(folderPath)
					.filter(Files::isRegularFile)
					.toList();

			// Convertendo cada arquivo para MultipartFile
			for (Path file : files) {
				FileSystemResource fileResource = new FileSystemResource(file.toFile());
				FileToMultipartFile multipartFile = new FileToMultipartFile(new File(String.valueOf(file)));

				multipartFiles.add(multipartFile);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return multipartFiles;
	}
}

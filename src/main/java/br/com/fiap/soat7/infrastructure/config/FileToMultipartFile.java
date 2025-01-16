package br.com.fiap.soat7.infrastructure.config;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
public class FileToMultipartFile implements MultipartFile {

	private final File file;

	public FileToMultipartFile(File file) {
		this.file = file;
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public String getOriginalFilename() {
		return file.getName();
	}

	@Override
	public String getContentType() {
		return "application/octet-stream"; // Ou outro tipo de conteúdo, se conhecido
	}

	@Override
	public boolean isEmpty() {
		return file.length() == 0;
	}

	@Override
	public long getSize() {
		return file.length();
	}

	@Override
	public byte[] getBytes() throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			return fis.readAllBytes();
		}
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(file);
	}

	@Override
	public void transferTo(File dest) throws IOException {
		try (InputStream input = new FileInputStream(file);
		     OutputStream output = new FileOutputStream(dest)) {
			input.transferTo(output);
		}
	}
}

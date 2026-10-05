package com.app.blog.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.blog.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

	@Override
	public String UploadImage(String path, MultipartFile file) throws IOException {

		// file name
		String fileName = file.getOriginalFilename();

		// Random name generate of file (if same doc uploaded multiple time)

		String randomId = UUID.randomUUID().toString();

		String newfileName = randomId.concat(fileName.substring(fileName.lastIndexOf(".")));

		// full path
		String filepath = path + File.separator + newfileName;
		log.info("filepath==:{}", filepath);

		// Create folder if not created

		File folderPath = new File(path);

		if (!folderPath.exists()) {
			folderPath.mkdir();
		}
		log.info("folder==:{}", folderPath);
		// file copy

		Files.copy(file.getInputStream(), Paths.get(filepath));

		return newfileName;
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {
		String fullpath = path + File.separator + fileName;
		InputStream input = new FileInputStream(fullpath);
		return input;
	}

}

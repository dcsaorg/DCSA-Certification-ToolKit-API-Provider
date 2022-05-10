package org.dcsa.api.validator.webservice.uploader.service;

import lombok.extern.java.Log;
import org.dcsa.api.validator.model.UploadType;
import org.dcsa.api.validator.util.FileUtility;
import org.dcsa.api.validator.util.TestUtility;
import org.dcsa.api.validator.webservice.init.AppProperty;
import org.dcsa.api.validator.webservice.uploader.exception.StorageException;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.logging.Level;

@Log
@Service
public class FileSystemStorageService implements StorageService {

	public void store(MultipartFile file, Path uploadPath, UploadType uploadType) {
		try {
			if (file.isEmpty() || uploadPath == null) {
				throw new StorageException("Failed to store empty file for path.");
			}
			Path destinationFile = AppProperty.uploadPath.resolve(
					Paths.get(Objects.requireNonNull(file.getOriginalFilename())))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(AppProperty.uploadPath.toAbsolutePath())) {
				throw new StorageException(
						"Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
		overrideConfig(file.getOriginalFilename(), uploadType);
	}

	private void overrideConfig(String fileName, UploadType uploadType){
		if(uploadType == UploadType.ConfigData){
			TestUtility.loadConfigData(FileUtility.loadFileAsString(AppProperty.uploadPath.toAbsolutePath() + File.separator+ fileName));
			log.log(Level.INFO, "ConfigData "+AppProperty.uploadPath.toAbsolutePath() + File.separator+ fileName+ " UPLOADED");
		} else if(uploadType == UploadType.TestData){
			TestUtility.loadTestData(FileUtility.loadFileAsString(AppProperty.uploadPath.toAbsolutePath() + File.separator+ fileName));
			log.log(Level.INFO, "TestData "+AppProperty.uploadPath.toAbsolutePath() + File.separator+ fileName+ " UPLOADED");
		}
	}

	public static void deleteAll() {
		FileSystemUtils.deleteRecursively(AppProperty.uploadPath.toFile());
	}

}

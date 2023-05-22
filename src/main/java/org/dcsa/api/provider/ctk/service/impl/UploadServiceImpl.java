package org.dcsa.api.provider.ctk.service.impl;

import lombok.extern.java.Log;
import org.dcsa.api.provider.ctk.init.AppProperty;
import org.dcsa.api.provider.ctk.exception.StorageException;
import org.dcsa.api.provider.ctk.model.enums.UploadType;
import org.dcsa.api.provider.ctk.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Log
@Service
public class UploadServiceImpl implements UploadService {

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
		//overrideConfig(file.getOriginalFilename(), uploadType);
	}

/*	private void overrideConfig(String fileName, UploadType uploadType){
		if(uploadType == UploadType.ConfigData){
			TestUtility.loadConfigData(FileUtility.loadFileAsString(AppProperty.uploadPath.toAbsolutePath() + File.separator+ fileName));
			log.log(Level.INFO, "ConfigData "+AppProperty.uploadPath.toAbsolutePath() + File.separator+ fileName+ " UPLOADED");
		} else if(uploadType == UploadType.TestData){
			TestUtility.loadTestData(FileUtility.loadFileAsString(AppProperty.uploadPath.toAbsolutePath() + File.separator+ fileName));
			log.log(Level.INFO, "TestData "+AppProperty.uploadPath.toAbsolutePath() + File.separator+ fileName+ " UPLOADED");
		} else if(uploadType == UploadType.AppData){
			PropertyLoader.setResourceFilename(fileName);
			AppProperty.isAppDataUploaded = true;
			log.log(Level.INFO, "AppData "+AppProperty.uploadPath.toAbsolutePath() + File.separator+ fileName+ " UPLOADED");
		}
	}*/

	public static void deleteAll() {
		FileSystemUtils.deleteRecursively(AppProperty.uploadPath.toFile());
	}

}

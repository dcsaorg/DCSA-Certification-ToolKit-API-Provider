package org.dcsa.api.validator.webservice.uploader.service;

import org.dcsa.api.validator.model.UploadType;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
	void store(MultipartFile file, Path uploadPath, UploadType uploadType);
}

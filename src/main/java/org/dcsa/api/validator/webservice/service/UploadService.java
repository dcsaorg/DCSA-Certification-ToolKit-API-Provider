package org.dcsa.api.validator.webservice.service;

import org.dcsa.api.validator.model.enums.UploadType;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface UploadService {
	void store(MultipartFile file, Path uploadPath, UploadType uploadType);
}

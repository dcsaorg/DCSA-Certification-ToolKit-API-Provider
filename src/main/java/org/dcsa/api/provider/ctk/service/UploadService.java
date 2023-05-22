package org.dcsa.api.provider.ctk.service;

import org.dcsa.api.provider.ctk.model.enums.UploadType;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface UploadService {
	void store(MultipartFile file, Path uploadPath, UploadType uploadType);
}

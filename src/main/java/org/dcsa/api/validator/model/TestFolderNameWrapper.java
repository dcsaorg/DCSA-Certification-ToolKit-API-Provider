package org.dcsa.api.validator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestFolderNameWrapper {

    private List<TestFolderName> folder  = new ArrayList<>();
}

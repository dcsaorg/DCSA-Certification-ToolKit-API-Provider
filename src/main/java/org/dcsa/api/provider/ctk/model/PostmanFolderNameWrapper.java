package org.dcsa.api.provider.ctk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostmanFolderNameWrapper {

    private List<PostManFolderName> folder  = new ArrayList<>();
}

package org.dcsa.api.validator.models;

import lombok.Data;
import org.dcsa.api.validator.constants.ValidationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ValidationResult {
    List<Map<ValidationType,String>> validations=new ArrayList<>();
}

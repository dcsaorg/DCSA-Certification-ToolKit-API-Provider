package org.dcsa.api.validator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TestCase {
    TestRequest request=new TestRequest();
}

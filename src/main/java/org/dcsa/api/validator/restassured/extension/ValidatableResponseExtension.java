package org.dcsa.api.validator.restassured.extension;

import org.dcsa.api.validator.constants.StatusCode;
import org.dcsa.api.validator.constants.ValidationCode;


public interface ValidatableResponseExtension {
    ValidatableResponseExtension created(StatusCode statusCode);

    ValidatableResponseExtension found(StatusCode statusCode);

    ValidatableResponseExtension deleted(StatusCode statusCode);

    ValidatableResponseExtension modified(StatusCode statusCode);

    ValidatableResponseExtension foundAll(StatusCode statusCode);

    ValidatableResponseExtension header(StatusCode statusCode);

    ValidatableResponseExtension schema(ValidationCode validationCode);

    ValidatableResponseExtension assertThat();

    ValidatableResponseExtension statusCode(int expectedHttpCode);
}

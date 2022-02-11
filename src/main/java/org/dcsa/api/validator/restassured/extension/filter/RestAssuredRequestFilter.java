package org.dcsa.api.validator.restassured.extension.filter;

import io.cucumber.java.Scenario;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.AllArgsConstructor;
import org.testng.Reporter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class RestAssuredRequestFilter implements Filter {
    Scenario scenario;

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        StringBuffer prettyPrint = new StringBuffer("Request method: " + requestSpec.getMethod() + "\n\n");
        prettyPrint.append("Request URI: " + requestSpec.getURI() + "\n\n");

        if (requestSpec.getQueryParams() != null) {
            if (requestSpec.getQueryParams().size() > 0) {
                prettyPrint.append("Query params: ");
                for (Map.Entry<String, String> queries : requestSpec.getQueryParams().entrySet()) {
                    prettyPrint.append(queries.getKey() + "=" + queries.getValue() + ", ");
                }
                prettyPrint.append("\n\n");
            }
        }

        List<Header> headers = requestSpec.getHeaders().asList();
        if (headers != null) {
            prettyPrint.append("Request Headers: ");
            for (Header header : headers) {
                prettyPrint.append(header.getName() + "=" + header.getValue() + ", ");
            }
            prettyPrint.append("\n\n");
        }
        if (requestSpec.getBody() != null)
            prettyPrint.append("Request Body ==>" + requestSpec.getBody() + "\n\n");

        if (response != null) {
            prettyPrint.append("Http Code: " + response.getStatusCode() + "\n\n");
            prettyPrint.append("Response Headers: " + "Content-Type="
                    + response.getHeader("Content-Type") + ", API-Version="
                    + response.getHeader("API-Version")
                    + "\n\n");
            if (response.getBody() != null) {
                String responseString = response.getBody().asString();
                if (responseString != null && responseString != "")
                    prettyPrint.append("Response Body ==>" + responseString + "\n\n");
            }
        }
        scenario.log(prettyPrint.toString());
        Arrays.stream(prettyPrint.toString().split("\n")).forEach(x->
        Reporter.log(x)
        );
        return response;
    }
}
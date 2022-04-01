package org.dcsa.api.validator.webservice.controller;


import org.dcsa.api.validator.webservice.init.AppProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.testng.TestNG;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProviderController {

   private final AppProperty appProperty;

    public ProviderController(AppProperty appProperty) {
        this.appProperty = appProperty;
    }

    @GetMapping(value = "/start" )
  String startTestNg(){
      appProperty.init();
      TestNG testng = new TestNG();
      final String suitePath = System.getProperty("user.dir")+"\\suitexmls\\"+AppProperty.TEST_SUITE_NAME;
      List<String> xmlList = new ArrayList<>();
      xmlList.add(suitePath);
      testng.setTestSuites(xmlList);
      testng.run();
      return  null;
  }

}

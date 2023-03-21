package org.dcsa.api.validator.reporter.report;

import lombok.extern.java.Log;
import org.dcsa.api.validator.model.Requirement;
import org.dcsa.api.validator.model.enums.PostmanCollectionType;
import org.dcsa.api.validator.webservice.init.AppProperty;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

@Log
public class NewmanReportModifier {
    private static final String BODY_LOGO = "<body class=\"theme-dark\">";
    private static final String BODY_LOGO_REPLACE = "<body class=\"theme-dark\"> <div style=\"text-align: center\"><img src=\" https://dcsa.org/wp-content/uploads/2021/05/logo-files.jpg\" alt=\"dcsa-logo-blue-bg\" width=\"80\", height=\"80\"></div> ";

    private static final String NEWMAN_DASHBOARD = "Newman Run Dashboard";
    private static String DCSA_DASHBOARD = "DCSA %s Run Dashboard";

    private static final String COLLAPSE = "collapse-";

    private static final String MARKER = ">";

    private static final String DIV = "<div>";

    private static final String DIV_CLOSE = "</div>";

    private static final String REQUIREMENT_HEADER_START = "<br><h5 class=\"card-title text-uppercase text-white text-center bg-info\">";

    private static final String REQUIREMENT_HEADER_END ="</h5>";

    private static final String REQUIREMENT_TABLE_START = "<div class=\"table-responsive\">\n" +
            "                                            <table class=\"table table-bordered\">\n" +
            "                                            <thead class=\"thead-light text-center\"><tr><th>Header Name</th><th>Header Value</th></tr></thead>\n" +
            "                                                <tbody>";


    private static final String TABLE_ROW_START = "<tr>";
    private static final String TABLE_DATA_NOW_WRAP_START = "<td class=\"text-nowrap\">";
    private static final String TABLE_DATA_WRAP_START = "<td class=\"text-wrap\">";
    private static final String TABLE_DATA_CLOSE = "</td>";
    private static final String TABLE_ROW_CLOSE = "</tr>";
    private static final String REQUIREMENT_TABLE_END = "</tbody>\n" +
            "                                            </table>\n" +
            "                                        </div>";
    private static StringBuilder stringBuilder = new StringBuilder();

    public static void modifyFile(String reportPath, PostmanCollectionType collectionTypeEnum) {
        Path path = Paths.get(reportPath);
        Charset charset = StandardCharsets.UTF_8;
        String htmlContent = "";
        try {
            htmlContent = Files.readString(path, charset);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        DCSA_DASHBOARD = String.format(DCSA_DASHBOARD, collectionTypeEnum.name());
        htmlContent = htmlContent.replaceAll(BODY_LOGO, BODY_LOGO_REPLACE);
        htmlContent = htmlContent.replaceAll(NEWMAN_DASHBOARD, DCSA_DASHBOARD);
        htmlContent = addRequestmentTable(htmlContent);
        try {
            Files.writeString(path, htmlContent, charset);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    public static String addRequestmentTable(String fullReport){
        AtomicReference<String> finalContent = new AtomicReference<>(fullReport);
        AppProperty.requirementList.forEach( item -> {
            if(finalContent.get().contains(item.getRequirementDescription())) {
                int indexOfMarker = getIndexOfAddRequirementTable(finalContent.get(), item.getRequirementDescription());
                if (indexOfMarker > 0) {
                    String requirementTable = String.format(DIV + REQUIREMENT_HEADER_START + " REQUIREMENT DETAILS " + REQUIREMENT_HEADER_END + REQUIREMENT_TABLE_START + // requirementID
                                    TABLE_ROW_START + TABLE_DATA_NOW_WRAP_START + " %s" + TABLE_DATA_CLOSE + // title of requirementDescription
                                    TABLE_DATA_WRAP_START + " %s" + TABLE_DATA_CLOSE + TABLE_ROW_CLOSE + // requirementDescription
                                    TABLE_ROW_START + TABLE_DATA_NOW_WRAP_START + " %s" + TABLE_DATA_CLOSE + // title of requirementDescription
                                    TABLE_DATA_WRAP_START + " %s" + TABLE_DATA_CLOSE + TABLE_ROW_CLOSE + // requirementDescription
                                    TABLE_ROW_START + TABLE_DATA_NOW_WRAP_START + " %s" + TABLE_DATA_CLOSE + // title of requirementDescription
                                    TABLE_DATA_WRAP_START + " %s" + TABLE_DATA_CLOSE + TABLE_ROW_CLOSE + // requirementDescription
                                    REQUIREMENT_TABLE_END + DIV_CLOSE,
                            "Requirement description: ", item.getRequirementDescription(),
                            "Requirement source: ", item.getRequirementSource(),
                            "Test approach:", item.getTestApproach());
                    finalContent.set(addSubStringAt(finalContent.get(), requirementTable, indexOfMarker + 1));
                }
            }
        });
        return finalContent.get();
    }

    private static void processLine(String line){
        if(line.contains(BODY_LOGO)){
           line = line.replaceAll(BODY_LOGO, BODY_LOGO_REPLACE);
           stringBuilder.append(line);
        }else if(line.contains(NEWMAN_DASHBOARD)) {
            line = line.replaceAll(NEWMAN_DASHBOARD, DCSA_DASHBOARD);
            stringBuilder.append(line);
        }else {
            String finalLine = line;
            Optional<Requirement> findFirstItem = AppProperty.requirementList.stream()
                                                    .filter(item -> item.getRequirementDescription().equals(finalLine))
                                                    .findFirst();
            if(findFirstItem.isPresent()){
                Requirement requirement = findFirstItem.get();
            }
        }
    }

/*    public static String getReportString(String reportPath){
        Path path = Paths.get(reportPath);
        Charset charset = StandardCharsets.UTF_8;

        try {
            htmlContent = Files.readString(path, charset);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return htmlContent;
    }*/


    public static int getIndexOfAddRequirementTable(String mainStr, String Requirement){
        int indexOfRequirementId = mainStr.indexOf(Requirement);

        int indexOfCollapse = indexOfSubstringAt(mainStr, COLLAPSE, indexOfRequirementId);

        return indexOfSubstringAt(mainStr, MARKER, indexOfCollapse);
    }

    public static String addSubStringAt(String mainStr, String subStr, int position) {
        return mainStr.substring(0, position) + subStr + mainStr.substring(position);
    }

    public static int indexOfSubstringAt(String inputStr, String subStr, int startIndex) {
        int position = -1;
        do {
            position = inputStr.indexOf(subStr, position + 1);
        } while (startIndex > position);
        return position;
    }


}

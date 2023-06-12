package org.dcsa.api.provider.ctk.service;

import lombok.extern.java.Log;
import org.dcsa.api.provider.ctk.controller.BookingConfirmationController;
import org.dcsa.api.provider.ctk.model.Requirement;
import org.dcsa.api.provider.ctk.model.enums.PostmanCollectionType;
import org.dcsa.api.provider.ctk.util.JsonUtility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

import static org.dcsa.api.provider.ctk.model.enums.PostmanCollectionType.*;

@Log
public class NewmanReportModifier {
    private static final String BODY_LOGO = "<body class=\"theme-dark\">";
    private static final String BODY_LOGO_REPLACE = "<body class=\"theme-dark\"> <div style=\"text-align: center\"><img src=\" https://dcsa.org/wp-content/uploads/2021/05/logo-files.jpg\" alt=\"dcsa-logo-blue-bg\" width=\"80\", height=\"80\"></div> ";

    private static final String NEWMAN_DASHBOARD = "Newman Run Dashboard";
    private static final String COLLAPSE = "collapse-";

    private static final String GREATER_THAN_MARKER = ">";

    private static final String DIV = "<div>";

    private static final String DIV_MANUAL = "<div class=\"tab-pane fade\" id=\"pills-manual\" role=\"tabcard\" aria-labelledby=\"pills-skipped-tab\">";

    private static final String DIV_CLOSE = "</div>";

    private static final String REQUIREMENT_HEADER_START = "<br><h5 class=\"card-title text-uppercase text-white text-center bg-info\">";

    private static final String REQUIREMENT_HEADER_END ="</h5>";

    private static final String REQUIREMENT_TABLE_START = "<div class=\"table-responsive\">\n" +
            "                                            <table class=\"table table-bordered\">\n" +
            "                                            <thead class=\"thead-light text-center\"><tr><th>Validation Header</th><th>Validation Description</th></tr></thead>\n" +
            "                                                <tbody>";


    private static final String TABLE_ROW_START = "<tr>";
    private static final String TABLE_DATA_NOW_WRAP_START = "<td class=\"text-nowrap\">";
    private static final String TABLE_DATA_WRAP_START = "<td class=\"text-wrap\">";
    private static final String TABLE_DATA_CLOSE = "</td>";
    private static final String TABLE_ROW_CLOSE = "</tr>";
    private static final String LI_CLOSE = "</li>";

    private static String BOLD_COUNTER = "<b>%s</b>";

    private static final String REQUIREMENT_TABLE_END = "</tbody>\n" +
            "                                            </table>\n" +
            "                                          </div>";
    private static final String MANUAL_TEST_TABLE_START_MARK = "<div class=\"tab-pane fade\" id=\"pills-skipped\"";

    private static final String MANUAL_TEST_TITLE_START_MARK = "id=\"pills-skipped-tab\"";

    private static String MANUAL_TEST_TITLE = "            <li class=\"nav-item bg-info active\" data-toggle=\"tooltip\" title=\"Click to view the Manual Validations\">\n" +
                                                    "                <a class=\"nav-link text-white\" id=\"pills-manual-tab\" data-toggle=\"pill\" href=\"#pills-manual\" role=\"tab\" aria-controls=\"pills-skipped\" aria-selected=\"false\">Manual Validations <span class=\"badge badge-light\">%s</span></a>\n" +
                                                    "            </li>\n";
    private static final String TNT_REQUIREMENT = File.separator+ "requirement" +File.separator+"TntRequirement.json";
    private static final String TNT_MANUAL_TEST = File.separator+ "requirement" +File.separator+"TntManualTest.json";
    private static final String OVS_REQUIREMENT = File.separator+ "requirement" +File.separator+"OvsRequirement.json";

    private static final String OVS_MANUAL_TEST = File.separator+ "requirement" +File.separator+"OvsManualTest.json";
    private static final String E_DOCUMENTATION_REQUIREMENT = File.separator+ "requirement" +File.separator+"EDocumentationRequirement.json";

    private static final String E_DOCUMENTATION_MANUAL_TEST = File.separator+ "requirement" +File.separator+"EDocumentationManualTest.json";

    private static final String BOOKING_REQUIREMENT = File.separator+ "requirement" +File.separator+"BookingRequirement.json";
    private static final String BOOKING_MANUAL_TEST = File.separator+ "requirement" +File.separator+"BookingManualTest.json";

    private static final String EBL_REQUIREMENT = File.separator+ "requirement" +File.separator+"EblRequirement.json";
    private static final String EBL_MANUAL_TEST = File.separator+ "requirement" +File.separator+"EblManualTest.json";


    public static List<Requirement> requirementList;
    public static List<Requirement> manualTestList;
    public static void modifyFile(String reportPath, PostmanCollectionType collectionTypeEnum, boolean isOfficial) {
        populateRequirementAndManualTest(collectionTypeEnum);
        Path path = Paths.get(reportPath);
        Charset charset = StandardCharsets.UTF_8;
        String htmlContent = "";
        try {
            htmlContent = Files.readString(path, charset);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        String dcsaDeshboard = "DCSA %s.";
        if(collectionTypeEnum == EDOC){
            dcsaDeshboard = String.format(dcsaDeshboard, "E-Documentation".toUpperCase());
        }else{
            dcsaDeshboard = String.format(dcsaDeshboard, collectionTypeEnum.name().toUpperCase());
        }
        if(isOfficial){
            dcsaDeshboard = "OFFICIAL "+dcsaDeshboard;
        }else{
            dcsaDeshboard = "UNOFFICIAL "+dcsaDeshboard;
        }

        if(collectionTypeEnum == BOOKING){
            String requestedDelay = "%s SECONDS DELAY REQUESTED.";
            requestedDelay = String.format(requestedDelay, BookingConfirmationController.REQUESTED_DELAY);
            dcsaDeshboard = dcsaDeshboard +  "<br>" + requestedDelay;
        }

        htmlContent = htmlContent.replaceAll(BODY_LOGO, BODY_LOGO_REPLACE);
        htmlContent = htmlContent.replaceAll(NEWMAN_DASHBOARD, dcsaDeshboard);
        htmlContent = addManualTestTitle(htmlContent);
        htmlContent = addRequirementTable(htmlContent);
        htmlContent = addManualTestTable(htmlContent);
        try {
            Files.writeString(path, htmlContent, charset);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    private static String addManualTestTitle(String mainHtmlContent){
        int indexOfAddManualTestTitle =  getIndexOfAddManualTestTitle(mainHtmlContent, MANUAL_TEST_TITLE_START_MARK);
        MANUAL_TEST_TITLE = String.format(MANUAL_TEST_TITLE, manualTestList.size());
        return addSubStringAt(mainHtmlContent, MANUAL_TEST_TITLE, indexOfAddManualTestTitle);
    }

    private static void populateRequirementAndManualTest(PostmanCollectionType collectionTypeEnum){
        requirementList = new ArrayList<>();
        manualTestList = new ArrayList<>();
        if(collectionTypeEnum == TNT){
            requirementList = JsonUtility.convertRequirementIdJson(TNT_REQUIREMENT);
            manualTestList = JsonUtility.convertRequirementIdJson(TNT_MANUAL_TEST);
        }else if(collectionTypeEnum == OVS){
            requirementList = JsonUtility.convertRequirementIdJson(OVS_REQUIREMENT);
            manualTestList = JsonUtility.convertRequirementIdJson(OVS_MANUAL_TEST);
        }else if(collectionTypeEnum == EDOC){
            requirementList = JsonUtility.convertRequirementIdJson(E_DOCUMENTATION_REQUIREMENT);
            manualTestList = JsonUtility.convertRequirementIdJson(E_DOCUMENTATION_MANUAL_TEST);
        }else if(collectionTypeEnum == BOOKING){
            requirementList = JsonUtility.convertRequirementIdJson(BOOKING_REQUIREMENT);
            manualTestList = JsonUtility.convertRequirementIdJson(BOOKING_MANUAL_TEST);
        }
        else if(collectionTypeEnum == EBL){
            requirementList = JsonUtility.convertRequirementIdJson(EBL_REQUIREMENT);
            manualTestList = JsonUtility.convertRequirementIdJson(EBL_MANUAL_TEST);
        }
    }

    public static String addRequirementTable(String fullReport){
        AtomicReference<String> finalContent = new AtomicReference<>(fullReport);
        requirementList.forEach( item -> {
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

    public static String addManualTestTable(String fullReport){
        AtomicReference<String> finalContent = new AtomicReference<>(fullReport);
        int indexOfTableStart = getIndexOfManualTestTableStart(finalContent.get());
        StringBuilder finalManualTestTable = new StringBuilder();
        final int[] counter = {1};
        if (indexOfTableStart > 0) {
             manualTestList.forEach(item -> {
             String table = String.format(
                                TABLE_ROW_START + TABLE_DATA_NOW_WRAP_START +BOLD_COUNTER+ " %s" + TABLE_DATA_CLOSE + // title of requirementDescription
                                TABLE_DATA_WRAP_START + " %s" + TABLE_DATA_CLOSE + TABLE_ROW_CLOSE + // requirementDescription
                                TABLE_ROW_START + TABLE_DATA_NOW_WRAP_START + " %s" + TABLE_DATA_CLOSE + // title of requirementDescription
                                TABLE_DATA_WRAP_START + " %s" + TABLE_DATA_CLOSE + TABLE_ROW_CLOSE + // requirementDescription
                                TABLE_ROW_START + TABLE_DATA_NOW_WRAP_START + " %s" + TABLE_DATA_CLOSE + // title of requirementDescription
                                TABLE_DATA_WRAP_START + " %s" + TABLE_DATA_CLOSE + TABLE_ROW_CLOSE + // requirementDescription
                                TABLE_ROW_START + TABLE_DATA_NOW_WRAP_START + TABLE_DATA_CLOSE + // empty row
                                TABLE_DATA_WRAP_START + TABLE_DATA_CLOSE + TABLE_ROW_CLOSE, // empty row
                                counter[0],"Validation description: ", item.getRequirementDescription(),
                                "Validation source: ", item.getRequirementSource(),
                                "Test approach:", item.getTestApproach());
                finalManualTestTable.append(table);
                counter[0]++;
            });
            finalManualTestTable.insert(0, DIV_MANUAL + REQUIREMENT_HEADER_START + " MANUAL VALIDATION " + REQUIREMENT_HEADER_END + REQUIREMENT_TABLE_START);
            finalManualTestTable.insert(finalManualTestTable.length(), REQUIREMENT_TABLE_END + DIV_CLOSE);
            fullReport = addSubStringAt(fullReport, finalManualTestTable.toString(), indexOfTableStart);
        }
        return fullReport;
    }
    public static int getIndexOfAddRequirementTable(String mainStr, String Requirement){
        final String iteration1 = "Iteration: 1 - ";
        int indexOfRequirementId = mainStr.indexOf(iteration1+Requirement);

        int indexOfCollapse = indexOfSubstringGraterAt(mainStr, COLLAPSE, indexOfRequirementId);

        return indexOfSubstringGraterAt(mainStr, GREATER_THAN_MARKER, indexOfCollapse);
    }

    public static int getIndexOfAddManualTestTitle(String mainStr, String startPont){
        int indexOfRequirementId = mainStr.indexOf(startPont);
        return indexOfSubstringGraterAt(mainStr, LI_CLOSE, indexOfRequirementId)+LI_CLOSE.length()+1;
    }

    public static int getIndexOfManualTestTableStart(String mainStr){
        int indexOfRequirementId = mainStr.indexOf(MANUAL_TEST_TABLE_START_MARK);
        return indexOfRequirementId-1;
    }

    public static String addSubStringAt(String mainStr, String subStr, int position) {
        return mainStr.substring(0, position) + subStr + mainStr.substring(position);
    }

    public static int indexOfSubstringGraterAt(String inputStr, String subStr, int startIndex) {
        int position = -1;
        do {
            position = inputStr.indexOf(subStr, position + 1);
        } while (startIndex > position);
        return position;
    }

}

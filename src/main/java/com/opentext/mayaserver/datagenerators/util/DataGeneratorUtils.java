package com.opentext.mayaserver.datagenerators.util;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mifmif.common.regex.Generex;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import static com.opentext.mayaserver.utility.Constants.AWS_IDENTITY_LINEITEMID_REGEX;
import static com.opentext.mayaserver.utility.Constants.FILE_PATH_SEPARATOR;

public class DataGeneratorUtils {

    private static final String NUMERIC_CHARS = "012346789";
    private static final String ALPHANUMERIC_CHARS = "abcdefghijklmnopqrstuvwxyz" + NUMERIC_CHARS;

    public static void writeContentToFile(Object obj, String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

        objectMapper.writer(prettyPrinter).writeValue(new File(filePath), obj);
    }

    public static String generateRandomStringUsingRegex(String regex) {
        Generex generex = new Generex(regex);
        return generex.random();
    }

    public static String newUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateAwsLineItemId() {
        return generateRandomStringUsingRegex(AWS_IDENTITY_LINEITEMID_REGEX);
    }

    public static String generateAlphaNumericContent(int length) {
        String content = "";
        for (int i = 0; i < length; i++) {
            content += selectRandomCharFromString(ALPHANUMERIC_CHARS);
        }
        return content;
    }

    private static char selectRandomCharFromString(String chars) {
        Random random = new Random();
        int randomIndex = random.nextInt(chars.length());
        return chars.charAt(randomIndex);
    }

    public static int getRowCopyLimit(int startRange, int endRange) {
        Random random = new Random();
        return random.nextInt(startRange, endRange);
    }


    public static Map<String, String> createAwsBillDirectories(String billRootDirPath, LocalDate startDate, LocalDate endDate) {
        Map<String, String> billDirMap = new HashMap<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        SimpleDateFormat assemblyFormatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        LocalDate start = LocalDate.parse(startDate.format(df), df);
        LocalDate end = LocalDate.parse(endDate.format(df), df);
        for (LocalDate d = start; (d.isEqual(end) || d.isBefore(end)); d = d.plusMonths(1)) {
            String monthDirectory = df.format(d.with(TemporalAdjusters.firstDayOfMonth())) + "-" + df.format(d.with(TemporalAdjusters.firstDayOfNextMonth()));
            String lastDayOfMonth = d.with(TemporalAdjusters.lastDayOfMonth()).toString();
            String[] dateArray = lastDayOfMonth.split("-");
            int yyyy = 0, mm = 0, dd = 0;
            if (dateArray.length == 3) {
                yyyy = Integer.parseInt(dateArray[0]);
                mm = Integer.parseInt(dateArray[1]);
                dd = Integer.parseInt(dateArray[2]);
            }
            String assemblyDirectory = assemblyFormatter.format(new Date(yyyy - 1900, mm - 1, dd));
            String assemblyDirAbsPath = billRootDirPath + FILE_PATH_SEPARATOR + monthDirectory + FILE_PATH_SEPARATOR + assemblyDirectory;
            billDirMap.put(monthDirectory, assemblyDirAbsPath);
            new File(assemblyDirAbsPath).mkdirs();
        }
        return billDirMap;
    }

}

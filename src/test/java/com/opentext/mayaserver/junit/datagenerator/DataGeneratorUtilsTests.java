package com.opentext.mayaserver.junit.datagenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.datagenerators.util.DataGeneratorUtils;
import com.opentext.mayaserver.junit.datagenerator.util.TestClass;
import com.opentext.mayaserver.utility.CommonUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;

import static com.opentext.mayaserver.utility.Constants.FILE_PATH_SEPARATOR;
import static com.opentext.mayaserver.utility.Constants.AWS_COST_DIR;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DataGeneratorUtilsTests {

    @Autowired
    ApplicationPropertiesConfig applicationPropertiesConfig;

    @Test
    public void testWriteContentToFile() throws IOException {
        String filePath = "test_file.json";
        TestClass testClassObject = new TestClass("1233543554657", "abc");
        try {
            DataGeneratorUtils.writeContentToFile(testClassObject, filePath);
            File tempFile = new File(filePath);
            assertTrue(tempFile.exists());
            ObjectMapper objectMapper = new ObjectMapper();
            TestClass readObject = objectMapper.readValue(tempFile, TestClass.class);
            assertEquals(testClassObject.getTestId(), readObject.getTestId());
            assertEquals(testClassObject.getTestName(), readObject.getTestName());
        } finally {
            Files.deleteIfExists(Path.of(filePath));
        }
    }

    @Test
    public void testWriteContentToFileFailTest() {
        String invalidFilePath = "invalid_directory/invalid_file.json";
        TestClass testClassObject = new TestClass("1233543554657", "abc");
        assertThrows(IOException.class, () -> {
            DataGeneratorUtils.writeContentToFile(testClassObject, invalidFilePath);
        });
    }

    @Test
    public void singleMonthDirectoryGenerationTest() {
        String useCaseName = "pilsner-test";
        String billRootDirPath = CommonUtils.createDirectoryBasedOnUseCase(useCaseName + FILE_PATH_SEPARATOR + AWS_COST_DIR, applicationPropertiesConfig.getS3Mock().getNfs());
        LocalDate startDate = LocalDate.of(2023, 06, 10);
        LocalDate endDate = LocalDate.of(2023, 06, 10);
        Map<String, String> billDirMap = DataGeneratorUtils.createAwsBillDirectories(billRootDirPath, startDate, endDate);
        assertEquals(billDirMap.size(), 1);
        for (Map.Entry<String, String> entry : billDirMap.entrySet()) {
            String mothDirPath = entry.getKey();
            assertEquals(mothDirPath, "20230601-20230701");
        }
    }

    @Test
    public void multipleMonthDirectoryGenerationTest() {
        String useCaseName = "pilsner-test";
        String billRootDirPath = CommonUtils.createDirectoryBasedOnUseCase(useCaseName + FILE_PATH_SEPARATOR + AWS_COST_DIR, applicationPropertiesConfig.getS3Mock().getNfs());
        LocalDate startDate = LocalDate.of(2023, 06, 10);
        LocalDate endDate = LocalDate.of(2023, 10, 10);
        Map<String, String> billDirMap = DataGeneratorUtils.createAwsBillDirectories(billRootDirPath, startDate, endDate);
        assertEquals(billDirMap.size(), 5);
    }
}

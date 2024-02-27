package com.opentext.mayaserver.junit;

import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.utility.CommonUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CommonUtilsTests {

    @Autowired
    ApplicationPropertiesConfig applicationPropertiesConfig;

    @Test
    public void createDirectoryBasedOnUseCaseTest() throws IOException {
        String directory = CommonUtils.createDirectoryBasedOnUseCase("test-useCase", applicationPropertiesConfig.getMockoon().getNfsDataPath());
        assertNotNull(directory);
        assertTrue(new File(directory).exists());
        File file = new File(directory);
        FileUtils.deleteDirectory(file);
    }

    @Test
    public void testCreateDirectoryBasedOnUseCase_DirectoryAlreadyExists() throws IOException {
        String useCaseName = "test-useCase";
        String path = applicationPropertiesConfig.getMockoon().getNfsDataPath();
        String directory = CommonUtils.createDirectoryBasedOnUseCase(useCaseName, path);
        assertNotNull(directory);
        assertTrue(new File(directory).exists());
        String newlyCreatedDirectory = CommonUtils.createDirectoryBasedOnUseCase(useCaseName, path);
        assertEquals(directory, newlyCreatedDirectory);
        File file = new File(directory);
        FileUtils.deleteDirectory(file);
    }
}

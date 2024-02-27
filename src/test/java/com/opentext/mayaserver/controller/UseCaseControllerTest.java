package com.opentext.mayaserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.mayaserver.TestUtility;
import com.opentext.mayaserver.models.vo.UseCaseVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class UseCaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getUseCasesTest() throws Exception {
        this.mockMvc.perform(get("/v1/usecase"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUseCasesByParamTest() throws Exception {
        this.mockMvc.perform(get("/v1/usecase").param("usecaseName", "nova-usecse-1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUseCasesTest() throws Exception {
        this.mockMvc.perform(delete("/v1/usecase").param("usecaseName", "nova-usecse-1"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void createUseCaseWithInvalidBillingRecordsPerDayTest() throws Exception {
        objectMapper.findAndRegisterModules();
        TestUtility testUtility = new TestUtility();
        UseCaseVO useCaseVO = testUtility.getUseCaseVO();
        useCaseVO.getBilling().setRecordsPerDay(-100);

        this.mockMvc.perform(post("/v1/usecase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(useCaseVO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUseCaseWithInvalidCloudProviderTest() throws Exception {
        objectMapper.findAndRegisterModules();
        TestUtility testUtility = new TestUtility();
        UseCaseVO useCaseVO = testUtility.getUseCaseVO();
        useCaseVO.setCloudProvider("abc");

        this.mockMvc.perform(post("/v1/usecase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(useCaseVO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUseCaseWithInvalidNumberOfAccountsTest() throws Exception {
        objectMapper.findAndRegisterModules();
        TestUtility testUtility = new TestUtility();
        UseCaseVO useCaseVO = testUtility.getUseCaseVO();
        useCaseVO.getAccount().setNumberOfAccounts(-20);

        this.mockMvc.perform(post("/v1/usecase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(useCaseVO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUseCaseWithInvalidPageSizeTest() throws Exception {
        objectMapper.findAndRegisterModules();
        TestUtility testUtility = new TestUtility();
        UseCaseVO useCaseVO = testUtility.getUseCaseVO();
        useCaseVO.getAccount().setPageSize(-20);

        this.mockMvc.perform(post("/v1/usecase")
                        .content(objectMapper.writeValueAsString(useCaseVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

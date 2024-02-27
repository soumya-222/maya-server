package com.opentext.mayaserver.controllers;


import com.opentext.mayaserver.models.vo.UseCaseVO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.io.IOException;


/**
 * @author Rajiv
 */

@RequestMapping("/v1")
public interface UseCaseAPI {

    @PostMapping(value = "/usecase",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<Object> createUseCase(@RequestBody @Valid UseCaseVO useCaseVO);

    @GetMapping(value = "/usecase")
    ResponseEntity<Object> getUseCase(@RequestParam(value = "usecaseName", required = false) String useCaseName);
    @GetMapping(value = "/usecase/{usecaseName}/costdata")
    ResponseEntity<Object> getUseCaseCostData(@PathVariable(name = "usecaseName") String useCaseName,
                                              @RequestParam(name = "rootAccount", required = false) String rootAccount,
                                              @RequestParam(name = "memberAccounts", required = false) List<String> memberAccounts,
                                              @RequestParam(name = "billPeriod", required = false) String billPeriod);
    @GetMapping(value = "/usecase/{usecaseName}/accounts")
    ResponseEntity<Object> getAllAccounts(@PathVariable(name = "usecaseName") String useCaseName);

    @DeleteMapping(value = "/usecase")
    ResponseEntity<Object> deleteUseCase(@RequestParam(value = "usecaseName") String useCaseName) throws IOException;

}

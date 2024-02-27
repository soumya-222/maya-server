
package com.opentext.mayaserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home redirection to swagger api documentation
 */
@Slf4j
@Controller
public class HomeController {
    @RequestMapping(value = "/swagger")
    public String index() {
        log.info("swagger-ui.html");
        return "redirect:swagger-ui.html";
    }
}

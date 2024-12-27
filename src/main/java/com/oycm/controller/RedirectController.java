package com.oycm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author ouyangcm
 * create 2024/12/27 16:18
 */
@Controller
public class RedirectController {

    @GetMapping("/redirect1")
    public RedirectView redirect1() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://127.0.0.1:8090/redirect2");
        return redirectView;
    }

    @GetMapping("/redirect2")
    public RedirectView redirect2() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://127.0.0.1:8090/redirect.do");
        return redirectView;
    }
}

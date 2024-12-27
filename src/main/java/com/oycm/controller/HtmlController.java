package com.oycm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author ouyangcm
 * create 2024/12/27 15:58
 */
@RestController
public class HtmlController {


    @GetMapping("/redirect.do")
    public String redirect() {
        StringBuilder response = new StringBuilder();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("static/response.html");


        String line = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return response.toString();
    }
}

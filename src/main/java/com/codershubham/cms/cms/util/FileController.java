package com.codershubham.cms.cms.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;  // Correct Resource import
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file")
public class FileController {

    @GetMapping("/downloadTemplate")
    public ResponseEntity<Resource> downloadTemplate() throws IOException {
        // Specify the path to your file in the resources folder
        ClassPathResource resource = new ClassPathResource("static/files/questions_template.xlsx");

        InputStream inputStream = resource.getInputStream();

        // Set the response headers for file download
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}

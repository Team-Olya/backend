package com.teamolha.talantino.general.aws;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/s3")
@AllArgsConstructor
public class S3Controller {

    private S3Service s3Service;

    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String upload(Authentication auth, @RequestParam("file") MultipartFile file) {
        return s3Service.saveFile(auth, file);
    }

    @DeleteMapping("/delete")
    public void delete(Authentication auth) {
        s3Service.deleteFile(auth);
    }
}

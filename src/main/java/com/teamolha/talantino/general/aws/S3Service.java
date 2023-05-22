package com.teamolha.talantino.general.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.teamolha.talantino.account.model.AccountRole;
import com.teamolha.talantino.account.model.entity.Account;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.repository.TalentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static org.apache.http.entity.ContentType.*;

@Service
@Transactional
@RequiredArgsConstructor
public class S3Service {

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3;

    private final TalentRepository talentRepository;

    private final SponsorRepository sponsorRepository;

    public String saveFile(Authentication auth, MultipartFile file) {
        if (!isValid(file)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }

        var user = getUser(auth);
        try {
            File file1 = convertMultiPartToFile(file);
            String fileName = generateFileName(user, file);
            deleteFile(auth);
            PutObjectResult objectResult = s3.putObject(bucketName, fileName, file1);
            if (user instanceof Talent) {
                ((Talent) user).setAvatar(generateUrl(fileName));
                talentRepository.save((Talent) user);
            } else {
                ((Sponsor) user).setAvatar(generateUrl(fileName));
                sponsorRepository.save((Sponsor) user);
            }
            return objectResult.getContentMd5();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValid(MultipartFile file) {
        return !file.isEmpty() && Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_WEBP.getMimeType(),
                IMAGE_GIF.getMimeType()
        ).contains(file.getContentType());
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(Account user, MultipartFile file) {
        return String.format("avatars/%d/%s", user.getId(), file.getOriginalFilename());
    }

    private String generateUrl(String filename) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, filename);
    }

    public void deleteFile(Authentication auth) {
        var user = getUser(auth);
        s3.deleteObject(bucketName, getFileName(user.getAvatar()));
        if (user instanceof Talent) {
            ((Talent) user).setAvatar(null);
            talentRepository.save((Talent) user);
        } else {
            ((Sponsor) user).setAvatar(null);
            sponsorRepository.save((Sponsor) user);
        }
    }

    private String getFileName(String avatarUrl) {
        String[] parts = avatarUrl.split(String.format("https://%s.s3.amazonaws.com/", bucketName));
        return parts[1];
    }

    private Account getUser(Authentication auth) {
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList().contains(AccountRole.TALENT.name())
                ? talentRepository.findByEmailIgnoreCase(auth.getName()).get()
                : sponsorRepository.findByEmailIgnoreCase(auth.getName()).get();
    }
}

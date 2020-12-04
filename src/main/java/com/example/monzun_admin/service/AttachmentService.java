package com.example.monzun_admin.service;

import com.example.monzun_admin.exception.FileIsEmptyException;
import com.example.monzun_admin.exception.UserByEmailNotFound;
import com.example.monzun_admin.model.Attachment;
import com.example.monzun_admin.model.User;
import com.example.monzun_admin.repository.AttachmentRepository;
import com.example.monzun_admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
public class AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    UserRepository userRepository;

    private final String UPLOAD_PATH = System.getProperty("user.dir") + "/attachments/";

    public Attachment storeFile(MultipartFile file) throws IOException {
        Attachment attachment;
        if (file.isEmpty()) {
            throw new FileIsEmptyException("File is empty " + file.getName());
        }

        try {
            attachment = this.saveAttachment(file);
            saveFile(file);
        } catch (IOException e) {
            throw e;
        }

        return attachment;
    }

    public Resource download(UUID Uuid) throws FileNotFoundException {
        try {
            Attachment attachment = attachmentRepository.findByuuid(Uuid);
            Path filePath = Paths.get(attachment.getPath());
            Resource resource = new UrlResource(filePath.toUri());

            if(!resource.exists()) {
                throw new FileNotFoundException("file not found " + Uuid);
            }

            return resource;
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + Uuid);
        }
    }

    public boolean delete(UUID uuid) throws IOException {
        Attachment attachment = attachmentRepository.findByuuid(uuid);

        if (attachment == null) {
            return false;
        }

        try {
            Files.delete(Paths.get(attachment.getPath()));
            attachmentRepository.delete(attachment);
        } catch (IOException e) {
            throw e;
        }

        return true;
    }

    private void saveFile(MultipartFile file) throws IOException {
        Path path = Paths.get(UPLOAD_PATH + file.getOriginalFilename());
        Files.write(path, file.getBytes());
    }

    private Attachment saveAttachment(MultipartFile file) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email);

        if (owner == null) {
            throw new UserByEmailNotFound("User with email " + email + " not found");
        }

        Attachment attachment = new Attachment();
        attachment.setFilename(file.getName());
        attachment.setOriginalFilename(file.getOriginalFilename());
        attachment.setPath(UPLOAD_PATH + file.getOriginalFilename());
        attachment.setOwner(owner);
        attachment.setFilename(file.getName());
        attachment.setCreatedAt(new Date());
        attachmentRepository.save(attachment);

        return attachment;
    }
}

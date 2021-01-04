package com.example.monzun_admin.service;

import com.example.monzun_admin.dto.AttachmentDTO;
import com.example.monzun_admin.dto.AttachmentShortDTO;
import com.example.monzun_admin.entities.Attachment;
import com.example.monzun_admin.entities.User;
import com.example.monzun_admin.exception.FileIsEmptyException;
import com.example.monzun_admin.exception.UserByEmailNotFoundException;
import com.example.monzun_admin.repository.AttachmentRepository;
import com.example.monzun_admin.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public AttachmentService(
            AttachmentRepository attachmentRepository,
            UserRepository userRepository,
            ModelMapper modelMapper
    ) {
        this.attachmentRepository = attachmentRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    private final String UPLOAD_PATH = System.getProperty("user.dir") + "/attachments/";

    /**
     * Список DTO
     *
     * @param attachments список моделей для конвертирования в DTO
     * @return List<AttachmentDTO>
     */
    public List<AttachmentDTO> getAttachmentsDTO(List<Attachment> attachments) {
        return attachments.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Множественное сохранение файлов
     *
     * @param files files
     * @return List<Attachment>
     * @throws IOException IOException
     */
    public List<Attachment> storeFiles(MultipartFile[] files) throws IOException {
        List<Attachment> attachments = new ArrayList<>();
        for (MultipartFile file : files) {
            attachments.add(storeFile(file));
        }

        return attachments;
    }


    /**
     * Хранение файла и создание записи в БД
     *
     * @param file file
     * @return Attachment файл
     * @throws IOException IOException
     */
    public Attachment storeFile(MultipartFile file) throws IOException {
        Attachment attachment;
        if (file.isEmpty()) {
            throw new FileIsEmptyException("File is empty " + file.getName());
        }

        Path savedFile = saveFile(file);
        attachment = this.saveAttachment(savedFile, file.getOriginalFilename());

        return attachment;
    }

    /**
     * Файл для скачивания по Attachment UUID
     *
     * @param Uuid UUID файла
     * @return Resource
     * @throws FileNotFoundException FileNotFoundException
     */
    public Resource download(UUID Uuid) throws FileNotFoundException {
        try {
            Attachment attachment = attachmentRepository.findByuuid(Uuid);
            Path filePath = Paths.get(attachment.getPath());

            if (!filePath.toFile().exists()) {
                throw new FileNotFoundException("file not found " + Uuid);
            }

            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + Uuid);
        }
    }

    /**
     * Удаление файла (физическое и удаление записи из БД)
     *
     * @param uuid UUID Attachment
     * @return boolean
     * @throws IOException IOException
     */
    public boolean delete(UUID uuid) throws IOException {
        Attachment attachment = attachmentRepository.findByuuid(uuid);

        if (attachment == null) {
            return false;
        }

        Files.delete(Paths.get(attachment.getPath()));
        attachmentRepository.delete(attachment);

        return true;
    }


    /**
     * Преобразование модели в Short DTO
     *
     * @param attachment прикрепляемый файл
     * @return AttachmentShortDTO
     */
    public AttachmentShortDTO convertToShortDto(Attachment attachment) {
        return modelMapper.map(attachment, AttachmentShortDTO.class);
    }


    /**
     * Преобразование модели в DTO
     *
     * @param attachment прикрепляемый файл
     * @return AttachmentDTO
     */
    public AttachmentDTO convertToDto(Attachment attachment) {
        return modelMapper.map(attachment, AttachmentDTO.class);
    }


    /**
     * Физическое сохранение файла
     *
     * @param file файл
     * @return Path Сохраненный файл
     * @throws IOException IOException
     */
    private Path saveFile(MultipartFile file) throws IOException {
        File dir = new File(UPLOAD_PATH);

        if (!dir.exists()) {
            if (!dir.mkdirs()){
                throw new IOException("Cannot create attachment directory");
            }
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        Path filePath = Paths.get(UPLOAD_PATH + RandomString.make(10) + "." + extension);
        Files.copy(file.getInputStream(), filePath);

        return filePath;
    }


    /**
     * Запись Attachment в БД
     *
     * @param file файл
     * @return Attachment
     */
    private Attachment saveAttachment(Path file, @Nullable String fileName) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email);

        if (owner == null) {
            throw new UserByEmailNotFoundException("User with email " + email + " not found");
        }

        String baseURL = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        Attachment attachment = new Attachment();

        attachment.setUrl(baseURL + "/api/attachment/download/" + attachment.getUuid());
        attachment.setOriginalFilename(fileName != null ? fileName : file.toFile().getName());
        attachment.setFilename(file.toFile().getName());
        attachment.setPath(file.toFile().getAbsolutePath());
        attachment.setOwner(owner);
        attachment.setCreatedAt(LocalDateTime.now());
        attachmentRepository.save(attachment);

        return attachment;
    }
}




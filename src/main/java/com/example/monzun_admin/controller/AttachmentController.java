package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.AttachmentDTO;
import com.example.monzun_admin.repository.AttachmentRepository;
import com.example.monzun_admin.service.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/attachment")
public class AttachmentController extends BaseRestController {
    private final AttachmentService attachmentService;
    private final AttachmentRepository attachmentRepository;

    public AttachmentController(AttachmentService attachmentService, AttachmentRepository attachmentRepository) {
        this.attachmentService = attachmentService;
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * Загрузка файла.
     *
     * @param file MultipartFile file
     * @return JSON-структура загруженного файла
     * @throws IOException IOException
     */
    @PostMapping("/upload")
    public AttachmentDTO upload(@RequestParam("file") MultipartFile file) throws IOException {
        return new AttachmentDTO(attachmentService.storeFile(file));
    }

    /**
     * Скачивание файла.
     *
     * @param forceDownload флаг принудительной загрузки файла. Если указано TRUE - файл скачивается без предварительного
     *                      просмотра
     * @param uuid          UUID модели файла
     * @param request       Request
     * @return ResponseEntity - JSON ответ
     * @throws IOException IOException
     */
    @GetMapping("/download/{uuid:.+}")
    public ResponseEntity<?> download(
            @RequestParam Optional<Boolean> forceDownload,
            @PathVariable String uuid,
            HttpServletRequest request
    ) throws IOException {
        UUID uuidFromString = UUID.fromString(uuid);
        if (!attachmentRepository.existsAllByUuid(uuidFromString)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());
        }

        Resource resource = attachmentService.download(uuidFromString);
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        String contentDisposition = forceDownload.isPresent() ? "attachment" : "inline";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition
                        + "; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * Удаление файла
     *
     * @param uuid UUID модели файла
     * @return ResponseEntity - JSON ответ
     * @throws IOException IOException
     */
    @DeleteMapping("/delete/{uuid:.+}")
    public ResponseEntity<?> delete(@PathVariable String uuid) throws IOException {
        return attachmentService.delete(UUID.fromString(uuid))
                ? ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());
    }
}

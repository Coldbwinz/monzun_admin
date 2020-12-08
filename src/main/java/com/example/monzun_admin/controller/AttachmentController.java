package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.AttachmentDTO;
import com.example.monzun_admin.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private AttachmentService attachmentService;

    @PostMapping("/upload")
    public AttachmentDTO upload(@RequestParam("file") MultipartFile file) throws IOException {
        return new AttachmentDTO(attachmentService.storeFile(file));
    }

    @GetMapping("/download/{uuid:.+}")
    public ResponseEntity<Resource> download(
            @RequestParam Optional<Boolean> forceDownload,
            @PathVariable String uuid,
            HttpServletRequest request
    ) throws IOException {
        Resource resource = attachmentService.download(UUID.fromString(uuid));
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        String contentDisposition = forceDownload.isPresent() ? "attachment" : "inline";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition
                        + "; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{uuid:.+}")
    public ResponseEntity<?> delete(@PathVariable String uuid) throws IOException {
        return attachmentService.delete(UUID.fromString(uuid))
                ? ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());
    }
}

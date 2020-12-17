package com.example.monzun_admin.service;

import com.example.monzun_admin.dto.AttachmentShortDTO;
import com.example.monzun_admin.dto.StartupDTO;
import com.example.monzun_admin.entities.Startup;
import com.example.monzun_admin.repository.StartupRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StartupService {

    private final StartupRepository startupRepository;
    private final AttachmentService attachmentService;

    public StartupService(StartupRepository startupRepository, AttachmentService attachmentService) {
        this.startupRepository = startupRepository;
        this.attachmentService = attachmentService;
    }

    /**
     * Просмотр конкретного стартапа.
     *
     * @param id Startup ID
     * @return StartupDTO
     * @throws EntityNotFoundException не найден стартап
     */
    public StartupDTO getStartup(Long id) throws EntityNotFoundException {
        Optional<Startup> possibleStartup = startupRepository.findById(id);

        if (!possibleStartup.isPresent()) {
            throw new EntityNotFoundException();
        }

        Startup startup = possibleStartup.get();
        startup.setAttachmentsDTO(getStartupAttachmentDTOs(startup));

        return new StartupDTO(startup);
    }


    /**
     * Получение списка прикрепленных файлов стартапа
     *
     * @param startup стартап
     * @return List
     */
    public List<AttachmentShortDTO> getStartupAttachmentDTOs(Startup startup) {
        return startupRepository
                .getStartupAttachments(startup)
                .stream()
                .map(attachmentService::convertToShortDto)
                .collect(Collectors.toList());
    }
}

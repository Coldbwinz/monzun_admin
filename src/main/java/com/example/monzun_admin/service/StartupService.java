package com.example.monzun_admin.service;

import com.example.monzun_admin.dto.AttachmentShortDTO;
import com.example.monzun_admin.dto.StartupDTO;
import com.example.monzun_admin.dto.StartupListDTO;
import com.example.monzun_admin.entities.Startup;
import com.example.monzun_admin.repository.StartupRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StartupService {

    private final StartupRepository startupRepository;
    private final AttachmentService attachmentService;
    private final ModelMapper modelMapper;

    public StartupService(
            StartupRepository startupRepository,
            AttachmentService attachmentService,
            ModelMapper modelMapper
    ) {
        this.startupRepository = startupRepository;
        this.attachmentService = attachmentService;
        this.modelMapper = modelMapper;
    }

    /**
     * Список стартапов
     *
     * @return List DTO
     */
    public List<StartupListDTO> getStartups() {
        return startupRepository.findAll().stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    /**
     * Просмотр конкретного стартапа.
     *
     * @param id Startup ID
     * @return StartupDTO
     * @throws EntityNotFoundException не найден стартап
     */
    public StartupDTO getStartup(Long id) throws EntityNotFoundException {
        Startup startup = startupRepository.findById(id).orElseThrow(EntityNotFoundException::new);
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

    /**
     * Удаление стартапа
     *
     * @param id Long id
     * @throws EntityNotFoundException EntityNotFoundException
     */
    public void delete(Long id) throws EntityNotFoundException {
        Startup startup = startupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Startup not found id " + id));

        startupRepository.delete(startup);
    }


    /**
     * Преобразование в список DTO
     *
     * @param startup стартап
     * @return List DTO
     */
    private StartupListDTO convertToListDto(Startup startup) {
        return modelMapper.map(startup, StartupListDTO.class);
    }
}

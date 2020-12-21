package com.example.monzun_admin.service;

import com.example.monzun_admin.dto.AttachmentShortDTO;
import com.example.monzun_admin.dto.WeekReportDTO;
import com.example.monzun_admin.entities.WeekReport;
import com.example.monzun_admin.repository.WeekReportRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class WeekReportService {

    private final WeekReportRepository weekReportRepository;
    private final ModelMapper modelMapper;
    private final AttachmentService attachmentService;

    public WeekReportService(
            WeekReportRepository weekReportRepository,
            ModelMapper modelMapper,
            AttachmentService attachmentService
    ) {
        this.weekReportRepository = weekReportRepository;
        this.modelMapper = modelMapper;
        this.attachmentService = attachmentService;
    }

    /**
     * Детальный просмотр отчета трекера.
     *
     * @param weekReportId ID отчета
     * @return WeekReportDTO
     * @throws EntityNotFoundException EntityNotFoundException
     */
    public WeekReportDTO show(Long weekReportId) throws EntityNotFoundException {
        WeekReport weekReport = weekReportRepository.findById(weekReportId)
                .orElseThrow(() -> new EntityNotFoundException("Week report not found id " + weekReportId));

        weekReport.setAttachmentsDTO(getWeekReportsAttachmentDTOs(weekReport));

        return convertToDTO(weekReport);
    }


    /**
     * Получение прикрепленных файлов еженедельных отчетов
     *
     * @param weekReport отчет
     * @return List<AttachmentShortDTO>
     */
    public List<AttachmentShortDTO> getWeekReportsAttachmentDTOs(WeekReport weekReport) {
        return weekReportRepository
                .getWeekReportAttachments(weekReport)
                .stream()
                .map(attachmentService::convertToShortDto)
                .collect(Collectors.toList());
    }


    /**
     * Преобразование отчета в DTO
     *
     * @param weekReport очтет
     * @return WeekReportDTO
     */
    public WeekReportDTO convertToDTO(WeekReport weekReport) {
        return modelMapper.map(weekReport, WeekReportDTO.class);
    }
}

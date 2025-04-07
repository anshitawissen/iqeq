package com.iqeq.service;

import com.iqeq.dto.PriorityDto;
import com.iqeq.exception.CustomException;
import com.iqeq.model.Priority;
import com.iqeq.repository.PriorityRepository;
import com.iqeq.util.CommonServiceUtility;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.iqeq.dto.PriorityDto.build;

@Service
@Slf4j
public class PriorityService {

    @Autowired
    private PriorityRepository priorityRepository;

    @Autowired
    private ModelMapper modelMapper;
    private static final String VALUE = "value";

    public List<PriorityDto> getAllPriority() {
        List<Priority> priorities = priorityRepository.findByIsArchiveFalse(Sort.by(Sort.Order.asc(VALUE)));
        return priorities.stream().map(PriorityDto::build).collect(Collectors.toList());
    }

    public PriorityDto createPriority(PriorityDto priorityDto) throws CustomException {
        Optional<Priority> optionalPriority = priorityRepository.findByLabelAndIsArchiveFalse(priorityDto.getLabel());
        if(optionalPriority.isPresent()){
            throw new CustomException(400, "Priority  "+ priorityDto.getLabel() +" already exist.");
        }
        Priority priority = modelMapper.map(priorityDto,Priority.class);
        if (Objects.isNull(priority.getCreatedBy())) {
            priority.setCreatedBy(CommonServiceUtility.getLoggedInUserEmailId());
        }
        priority.setCreatedDate(LocalDateTime.now());
        priority.setRevisionNo(0);
        return build(priorityRepository.save(priority));
    }

    public Priority getPriorityByLabel(String label) throws CustomException {
        return priorityRepository.findByLabelAndIsArchiveFalse(label)
                .orElseThrow(() -> new CustomException(400,"Priority not found for label: " + label));
    }
}

package ru.practicum.compilation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.compilation.model.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.entity.Compilation;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.util.pageable.OffsetPageRequest;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        log.info("Creating compilation with body={}", newCompilationDto.toString());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);

        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findEventByIdIn(newCompilationDto.getEvents()));
        }

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationDto> getCompilationsByPinned(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = OffsetPageRequest.of(from, size);
        List<Compilation> compilations;

        if (pinned != null) {
            compilations = compilationRepository.findByPinned(pinned, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable).toList();
        }

        log.info("Getting compilations by params: pinned={}, from={}, size={}", pinned, from, size);
        return CompilationMapper.toCompilationDto(compilations);
    }

    @Override
    public CompilationDto getCompilationById(Long id) {
        Compilation compilation = getCompilationModelById(id);

        log.info("Getting compilation by id={}", id);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public Compilation getCompilationModelById(Long id) {
        log.info("Getting compilation model by id={}", id);

        return compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Compilation", id));
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long id, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = getCompilationModelById(id);

        log.info("Updating compilation with body={}", updateCompilationRequest.toString());

        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(eventRepository.findEventByIdIn(updateCompilationRequest.getEvents()));
        }

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        if (updateCompilationRequest.getTitle() != null && !updateCompilationRequest.getTitle().isBlank()) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long id) {
        compilationExists(id);

        log.info("Deleting compilation by id={}", id);
        compilationRepository.deleteById(id);
    }

    @Override
    public void compilationExists(Long id) {
        log.info("Checking that compilation with id={} exists", id);

        if (!compilationRepository.existsById(id)) {
            throw new NotFoundException("Compilation", id);
        }
    }
}

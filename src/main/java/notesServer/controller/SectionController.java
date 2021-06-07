package notesServer.controller;

import notesServer.dto.request.section.SectionActionRequestDto;
import notesServer.dto.response.section.SectionInfoResponseDto;
import notesServer.dto.response.user.EmptyResponseDto;
import notesServer.erros.ServerException;
import notesServer.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;


@RestController
@RequestMapping("/api/sections")
@Validated
public class SectionController {

    private SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public SectionInfoResponseDto createSection(
            @CookieValue("JAVASESSIONID") String cookie,
            @RequestBody @Valid SectionActionRequestDto createDto
    ) throws ServerException {
        return sectionService.createSection(createDto, cookie);
    }

    @PutMapping(path = "{sectionId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public SectionInfoResponseDto editSection(
            @CookieValue("JAVASESSIONID") String cookie,
            @PathVariable("sectionId") @Min(1) long sectionId,
            @RequestBody @Valid SectionActionRequestDto renameDto
    ) throws ServerException {
        return sectionService.editSection(renameDto, sectionId, cookie);
    }

    @DeleteMapping(path = "{sectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponseDto deleteSection(
            @CookieValue("JAVASESSIONID") String cookie,
            @PathVariable("sectionId") @Min(1) long sectionId
    ) throws ServerException {
        return sectionService.deleteSection(sectionId, cookie);
    }

    @GetMapping(path = "{sectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SectionInfoResponseDto getSectionInfo(
            @CookieValue("JAVASESSIONID") String cookie,
            @PathVariable("sectionId") @Min(1) long sectionId
    ) throws ServerException {
        return sectionService.getSectionInfo(sectionId, cookie);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SectionInfoResponseDto> getAllSections(
            @CookieValue("JAVASESSIONID") String cookie
    ) throws ServerException {
        return sectionService.getAllSections(cookie);
    }
}

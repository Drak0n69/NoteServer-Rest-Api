package notesServer.controller;

import com.google.gson.Gson;
import notesServer.dto.request.section.SectionActionRequestDto;
import notesServer.service.SectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SectionController.class)
public class TestSectionController {

    @MockBean
    private SectionService sectionService;

    @Autowired
    MockMvc mvc;

    private final Gson GSON = new Gson();

    @ParameterizedTest
    @ValueSource(strings = {"Books", "Книги", "Old cars", "Диалоги о животных"})
    void createSection(String name) throws Exception {
        SectionActionRequestDto createDto = new SectionActionRequestDto(name);
        String json = GSON.toJson(createDto);
        mvc.perform(post("/api/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().isOk());
        verify(sectionService, times(1)).createSection(eq(createDto), anyString());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"iiiiiiiiiiiiiigggggggggggggggggggoooooooooooooorrrrrrrrrrь", "!@#F_><UHEI▌#!!@#"})
    void failCreateSectionWrongName(String name) throws Exception {
        SectionActionRequestDto createDto = new SectionActionRequestDto(name);
        String json = GSON.toJson(createDto);
        mvc.perform(post("/api/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void renameSection() throws Exception {
        SectionActionRequestDto renameDto = new SectionActionRequestDto("books");
        String json = GSON.toJson(renameDto);
        mvc.perform(put("/api/sections/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().isOk());
        verify(sectionService, times(1)).editSection(renameDto, 1L, "val");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void failRenameSection(long id) throws Exception {
        SectionActionRequestDto renameDto = new SectionActionRequestDto("books");
        String json = GSON.toJson(renameDto);
        mvc.perform(put("/api/sections/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"iiiiiiiiiiiiiigggggggggggggggggggoooooooooooooorrrrrrrrrrь", "!@#F_><UHEI▌#!!@#"})
    void failRenameSection(String name) throws Exception {
        SectionActionRequestDto renameDto = new SectionActionRequestDto(name);
        String json = GSON.toJson(renameDto);
        mvc.perform(put("/api/sections/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void deleteSection() throws Exception {
        mvc.perform(delete("/api/sections/1")
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().isOk());
        verify(sectionService, times(1)).deleteSection(1L, "val");
    }

    @Test
    void getSectionInfo() throws Exception {
        mvc.perform(get("/api/sections/1")
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().isOk());
        verify(sectionService, times(1)).getSectionInfo(1L, "val");
    }

    @Test
    void failGetSectionInfo() throws Exception {
        mvc.perform(get("/api/sections/1")
                .cookie(new Cookie("wrong_cookie", "val")))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void getAllSections() throws Exception {
        mvc.perform(get("/api/sections")
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().isOk());
        verify(sectionService, times(1)).getAllSections("val");
    }
}

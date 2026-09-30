package com.BTA.SmartPrep.controller;

import com.BTA.SmartPrep.service.SolutionService;
import com.BTA.SmartPrep.domain.dto.submission.SubmissionHistoryDto;
import com.BTA.SmartPrep.domain.entity.SolutionRating;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SolutionController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class SolutionHistoryControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private SolutionService solutionService;

    @Test
    void historyReturnsNewestSubmissionFirst() throws Exception {
        String userId = "1df278cf-cf43-4bf7-9c34-dc9a9ded6ead";
        when(solutionService.getSubmissions(userId)).thenReturn(List.of(
                new SubmissionHistoryDto("new", "Contains Duplicate", SolutionRating.GREEN,
                        LocalDateTime.parse("2026-09-30T15:00:00"), "newer source"),
                new SubmissionHistoryDto("old", "Two Sum", SolutionRating.YELLOW,
                        LocalDateTime.parse("2026-09-29T15:00:00"), "older source")
        ));
        mockMvc.perform(get("/api/v1/solution/submissions").param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].problemTitle").value("Contains Duplicate"))
                .andExpect(jsonPath("$[0].rating").value("GREEN"))
                .andExpect(jsonPath("$[1].problemTitle").value("Two Sum"));
    }
}

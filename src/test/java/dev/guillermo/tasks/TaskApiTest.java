package dev.guillermo.tasks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskApiTest {
    @Autowired MockMvc mvc;

    private void create(String title, String status) throws Exception {
        mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"" + title + "\",\"status\":\"" + status + "\"}"))
           .andExpect(status().isCreated());
    }

    @Test
    void createAndFetchTask() throws Exception {
        mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Write README\",\"dueDate\":\"2026-12-01\"}"))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.status").value("TODO"))
           .andExpect(jsonPath("$.dueDate").value("2026-12-01"));
    }

    @Test
    void blankTitleIsRejected() throws Exception {
        mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"  \"}"))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.title").exists());
    }

    @Test
    void unknownIdReturns404() throws Exception {
        mvc.perform(get("/api/tasks/99999")).andExpect(status().isNotFound());
    }

    @Test
    void updateAndDelete() throws Exception {
        String body = mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Old\"}")).andReturn().getResponse().getContentAsString();
        String id = body.replaceAll(".*\"id\":(\\d+).*", "$1");
        mvc.perform(put("/api/tasks/" + id).contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New\",\"status\":\"DONE\"}"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.title").value("New"))
           .andExpect(jsonPath("$.status").value("DONE"));
        mvc.perform(delete("/api/tasks/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/tasks/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void filterByStatusAndPaginate() throws Exception {
        for (int i = 0; i < 3; i++) create("done-" + i, "DONE");
        mvc.perform(get("/api/tasks").param("status", "DONE").param("size", "2"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.content.length()").value(2))
           .andExpect(jsonPath("$.totalElements").value(org.hamcrest.Matchers.greaterThanOrEqualTo(3)));
    }
}

package net.cabrasky.yambo.controllers;

import net.cabrasky.yambo.models.Project;
import net.cabrasky.yambo.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @Test
    void testGetAllProjects() throws Exception {
        // Arrange
        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Project 1");
        project1.setLogo("Project_1_logo.png");
        project1.setDescription("Project description 1");

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Project 2");
        project2.setLogo("Project_2_logo.png");
        project2.setDescription("Project description 2");

        when(projectService.getAllProjects()).thenReturn(List.of(project1, project2));

        // Act & Assert
        mockMvc.perform(get("/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Project 1"))
                .andExpect(jsonPath("$[0].logo").value("Project_1_logo.png"))
                .andExpect(jsonPath("$[0].description").value("Project description 1"))
                .andExpect(jsonPath("$[1].name").value("Project 2"))
                .andExpect(jsonPath("$[1].logo").value("Project_2_logo.png"))
                .andExpect(jsonPath("$[1].description").value("Project description 2"));
    }
}

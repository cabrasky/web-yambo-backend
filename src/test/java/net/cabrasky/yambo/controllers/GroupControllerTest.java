package net.cabrasky.yambo.controllers;

import net.cabrasky.yambo.models.Group;
import net.cabrasky.yambo.models.Project;
import net.cabrasky.yambo.services.GroupService;
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

class GroupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void testGetAllGroups() throws Exception {
        // Arrange
        Project project = new Project();
        project.setId(1L);

        Group group1 = new Group();
        group1.setId(1L);
        group1.setName("Group 1");
        group1.setProject(project);

        Group group2 = new Group();
        group2.setId(2L);
        group2.setName("Group 2");
        group2.setProject(project);

        when(groupService.getAllGroups()).thenReturn(List.of(group1, group2));

        // Act & Assert
        mockMvc.perform(get("/groups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Group 1"))
                .andExpect(jsonPath("$[1].name").value("Group 2"));
    }
}

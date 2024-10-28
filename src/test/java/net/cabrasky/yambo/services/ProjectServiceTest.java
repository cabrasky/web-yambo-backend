package net.cabrasky.yambo.services;

import net.cabrasky.yambo.models.Project;
import net.cabrasky.yambo.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProjects() {
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

        when(projectRepository.findAll()).thenReturn(List.of(project1, project2));

        // Act
        List<Project> projects = projectService.getAllProjects();

        // Assert
        assertEquals(2, projects.size());
        assertEquals("Project 1", projects.get(0).getName());
        assertEquals("Project_1_logo.png", projects.get(0).getLogo());
        assertEquals("Project description 1", projects.get(0).getDescription());
        assertEquals("Project 2", projects.get(1).getName());
        assertEquals("Project_2_logo.png", projects.get(1).getLogo());
        assertEquals("Project description 2", projects.get(1).getDescription());

    }
}

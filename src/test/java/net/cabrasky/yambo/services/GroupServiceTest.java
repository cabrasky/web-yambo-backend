package net.cabrasky.yambo.services;

import net.cabrasky.yambo.models.Group;
import net.cabrasky.yambo.models.Project;
import net.cabrasky.yambo.repositories.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGroups() {
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

        when(groupRepository.findAll()).thenReturn(List.of(group1, group2));

        // Act
        List<Group> groups = groupService.getAllGroups();

        // Assert
        assertEquals(2, groups.size());
        assertEquals("Group 1", groups.get(0).getName());
        assertEquals("Group 2", groups.get(1).getName());
    }
}

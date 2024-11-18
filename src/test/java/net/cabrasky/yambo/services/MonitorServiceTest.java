package net.cabrasky.yambo.services;

import net.cabrasky.yambo.models.Group;
import net.cabrasky.yambo.models.Monitor;
import net.cabrasky.yambo.repositories.MonitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class MonitorServiceTest {

    @Mock
    private MonitorRepository monitorRepository;

    @InjectMocks
    private MonitorService monitorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMonitors() {
        // Arrange
        Group group1 = new Group();
        Group group2 = new Group();

        Monitor monitor1 = new Monitor();
        monitor1.setId(1L);
        monitor1.setName("Monitor 1");
        monitor1.setImage("image1");
        monitor1.setDescription("Description for Monitor 1");
        monitor1.setGroups(Set.of(group1));

        Monitor monitor2 = new Monitor();
        monitor2.setId(2L);
        monitor2.setName("Monitor 2");
        monitor2.setImage("image2");
        monitor2.setDescription("Description for Monitor 2");
        monitor2.setGroups(Set.of(group2));

        when(monitorRepository.findAll()).thenReturn(List.of(monitor1, monitor2));

        // Act
        List<Monitor> monitors = monitorService.getAllMonitors();

        // Assert
        assertEquals(2, monitors.size());
        assertEquals("Monitor 1", monitors.get(0).getName());
        assertEquals("image1", monitors.get(0).getImage());
        assertTrue(monitors.get(0).getGroups().contains(group1));
        assertEquals("image1", monitors.get(0).getImage());
        assertEquals("Monitor 2", monitors.get(1).getName());
        assertEquals("image2", monitors.get(1).getImage());
        assertTrue(monitors.get(1).getGroups().contains(group2));
    }

    @Test
    void testGetMonitorsByGroupName() {
        // Arrange
        Monitor monitor = new Monitor();
        monitor.setId(1L);
        monitor.setName("Monitor 1");
        monitor.setDescription("Description for Monitor 1");

        String groupName = "Group A";
        
        when(monitorRepository.findByGroupName(groupName)).thenReturn(List.of(monitor));

        // Act
        List<Monitor> monitors = monitorService.getMonitorsByGroupName(groupName);

        // Assert
        assertEquals(1, monitors.size());
        assertEquals("Monitor 1", monitors.get(0).getName());
        assertEquals("Description for Monitor 1", monitors.get(0).getDescription());
    }
}

package net.cabrasky.yambo.controllers;

import net.cabrasky.yambo.models.Monitor;
import net.cabrasky.yambo.services.MonitorService;
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

class MonitorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MonitorService monitorService;

    @InjectMocks
    private MonitorController monitorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(monitorController).build();
    }

    @Test
    void testGetAllMonitorsWithoutGroupName() throws Exception {
        // Arrange
        Monitor monitor1 = new Monitor();
        monitor1.setId(1L);
        monitor1.setName("Monitor 1");
        monitor1.setDescription("Description for Monitor 1");

        Monitor monitor2 = new Monitor();
        monitor2.setId(2L);
        monitor2.setName("Monitor 2");
        monitor2.setDescription("Description for Monitor 2");

        when(monitorService.getAllMonitors()).thenReturn(List.of(monitor1, monitor2));

        // Act & Assert
        mockMvc.perform(get("/monitors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Monitor 1"))
                .andExpect(jsonPath("$[0].description").value("Description for Monitor 1"))
                .andExpect(jsonPath("$[1].name").value("Monitor 2"))
                .andExpect(jsonPath("$[1].description").value("Description for Monitor 2"));
    }

    @Test
    void testGetAllMonitorsWithGroupName() throws Exception {
        // Arrange
        Monitor monitor = new Monitor();
        monitor.setId(1L);
        monitor.setName("Monitor 1");
        monitor.setDescription("Description for Monitor 1");

        String groupName = "Group A";

        when(monitorService.getMonitorsByGroupName(groupName)).thenReturn(List.of(monitor));

        // Act & Assert
        mockMvc.perform(get("/monitors")
                        .param("groupName", groupName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Monitor 1"))
                .andExpect(jsonPath("$[0].description").value("Description for Monitor 1"));
    }
}

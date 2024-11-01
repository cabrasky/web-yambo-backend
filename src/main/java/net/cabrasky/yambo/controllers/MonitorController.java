package net.cabrasky.yambo.controllers;

import net.cabrasky.yambo.models.Monitor;
import net.cabrasky.yambo.services.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monitors")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @GetMapping
    public ResponseEntity<List<Monitor>> getAllMonitors(@RequestParam(required = false) String groupName) {
        List<Monitor> monitors;
        if (groupName != null) {
            monitors = monitorService.getMonitorsByGroupName(groupName);
        } else {
            monitors = monitorService.getAllMonitors();
        }
        return new ResponseEntity<>(monitors, HttpStatus.OK);
    }

}

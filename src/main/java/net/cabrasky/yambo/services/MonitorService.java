package net.cabrasky.yambo.services;

import net.cabrasky.yambo.models.Monitor;
import net.cabrasky.yambo.repositories.MonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorService {

    @Autowired
    private MonitorRepository monitorRepository;

    public List<Monitor> getAllMonitors() {
        return monitorRepository.findAll();
    }

    public List<Monitor> getMonitorsByGroupName(String groupName) {
        return monitorRepository.findByGroupName(groupName);
    }
}

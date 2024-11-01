package net.cabrasky.yambo.services;

import net.cabrasky.yambo.models.Group;
import net.cabrasky.yambo.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }
}

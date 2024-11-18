package net.cabrasky.yambo.controllers;

import net.cabrasky.yambo.models.Group;
import net.cabrasky.yambo.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups =  groupService.getAllGroups();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }
}

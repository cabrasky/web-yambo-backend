package net.cabrasky.yambo.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Monitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 600)
    private String description;

    @Column(length = 200)
    private String image;

    @ManyToMany
    @JoinTable(
        name = "monitor_group",
        joinColumns = @JoinColumn(name = "monitor_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> groups;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
}

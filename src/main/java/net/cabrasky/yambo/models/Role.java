package net.cabrasky.yambo.models;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority  {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Override
    public String getAuthority() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
}

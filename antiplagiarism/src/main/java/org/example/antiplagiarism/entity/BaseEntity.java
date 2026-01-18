package org.example.antiplagiarism.entity;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {

    private String id;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

}

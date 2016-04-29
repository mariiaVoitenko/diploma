package com.voitenko.diploma.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Sightseeing_content.
 */
@Entity
@Table(name = "sightseeing_content")
public class Sightseeing_content implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Content content;

    @ManyToOne
    private Sightseeing sightseeing;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Sightseeing getSightseeing() {
        return sightseeing;
    }

    public void setSightseeing(Sightseeing sightseeing) {
        this.sightseeing = sightseeing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sightseeing_content sightseeing_content = (Sightseeing_content) o;
        if(sightseeing_content.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sightseeing_content.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sightseeing_content{" +
            "id=" + id +
            '}';
    }
}

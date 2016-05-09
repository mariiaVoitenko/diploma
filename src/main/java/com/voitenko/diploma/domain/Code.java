package com.voitenko.diploma.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Code.
 */
@Entity
@Table(name = "code")
public class Code implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "picture", nullable = false)
    private String picture;

    @ManyToOne
    private Sightseeing sightseeing;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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
        Code code = (Code) o;
        if(code.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, code.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Code{" +
            "id=" + id +
            ", picture='" + picture + "'" +
            '}';
    }
}

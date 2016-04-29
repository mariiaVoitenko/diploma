package com.voitenko.diploma.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Sightseeing.
 */
@Entity
@Table(name = "sightseeing")
public class Sightseeing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "info", nullable = false)
    private String info;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Float longitude;

    @NotNull
    @Column(name = "photo", nullable = false)
    private String photo;

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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sightseeing sightseeing = (Sightseeing) o;
        if(sightseeing.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sightseeing.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sightseeing{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", info='" + info + "'" +
            ", latitude='" + latitude + "'" +
            ", longitude='" + longitude + "'" +
            ", photo='" + photo + "'" +
            '}';
    }
}

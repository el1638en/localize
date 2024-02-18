package api.com.medhead.localize.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "speciality")
public class Speciality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String name;
    @ManyToMany
    @JsonIgnore
    private List<Hospital> hospitals;
    @ManyToOne //plusieurs specialités pour un seul groupe
    @JoinColumn(name = "specialityGroup_fk")
    @JsonIgnore
    private SpecialityGroup specialityGroup;

    public Speciality(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Speciality() {
    }
}

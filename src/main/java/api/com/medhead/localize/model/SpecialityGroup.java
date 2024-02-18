package api.com.medhead.localize.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="specialityGroup")
public class SpecialityGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "specialityGroup", cascade = CascadeType.REMOVE)
    private List<Speciality> specialities = new ArrayList<>();

    public SpecialityGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public SpecialityGroup(String name) {
        this.name = name;
    }
    public SpecialityGroup() {
    }
}

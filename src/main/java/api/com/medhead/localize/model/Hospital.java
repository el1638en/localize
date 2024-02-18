package api.com.medhead.localize.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import java.util.Comparator;
import java.util.List;

@Entity
@Table(name="hospital")
@Data
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "city")
    private String city;
    @Column(name = "postCode")
    private String postCode;
    @Column(name = "longitude")
    private double longitude;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "phone")
    private String phone;
    @Column(name = "website")
    private String website;
    @Column(name = "email")
    private String email;
    @Column(name = "available_beds", columnDefinition = "integer default 0")
    private int availableBeds;
    @Transient
    private Long distanceInTime;
    @Transient
    private String formattedDistanceInTime;
    @Transient
    private String formattedDistanceInKm;

    @ManyToMany(mappedBy = "hospitals", cascade = CascadeType.REMOVE)
    private List<Speciality> specialities;

    public Hospital(int id, String name, String address, String city, String postCode, double longitude, double latitude, String phone, String website, String email, int availableBeds) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.postCode = postCode;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phone = phone;
        this.website = website;
        this.email = email;
        this.availableBeds = availableBeds;
    }

    public Hospital() {
    }

    public static Comparator<Hospital> HospitalTimeComparator = new Comparator<Hospital>() {

        @Override
        public int compare(Hospital o1, Hospital o2) {
            Long hospitalTime1= o1.getDistanceInTime();
            Long hospitalTime2= o2.getDistanceInTime();

            // Returning in ascending order
            return hospitalTime1.compareTo(hospitalTime2);
        }
    };
}


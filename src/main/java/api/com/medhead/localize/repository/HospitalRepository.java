package api.com.medhead.localize.repository;


import api.com.medhead.localize.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository  extends JpaRepository<Hospital, Integer> {
    @Query(value = "SELECT distinct * FROM hospital h inner join speciality_hospitals sh on sh.hospitals_id=h.id and sh.specialities_id=:specialityId WHERE (h.latitude between :latitudeRight and :latitudeLeft) AND (h.longitude between :longitudeRight and :longitudeLeft) AND (h.available_beds > 0) LIMIT 5", nativeQuery = true)
    List<Hospital> findHospitalWithinPerimeterWithSpeciality(
            @Param("latitudeLeft") Double latitudeLeft,
            @Param("latitudeRight") Double latitudeRight,
            @Param("longitudeRight") Double longitudeRight,
            @Param("longitudeLeft") Double longitudeLeft,
            @Param("specialityId") int specialityId);
}


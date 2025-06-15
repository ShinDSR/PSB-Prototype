package shin_nc.psb_test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shin_nc.psb_test.entity.Biodata;
import shin_nc.psb_test.entity.User;

@Repository
public interface BiodataRepository extends JpaRepository<Biodata, Long> {

    Optional<Biodata> findByNisn(String nisn);

    Optional<Biodata> findByUser(User user);
}

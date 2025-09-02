package shin_nc.psb_test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shin_nc.psb_test.entity.Parents;
import shin_nc.psb_test.entity.User;

@Repository
public interface ParentsRepository extends JpaRepository<Parents, Long> {

    Optional<Parents> findByUser(User user);
}

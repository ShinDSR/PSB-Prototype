package shin_nc.psb_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shin_nc.psb_test.entity.Parent;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {

}

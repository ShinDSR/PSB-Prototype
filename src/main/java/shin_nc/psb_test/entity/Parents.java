package shin_nc.psb_test.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "parents")
public class Parents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String fatherName;

    @Column(length = 100, nullable = false)
    private String fatherJob;

    @Column(length = 15, nullable = true)
    private String fatherPhoneNumber;

    @Column(length = 100, nullable = false)
    private String motherName;

    @Column(length = 100, nullable = false)
    private String motherJob;

    @Column(length = 15, nullable = true)
    private String motherPhoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}

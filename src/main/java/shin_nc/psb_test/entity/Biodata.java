package shin_nc.psb_test.entity;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "biodatas")
public class Biodata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private AppGender gender;

    @Enumerated(EnumType.STRING)
    private AppReligion religion;
    
    @Column(length = 100, nullable = false)
    private String placeOfBirth;
    
    @Column(nullable = false)
    private Date birthDate;
        
    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 15, nullable = false)
    private String phoneNumber;

    @Column(length = 10, nullable = false, unique = true)
    private String nisn;

    @Column(length = 100, nullable = false)
    private String schoolFrom;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(mappedBy = "biodata", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Parent parent;

    @OneToOne(mappedBy = "biodata", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Registration registration;
    
}

package shin_nc.psb_test.service;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shin_nc.psb_test.dto.BiodataResponse;
import shin_nc.psb_test.entity.User;
import shin_nc.psb_test.repository.BiodataRepository;

@Service
public class BiodataService {

    @Autowired
    private BiodataRepository biodataRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional(readOnly = true)
    public BiodataResponse getCurrentBiodata(User user) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return BiodataResponse.builder()
                .id(user.getBiodata().getId())
                .name(user.getBiodata().getName())
                .gender(user.getBiodata().getGender().name())
                .religion(user.getBiodata().getReligion().name())
                .placeOfBirth(user.getBiodata().getPlaceOfBirth())
                .birthDate(dateFormat.format(user.getBiodata().getBirthDate()))
                .address(user.getBiodata().getAddress())
                .phoneNumber(user.getBiodata().getPhoneNumber())
                .nisn(user.getBiodata().getNisn())
                .schoolFrom(user.getBiodata().getSchoolFrom())
                .build();
    }

    
}

package shin_nc.psb_test.service;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import shin_nc.psb_test.dto.BiodataResponse;
import shin_nc.psb_test.dto.BiodataUpdateRequest;
import shin_nc.psb_test.entity.AppGender;
import shin_nc.psb_test.entity.AppReligion;
import shin_nc.psb_test.entity.Biodata;
import shin_nc.psb_test.entity.User;
import shin_nc.psb_test.repository.BiodataRepository;

@Service
public class BiodataService {

    @Autowired
    private BiodataRepository biodataRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional(readOnly = true)
    public BiodataResponse getCurrent(User user) {
        Biodata biodata = biodataRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Biodata not found"));

        return toBiodataResponse(biodata);
    }

    private BiodataResponse toBiodataResponse(Biodata biodata) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return BiodataResponse.builder()
                .id(biodata.getId())
                .name(biodata.getName())
                .gender(biodata.getGender().name())
                .religion(biodata.getReligion().name())
                .placeOfBirth(biodata.getPlaceOfBirth())
                .birthDate(dateFormat.format(biodata.getBirthDate()))
                .address(biodata.getAddress())
                .phoneNumber(biodata.getPhoneNumber())
                .nisn(biodata.getNisn())
                .schoolFrom(biodata.getSchoolFrom())
                .build();
    }
    
    @Transactional
    public BiodataResponse updateCurrent(User user, BiodataUpdateRequest request){
        validationService.validate(request);

        Biodata biodata = biodataRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Biodata not found"));

        biodata.setName(request.getName());
        biodata.setGender(AppGender.valueOf(request.getGender().toUpperCase()));
        biodata.setReligion(AppReligion.valueOf(request.getReligion().toUpperCase()));
        biodata.setPlaceOfBirth(request.getPlaceOfBirth());
        biodata.setBirthDate(java.sql.Date.valueOf(request.getBirthDate()));
        biodata.setAddress(request.getAddress());
        biodata.setPhoneNumber(request.getPhoneNumber());
        biodata.setNisn(request.getNisn());
        biodata.setSchoolFrom(request.getSchoolFrom());
        biodataRepository.save(biodata);
        return toBiodataResponse(biodata);
    }
}

package shin_nc.psb_test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import shin_nc.psb_test.dto.ParentsRequest;
import shin_nc.psb_test.dto.ParentsResponse;
import shin_nc.psb_test.entity.Parents;
import shin_nc.psb_test.entity.User;
import shin_nc.psb_test.repository.ParentsRepository;

@Service
public class ParentsService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ParentsRepository parentsRepository;

    @Transactional
    public ParentsResponse createCurrent(User user, ParentsRequest request){
        validationService.validate(request);

        Parents parents = new Parents();
        parents.setFatherName(request.getFatherName());
        parents.setFatherJob(request.getFatherJob());
        parents.setFatherPhoneNumber(request.getFatherPhoneNumber());
        parents.setMotherName(request.getMotherName());
        parents.setMotherJob(request.getMotherJob());
        parents.setMotherPhoneNumber(request.getMotherPhoneNumber());
        parents.setUser(user);

        parentsRepository.save(parents);
        
        return toParentsResponse(parents);
    }

    private ParentsResponse toParentsResponse(Parents parents) {
        // User user = parents.getUser();

        // UserResponse dto = UserResponse.builder()
        //     .id(user.getId())
        //     .email(user.getEmail())
        //     .build();

        return ParentsResponse.builder()
                .id(parents.getId())
                .fatherName(parents.getFatherName())
                .fatherJob(parents.getFatherJob())
                .fatherPhoneNumber(parents.getFatherPhoneNumber())
                .motherName(parents.getMotherName())
                .motherJob(parents.getMotherJob())
                .motherPhoneNumber(parents.getMotherPhoneNumber())
                // .user(dto)
                .build();
    }

    @Transactional(readOnly = true)
    public ParentsResponse getCurrent(User user){
        Parents parents = parentsRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data parents not found"));

        return toParentsResponse(parents);
    }

    @Transactional
    public ParentsResponse updateCurrent(User user, ParentsRequest request){
        validationService.validate(request);

        Parents parents = parentsRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data parents not found"));

        parents.setFatherName(request.getFatherName());
        parents.setFatherJob(request.getFatherJob());
        parents.setFatherPhoneNumber(request.getFatherPhoneNumber());
        parents.setMotherName(request.getMotherName());
        parents.setMotherJob(request.getMotherJob());
        parents.setMotherPhoneNumber(request.getMotherPhoneNumber());
        parentsRepository.save(parents);
        
        return toParentsResponse(parents);        
    }

}

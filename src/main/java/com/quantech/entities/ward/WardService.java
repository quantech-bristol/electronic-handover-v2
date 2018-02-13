package com.quantech.entities.ward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WardService {

    @Autowired
    WardRepository wardRepository;

    /**
     * Finds all wards currently stored in the repository.
     * @return A list of all wards currently stored, potentially corresponding to all wards in a hospital.
     */
    public List<Ward> getAllWards() {
        List<Ward> wards = new ArrayList<>();
        wardRepository.findAll().forEach(wards::add);
        return wards;
    }

    /**
     * Finds a ward corresponding to the unique id, stored in the repository.
     * @param id The id for which a ward is a associated with.
     * @return A ward corresponding to the id if one exists, null otherwise.
     */
    public Ward getWard(Long id) {
        return wardRepository.findOne(id);
    }

    /**
     * Save a given ward into the repository.
     * @param ward The ward to be saved.
     */
    public void saveWard(Ward ward) {
        wardRepository.save(ward);
    }

    /**
     * Deletes a given ward from the repository.
     * @param id The id corresponding to the ward that is to be deleted.
     */
    public void deleteWard(Long id) {
        wardRepository.delete(id);
    }

}

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

    public List<Ward> getAllWards() {
        List<Ward> wards = new ArrayList<>();
        wardRepository.findAll().forEach(wards::add);
        return wards;
    }

    public Ward getWard(Long id) {
        return wardRepository.findOne(id);
    }

    public void saveWard(Ward ward) {
        wardRepository.save(ward);
    }

    public void deleteWard(Long id) {
        wardRepository.delete(id);
    }

    private boolean isWardNonempty(Ward ward) {
        Integer c = ward.getCapacity();
        if (c > 0) return true;
        return false;
    }

    public List<Ward> getAllNonemptyWards() {
        List<Ward> wards = new ArrayList<>();
        wardRepository.findAll().forEach(wards::add);
        return wards.stream()
                    .filter(w -> isWardNonempty(w))
                    .collect(Collectors.toList());
    }
}

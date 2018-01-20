package com.quantech.ward;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WardRepository extends CrudRepository<Ward,Long> {

    public List<Ward> findByName(String name);
}

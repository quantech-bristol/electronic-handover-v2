package com.quantech.oldEntities.team;

import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {
    public Team findById(Long id);
}

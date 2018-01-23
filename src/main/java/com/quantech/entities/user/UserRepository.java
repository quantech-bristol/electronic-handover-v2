package com.quantech.entities.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserCore, Long> {

    public UserCore findUserCoreByUsername(String username);

    public UserCore getUserCoreByIdEquals(long id);
}

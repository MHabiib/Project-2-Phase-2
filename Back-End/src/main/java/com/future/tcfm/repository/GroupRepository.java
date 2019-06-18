package com.future.tcfm.repository;

import com.future.tcfm.model.Group;
import com.future.tcfm.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {
    Group findByName (String name);
    Group findByIdGroup (String id);
}

package com.github.kmu_wink.domain.user.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.github.kmu_wink.domain.user.schema.User;

public interface UserRepository extends MongoRepository<User, String> {

	boolean existsBySocialId(String socialId);

	Optional<User> findBySocialId(String socialId);
}

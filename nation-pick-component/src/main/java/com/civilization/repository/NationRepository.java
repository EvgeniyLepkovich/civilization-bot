package com.civilization.repository;

import com.civilization.model.Nation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NationRepository extends CrudRepository<Nation, Long> {
    @Query(value = "select * from nation where active is true order by rand() limit 18", nativeQuery = true)
    List<Nation> getFFA6RandomNation();
}

package com.civilization.repository;

import com.civilization.model.ActiveGame;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActiveGameRepository extends CrudRepository<ActiveGame, Long> {

    @Query(value = "select * from active_game ag where game_status = 'CREATE' and ag.start_date < NOW() - INTERVAL 7 DAY", nativeQuery = true)
    List<ActiveGame> getGamesOlderThenWeek();
}

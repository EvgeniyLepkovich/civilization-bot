package com.civilization.repository;

import com.civilization.model.Lobby;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LobbyRepository extends CrudRepository<Lobby, Long> {
    Lobby findByGameId(Long gameId);

    @Query(value =
            "select CASE when count(*) > 0 THEN true ELSE false END from Lobby l where l.lobbyStatus = 'STARTED' and l.gameId = :gameId")
    boolean isGameStarted(@Param("gameId") Long gameId);

    @Query(value =
            "select CASE when count(*) > 0 THEN true ELSE false END from Lobby l where l.lobbyStatus = 'CLOSED' and l.gameId = :gameId")
    boolean isLobbyClosed(@Param("gameId") Long gameId);

    @Query(value =
            "select CASE when count(*) > 0 THEN true ELSE false END from Lobby l where l.lobbyStatus = 'CREATED' and l.gameId = :gameId")
    boolean isLobbyCreated(Long gameId);
}

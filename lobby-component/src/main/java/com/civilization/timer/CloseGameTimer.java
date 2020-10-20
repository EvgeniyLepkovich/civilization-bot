package com.civilization.timer;

import com.civilization.discord.MessageSender;
import com.civilization.dto.LobbyDto;
import com.civilization.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class CloseGameTimer {

    private static final long THIRTY_MINUTES_DELAY = 1800000L;
    private static final String CLOSE_GAME_MESSAGE = "Game #%s was canceled cuz of not enough players in time";

    @Autowired
    private LobbyService lobbyService;
    @Autowired
    private MessageSender messageSender;

    public void runCloseGameTimer(LobbyDto lobbyDto) {
        TimerTask task = new TimerTask() {
            public void run() {
                if (lobbyService.isLobbyGameStarted(lobbyDto)) {
                    return;
                }

                lobbyService.closeLobbyGame(lobbyDto);
                messageSender.sendMessageToRatingGameChannel(String.format(CLOSE_GAME_MESSAGE, lobbyDto.getGameId()));
            }
        };

        Timer timer = new Timer("CloseGameTimer#" + lobbyDto.getGameId());
        timer.schedule(task, THIRTY_MINUTES_DELAY);
    }
}

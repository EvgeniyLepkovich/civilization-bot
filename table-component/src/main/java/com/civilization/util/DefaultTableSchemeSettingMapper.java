package com.civilization.util;

import com.civilization.dto.TableRowDTO;
import com.civilization.dto.TableSettingDTO;
import com.civilization.dto.UserDTO;
import com.civilization.dto.builder.TableSettingBuilder;
import com.civilization.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultTableSchemeSettingMapper {

    public TableSettingDTO toSetting(List<UserDTO> users, Long gameId, TableConfiguration tableConfiguration) {
        return new TableSettingBuilder()
                .withRows(toTableRows(users, gameId, tableConfiguration))
                .withWidthCalculator(tableConfiguration.getWidthCalculatorType().getCalculator())
                .withGrid(tableConfiguration.getGridType().getGridType())
                .withPaddingLeftRight(tableConfiguration.getPaddingLeft())
                .build();
    }

    private List<TableRowDTO> toTableRows(List<UserDTO> users, Long gameId, TableConfiguration tableConfiguration) {
        List<TableRowDTO> tableRowDTOS = new ArrayList<>();
        tableRowDTOS.add(getHeaderRow(gameId, tableConfiguration.getHeaderRowTemplate()));
        tableRowDTOS.add(getColumnRow(tableConfiguration.getColumnRowTemplate()));
        tableRowDTOS.addAll(getUserRows(users, tableConfiguration.getUserRowTemplate(), gameId));
        return tableRowDTOS;
    }

    private TableRowDTO getColumnRow(String columnRowTemplate) {
        TableRowDTO columnRow = new TableRowDTO();
        columnRow.addCell(columnRowTemplate.split("\\|"));
        return columnRow;
    }

    private List<TableRowDTO> getUserRows(List<UserDTO> users, String userRowTemplate, Long gameId) {
        return users.stream()
                .map(user -> toUserRow(user.getUser(), user.getUserRank(), userRowTemplate, gameId))
                .collect(Collectors.toList());
    }

    private TableRowDTO toUserRow(User user, UserRank userRank, String userRowTemplate, Long gameId) {
        String[] cellsValues = userRowTemplate
                .replace("{order}", String.valueOf(getUserSlot(user, gameId)))
                .replace("{name}", user.getUsername())
                .replace("{games}", userRank.getGamesCount().toString())
                .replace("{wins}", userRank.getWins().toString())
                .replace("{leave}", userRank.getLeaves().toString())
                .replace("{rating}", userRank.getRating().toString())
                .replace("{rolls}", toRolls(user, gameId))
                .replace("{ready}", String.valueOf(isUserConfirmedParticipationInGame(user, gameId)))
                .split("\\|");
        TableRowDTO tableRowDTO = new TableRowDTO();
        tableRowDTO.addCell(cellsValues);
        return tableRowDTO;
    }

    private String toRolls(User user, Long gameId) {
        return user.getUserActiveGames().stream()
                .filter(uag -> uag.getActiveGame().getId() == gameId)
                .map(UserActiveGame::getUserNationRoll)
                .map(UserNationRoll::getNationRolls)
                .flatMap(Collection::stream)
                .map(Nation::getEmojiCode)
                .collect(Collectors.joining(", "));

    }

    private boolean isUserConfirmedParticipationInGame(User user, Long gameId) {
        return user.getUserActiveGames().stream()
                .filter(uag -> uag.getActiveGame().getId() == gameId)
                .map(UserActiveGame::isGameConfirmed)
                .findFirst()
                .orElse(false);
    }

    private Integer getUserSlot(User user, Long gameId) {
        return user.getUserActiveGames()
                .stream()
                .filter(uag -> uag.getActiveGame().getId() == gameId)
                .map(UserActiveGame::getSlot)
                .findFirst()
                .orElse(0);
    }

    private TableRowDTO getHeaderRow(Long gameId, String headerRowTemplate) {
        TableRowDTO headerRow = new TableRowDTO();
        headerRow.addCell(null, null, null, null, headerRowTemplate.replace("{gameId}", gameId.toString()));
        return headerRow;
    }
}

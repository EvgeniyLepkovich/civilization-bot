package com.civilization.util;

import com.civilization.dto.GameResultDTO;
import com.civilization.dto.TableRowDTO;
import com.civilization.dto.TableSettingDTO;
import com.civilization.dto.builder.TableSettingBuilder;
import com.civilization.model.TableConfiguration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameFinishedTableSchemeSettingMapper {
    public TableSettingDTO toSetting(List<GameResultDTO> gameResultDTOs, Long gameId, TableConfiguration tableConfiguration) {
        return new TableSettingBuilder()
                .withRows(toTableRows(gameResultDTOs, gameId, tableConfiguration))
                .withWidthCalculator(tableConfiguration.getWidthCalculatorType().getCalculator())
                .withGrid(tableConfiguration.getGridType().getGridType())
                .withPaddingLeftRight(tableConfiguration.getPaddingLeft())
                .build();
    }

    private List<TableRowDTO> toTableRows(List<GameResultDTO> gameResultDTOs, Long gameId, TableConfiguration tableConfiguration) {
        List<TableRowDTO> tableRowDTOS = new ArrayList<>();
        tableRowDTOS.add(getHeaderRow(gameId, tableConfiguration.getHeaderRowTemplate()));
        tableRowDTOS.add(getColumnRow(tableConfiguration.getColumnRowTemplate()));
        tableRowDTOS.addAll(getGameResultRows(gameResultDTOs, tableConfiguration.getUserRowTemplate()));
        return tableRowDTOS;
    }

    private TableRowDTO getColumnRow(String columnRowTemplate) {
        TableRowDTO columnRow = new TableRowDTO();
        columnRow.addCell(columnRowTemplate.split("\\|"));
        return columnRow;
    }

    private List<TableRowDTO> getGameResultRows(List<GameResultDTO> gameResultDTOs, String gameResultRowTemplate) {
        List<TableRowDTO> tableRows = new ArrayList<>();
        int slot = 1;
        for (GameResultDTO gr : gameResultDTOs) {
            TableRowDTO tableRowDTO = toGameResultRow(gr, gameResultRowTemplate, slot++);
            tableRows.add(tableRowDTO);
        }
        return tableRows;
    }

    private TableRowDTO toGameResultRow(GameResultDTO gameResult, String userRowTemplate, Integer slot) {
        String[] cellsValues = userRowTemplate
                .replace("{order}", String.valueOf(slot))
                .replace("{name}", gameResult.getUsername())
                .replace("{result}", gameResult.getGameResult().toString())
                .replace("{oldRating}", gameResult.getOldRating().toString())
                .replace("{newRating}", gameResult.getNewRating().toString())
                .split("\\|");
        TableRowDTO tableRowDTO = new TableRowDTO();
        tableRowDTO.addCell(cellsValues);
        return tableRowDTO;
    }

    private TableRowDTO getHeaderRow(Long gameId, String headerRowTemplate) {
        TableRowDTO headerRow = new TableRowDTO();
        headerRow.addCell(null, null, null, headerRowTemplate.replace("{gameId}", gameId.toString()));
        return headerRow;
    }
}

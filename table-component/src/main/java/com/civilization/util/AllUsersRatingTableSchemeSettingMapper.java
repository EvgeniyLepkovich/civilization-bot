package com.civilization.util;

import com.civilization.dto.TableRowDTO;
import com.civilization.dto.TableSettingDTO;
import com.civilization.dto.builder.TableSettingBuilder;
import com.civilization.model.TableConfiguration;
import com.civilization.model.UserRank;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AllUsersRatingTableSchemeSettingMapper {

    public TableSettingDTO toSetting(List<UserRank> userRanks, TableConfiguration tableConfiguration) {
        return new TableSettingBuilder()
                .withRows(toTableRows(userRanks, tableConfiguration))
                .withWidthCalculator(tableConfiguration.getWidthCalculatorType().getCalculator())
                .withGrid(tableConfiguration.getGridType().getGridType())
                .withPaddingLeftRight(tableConfiguration.getPaddingLeft())
                .build();
    }

    private List<TableRowDTO> toTableRows(List<UserRank> userRanks, TableConfiguration tableConfiguration) {
        List<TableRowDTO> tableRowDTOS = new ArrayList<>();
        tableRowDTOS.add(getColumnRow(tableConfiguration.getColumnRowTemplate()));
        tableRowDTOS.addAll(getBodyRows(userRanks, tableConfiguration.getUserRowTemplate()));
        return tableRowDTOS;
    }

    private TableRowDTO getColumnRow(String columnRowTemplate) {
        TableRowDTO columnRow = new TableRowDTO();
        columnRow.addCell(columnRowTemplate.split("\\|"));
        return columnRow;
    }

    private List<TableRowDTO> getBodyRows(List<UserRank> userRanks, String bodyTemplate) {
        int place = 1;
        List<TableRowDTO> rows = new ArrayList<>();
        for (UserRank userRank : userRanks) {
            rows.add(toBodyRow(userRank, bodyTemplate, place++));
        }
        return rows;
    }

    private TableRowDTO toBodyRow(UserRank userRank, String bodyRowTemplate, int place) {
        String[] cellsValues = bodyRowTemplate
                .replace("{place}", String.valueOf(place))
                .replace("{name}", userRank.getUsername())
                .replace("{rating}", userRank.getRating().toString())
                .replace("{games}", userRank.getGamesCount().toString())
                .replace("{wins}", userRank.getWins().toString())
                .split("\\|");
        TableRowDTO tableRowDTO = new TableRowDTO();
        tableRowDTO.addCell(cellsValues);
        return tableRowDTO;
    }
}

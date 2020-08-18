package com.civilization.dto.builder;

import com.civilization.dto.TableConfigurationDTO;
import com.civilization.dto.TableRowDTO;
import com.civilization.dto.TableSettingDTO;
import de.vandermeer.asciitable.AT_ColumnWidthCalculator;
import de.vandermeer.asciithemes.TA_Grid;

import java.util.List;

public class TableSettingBuilder {

    private final TableSettingDTO tableSettingDTO;
    private final TableConfigurationDTO tableConfigurationDTO;

    public TableSettingBuilder() {
        tableSettingDTO = new TableSettingDTO();
        tableConfigurationDTO = new TableConfigurationDTO();
        tableSettingDTO.setTableConfigurationDTO(tableConfigurationDTO);
    }

    public TableSettingBuilder withRow(TableRowDTO tableRowDTO) {
        tableSettingDTO.addRow(tableRowDTO);
        return this;
    }

    public TableSettingBuilder withRows(List<TableRowDTO> tableRowDTO) {
        tableRowDTO.forEach(tableSettingDTO::addRow);
        return this;
    }

    public TableSettingBuilder withWidthCalculator(AT_ColumnWidthCalculator widthCalculator) {
        tableConfigurationDTO.setWidthCalculator(widthCalculator);
        return this;
    }

    public TableSettingBuilder withGrid(TA_Grid grid) {
        tableConfigurationDTO.setGrid(grid);
        return this;
    }

    public TableSettingBuilder withPaddingLeftRight(int paddingLeftRight) {
        tableConfigurationDTO.setPaddingLeft(paddingLeftRight);
        tableConfigurationDTO.setPaddingRight(paddingLeftRight);
        return this;
    }

    public TableSettingDTO build() {
        return tableSettingDTO;
    }
}

package com.civilization.dto;

import java.util.ArrayList;
import java.util.List;

public class TableSettingDTO {
    private List<TableRowDTO> rows = new ArrayList<>();
    private TableConfigurationDTO tableConfigurationDTO;

    public void addRow(TableRowDTO row) {
        rows.add(row);
    }

    public List<TableRowDTO> getRows() {
        return rows;
    }

    public TableConfigurationDTO getTableConfigurationDTO() {
        return tableConfigurationDTO;
    }

    public void setTableConfigurationDTO(TableConfigurationDTO tableConfigurationDTO) {
        this.tableConfigurationDTO = tableConfigurationDTO;
    }
}

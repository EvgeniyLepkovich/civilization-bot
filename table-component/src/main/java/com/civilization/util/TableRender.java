package com.civilization.util;

import com.civilization.dto.TableConfigurationDTO;
import com.civilization.dto.TableRowDTO;
import com.civilization.dto.TableSettingDTO;
import de.vandermeer.asciitable.AsciiTable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableRender {

    public String renderDefaultGameTable(TableSettingDTO tableSettingDTO) {
        AsciiTable asciiTable = new AsciiTable();
        addRows(asciiTable, tableSettingDTO.getRows());
        addConfiguration(asciiTable, tableSettingDTO.getTableConfigurationDTO());
        return asciiTable.render();
    }

    private void addConfiguration(AsciiTable asciiTable, TableConfigurationDTO tableConfigurationDTO) {
        asciiTable.getRenderer().setCWC(tableConfigurationDTO.getWidthCalculator());
        asciiTable.getContext().setGrid(tableConfigurationDTO.getGrid());
        asciiTable.setPaddingLeftRight(tableConfigurationDTO.getPaddingLeft(), tableConfigurationDTO.getPaddingRight());
    }

    private void addRows(AsciiTable asciiTable, List<TableRowDTO> rows) {
        rows.forEach(row -> addRow(asciiTable, row));
        asciiTable.addRule(); // to add last line of table
    }

    private void addRow(AsciiTable asciiTable, TableRowDTO tableRowDTO) {
        asciiTable.addRule();
        asciiTable.addRow(tableRowDTO.getCells());
    }
}

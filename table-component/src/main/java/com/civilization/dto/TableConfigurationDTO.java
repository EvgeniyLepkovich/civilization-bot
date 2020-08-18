package com.civilization.dto;

import de.vandermeer.asciitable.AT_ColumnWidthCalculator;
import de.vandermeer.asciithemes.TA_Grid;

public class TableConfigurationDTO {
    private AT_ColumnWidthCalculator widthCalculator;
    private TA_Grid grid;
    private int paddingLeft;
    private int paddingRight;

    public void setWidthCalculator(AT_ColumnWidthCalculator widthCalculator) {
        this.widthCalculator = widthCalculator;
    }

    public void setGrid(TA_Grid grid) {
        this.grid = grid;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public AT_ColumnWidthCalculator getWidthCalculator() {
        return widthCalculator;
    }

    public TA_Grid getGrid() {
        return grid;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }
}

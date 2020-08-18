package com.civilization.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableRowDTO {
    List<String> cells = new ArrayList<>();

    public void addCell(String value) {
        cells.add(value);
    }

    public void addCell(String... values) {
        cells.addAll(Arrays.asList(values));
    }

    public List<String> getCells() {
        return cells;
    }
}

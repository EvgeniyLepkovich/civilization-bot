package com.civilization.model;

import javax.persistence.*;

@Entity
public class TableConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_configuration_generator")
    @SequenceGenerator(name = "table_configuration_generator", sequenceName = "table_configuration_sequence")
    @Column(name = "table_configuration_id")
    private long id;

    private String ColumnRowTemplate;
    private String headerRowTemplate;
    private String userRowTemplate;

    @Enumerated(EnumType.STRING)
    private WidthCalculatorType widthCalculatorType;
    @Enumerated(EnumType.STRING)
    private GridType gridType;

    private int paddingLeft;
    private int paddingRight;

    @OneToOne(mappedBy = "tableConfiguration")
    private TableType tableType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeaderRowTemplate() {
        return headerRowTemplate;
    }

    public void setHeaderRowTemplate(String headerRowTemplate) {
        this.headerRowTemplate = headerRowTemplate;
    }

    public String getUserRowTemplate() {
        return userRowTemplate;
    }

    public void setUserRowTemplate(String userRowTemplate) {
        this.userRowTemplate = userRowTemplate;
    }

    public WidthCalculatorType getWidthCalculatorType() {
        return widthCalculatorType;
    }

    public void setWidthCalculatorType(WidthCalculatorType widthCalculatorType) {
        this.widthCalculatorType = widthCalculatorType;
    }

    public GridType getGridType() {
        return gridType;
    }

    public void setGridType(GridType gridType) {
        this.gridType = gridType;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public TableType getTableType() {
        return tableType;
    }

    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }

    public String getColumnRowTemplate() {
        return ColumnRowTemplate;
    }

    public void setColumnRowTemplate(String columnRowTemplate) {
        ColumnRowTemplate = columnRowTemplate;
    }
}

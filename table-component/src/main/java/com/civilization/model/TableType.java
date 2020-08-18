package com.civilization.model;

import javax.persistence.*;

@Entity
public class TableType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_type_generator")
    @SequenceGenerator(name = "table_type_generator", sequenceName = "table_type_sequence")
    @Column(name = "table_type_id")
    private long id;

    @Enumerated(EnumType.STRING)
    private TableTypeEnum type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "table_type_table_configuration",
            joinColumns = {
                @JoinColumn(name = "table_type_id", referencedColumnName = "table_type_id")
            },
            inverseJoinColumns = {
                @JoinColumn(name = "table_configuration_id", referencedColumnName = "table_configuration_id", unique = true)
            })
    private TableConfiguration tableConfiguration;

    public TableType() {
    }

    public TableType(TableTypeEnum type) {
        this.type = type;
    }

    public TableType(TableTypeEnum type, TableConfiguration tableConfiguration) {
        this.type = type;
        this.tableConfiguration = tableConfiguration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TableConfiguration getTableConfiguration() {
        return tableConfiguration;
    }

    public void setTableConfiguration(TableConfiguration tableConfiguration) {
        this.tableConfiguration = tableConfiguration;
    }
}

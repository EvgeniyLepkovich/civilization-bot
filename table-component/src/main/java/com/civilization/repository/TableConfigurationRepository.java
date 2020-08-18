package com.civilization.repository;

import com.civilization.model.TableConfiguration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableConfigurationRepository extends CrudRepository<TableConfiguration, Long> {

    @Query(value =
            "select * from table_configuration as tc " +
            "join table_type_table_configuration tttc on tttc.table_configuration_id = tc.table_configuration_id " +
            "join table_type as tt on tt.table_type_id = tttc.table_type_id " +
            "where tt.type = ?1"
            , nativeQuery = true)
    TableConfiguration getTableConfigurationBySchemeType(String tableType);
}

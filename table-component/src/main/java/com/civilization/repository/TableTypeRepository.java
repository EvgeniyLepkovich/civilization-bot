package com.civilization.repository;

import com.civilization.model.TableType;
import com.civilization.model.TableTypeEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableTypeRepository extends CrudRepository<TableType, Long> {
}

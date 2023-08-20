/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.sharedlib.mappers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import com.mokhtarabadi.sharedlib.dao.MessageDAO;

@Mapper
public interface DataMapper {

	@DaoFactory
	MessageDAO messageDAO(@DaoKeyspace CqlIdentifier keyspace, @DaoTable String table);
}

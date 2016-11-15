package org.dysd.dao.mybatis.mapper.call;

import org.apache.ibatis.annotations.Param;
import org.dysd.dao.call.ICallResult;
import org.springframework.stereotype.Repository;

@Repository
public interface ICallDao {

	public ICallResult call(@Param("input")String input);
}

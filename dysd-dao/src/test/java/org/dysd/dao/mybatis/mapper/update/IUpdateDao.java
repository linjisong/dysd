package org.dysd.dao.mybatis.mapper.update;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUpdateDao {
	
	public int insert(Map<String, Object> param);
	
	public Map<String, Object> select(@Param("paramCode")String paramCode);

	public int update(Map<String, Object> param);
	
	public int delete(@Param("paramCode")String paramCode);
}

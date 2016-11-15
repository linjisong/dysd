package org.dysd.dao.mybatis.mapper.batch;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.dysd.dao.annotation.BatchParam;
import org.dysd.dao.annotation.SqlRef;
import org.dysd.dao.annotation.SqlRefs;
import org.springframework.stereotype.Repository;

@Repository
public interface IBatchDao {

	public int[] batch1(@Param("paramCode")String paramCode, @BatchParam(item="item",index="index")List<Map<String, Object>> datas);
	
	public int delete(@Param("paramCode")String paramCode);
	
	public List<Map<String, Object>> selectList(@Param("paramCode")String paramCode);
	
	@SqlRefs({
		@SqlRef(value="batch2-first"),
		@SqlRef(value="batch2-second")
	})
	public int[] batch2(@Param("paramCode")String paramCode, @BatchParam(item="item",index="index")List<Map<String, Object>> datas);
	
	@SqlRefs({
		@SqlRef(value="delete"),
		@SqlRef(value="deleteDef")
	})
	public int[] batchDelete(@Param("paramCode")String paramCode);
}

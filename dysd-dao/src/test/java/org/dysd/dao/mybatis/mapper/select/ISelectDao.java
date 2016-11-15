package org.dysd.dao.mybatis.mapper.select;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.dysd.dao.annotation.FetchSize;
import org.dysd.dao.annotation.SqlRef;
import org.dysd.dao.page.IPage;
import org.dysd.dao.stream.IListStreamReader;
import org.springframework.stereotype.Repository;

@Repository
public interface ISelectDao {

	@SqlRef(value="selectList")
	public Map<String, Object> selectOne(@Param("paramCode")String paramCode);
	
	public List<Map<String, Object>> selectList();
	
	public List<Map<String, Object>> selectList(@Param("paramCode")String paramCode);
	
	public List<Map<String, Object>> selectList(IPage page);
	
	@FetchSize(5)
	@SqlRef(value="selectList")
	public IListStreamReader<Map<String, Object>> selectListStream();
}

package org.dysd.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.dysd.dao.annotation.BatchParam;
import org.dysd.dao.annotation.FetchSize;
import org.dysd.dao.annotation.OriginalUsage;
import org.dysd.dao.annotation.SqlRef;
import org.dysd.dao.annotation.SqlRefs;
import org.dysd.dao.page.IPage;
import org.dysd.dao.stream.IListStreamReader;
import org.springframework.stereotype.Repository;

@Repository
public interface IExampleDao {
	
	/**===========查询类示例===============**/
	public String selectString(@Param("paramName")String paramName);
	
	public String selectString2(@Param("paramCode")String paramCode);
	
	public List<String> selectStringList(@Param("paramGroup")String paramGroup);
	
	public String[] selectStringArray(@Param("paramGroup")String paramGroup);
	
	@OriginalUsage
	public int[] selectIntArray(@Param("paramGroup")String paramGroup);
	
	public ParamEnumDef selectOneJavaBean(@Param("paramCode")String paramCode);

	public Map<String, Object> selectOneMap(@Param("paramCode")String paramCode);
	
	public ParamEnumDef selectOneNestJavaBean(@Param("paramCode")String paramCode);
	
	public List<ParamEnumDef> selectListJavaBean(@Param("paramGroup")String paramGroup);
	
	public List<Map<String, Object>> selectListMap(@Param("paramGroup")String paramGroup);
	
	public List<ParamEnumDef> selectListNestJavaBean(@Param("paramGroup")String paramGroup);
	
	@MapKey("paramCode")
	@SqlRef("selectListNestJavaBean")//和selectListNestJavaBean方法使用相同的sql配置
	public Map<String, ParamEnumDef> selectMap(@Param("paramGroup")String paramGroup);
	
	@SqlRef("selectListJavaBean")//和selectListJavaBean方法使用相同的sql配置
	public List<ParamEnumDef> selectPageListJavaBean(@Param("paramGroup")String paramGroup, IPage page);
	
	@FetchSize(2)
	@SqlRef("selectListJavaBean")//和selectListJavaBean方法使用相同的sql配置
	public IListStreamReader<ParamEnumDef> selectListStream(@Param("paramGroup")String paramGroup);
	
	@SqlRef("selectListJavaBean")//和selectListJavaBean方法使用相同的sql配置
	public IListStreamReader<ParamEnumDef> selectListStream(@Param("paramGroup")String paramGroup, @FetchSize int fetchSize);
	
	/**===========修改类示例===============**/
	public int insert(ParamEnumDef paramEnumDef);
	
	public int update(ParamEnumDef paramEnumDef);
	
	public int delete(@Param("paramCode")String paramCode);
	
	public ParamEnumDef select(@Param("paramCode")String paramCode);
	
	/**===========批量类示例===============**/
	public List<ParamEnumData> selectParamEnumDatas(@Param("paramCode")String paramCode);
	
	public int deleteParamEnumDatas(@Param("paramCode")String paramCode);
	
	@SqlRef("batchSingleSql")
	public int[] batchSingleSql1(@BatchParam List<ParamEnumData> datas);
	
	@SqlRef("batchSingleSql")
	public int[] batchSingleSql2(@BatchParam(property="datas")ParamEnumDef paramEnumDef);
	
	public int[] batchSingleSql3(@Param("paramCode")String paramCode, @BatchParam(item="item",index="index")List<Map<String, Object>> datas);
	
	@SqlRefs({//多SQL单参数，但每个SQL执行时参数各不相同
		@SqlRef(value="batchMultiSql1"),
		@SqlRef(value="batchMultiSql2")
	})
	public int[] batchMultiSql(@Param("paramCode")String paramCode, @BatchParam(item="item",index="index")List<Object> datas);
	
	@SqlRefs({//多SQL单参数，且每个SQL执行时参数完全相同
		@SqlRef(value="deleteParamEnumDef"),
		@SqlRef(value="deleteParamEnumDatas")
	})
	public int[] batchMultiSqlButSingleParam(@Param("paramCode")String paramCode);
	
	@SqlRefs({//多SQL单参数，且每个SQL执行时参数完全相同，同时这个参数是一个数组类型
		@SqlRef(value="deleteMultiParamEnumDef"),
		@SqlRef(value="deleteMultiParamEnumDatas")
	})
	public int[] batchMultiSqlButSingleParam(@Param("paramCodes")String[] paramCodes);
	
	
}

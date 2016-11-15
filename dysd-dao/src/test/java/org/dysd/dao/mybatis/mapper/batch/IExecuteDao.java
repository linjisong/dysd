package org.dysd.dao.mybatis.mapper.batch;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.dysd.dao.annotation.BatchParam;
import org.dysd.dao.annotation.Execute;
import org.dysd.dao.annotation.Executes;
import org.dysd.dao.annotation.SqlRef;
import org.dysd.dao.annotation.SqlRefs;
import org.springframework.stereotype.Repository;

@Repository
public interface IExecuteDao {

	/**
	 * 首先整个参数封装成P{"role":role, "param2":permissions}的形式
	 * 1. 第一个SQL，是一个普通的执行，参数为P
	 * 2. 第二个SQL，是一个单SQL执行多次的批量，其中每次执行的批量参数为{"role":role, "index": index, "perm":permissions(index)}，这里的permissions为整个参数P的"param2"属性
	 */
	@Executes({
		@Execute(sqlRef=@SqlRef("dInsertRole"), param=@BatchParam(false)),
		@Execute(sqlRef=@SqlRef("dInsertRolePermission"), param=@BatchParam(item="perm", property="param2"))
	})
	public int[] dInsertRole(@Param("role")Role role, List<Permission> permissions);
	
	/**
	 * 首先整个参数封装成P{"role":role, "permission":permissions}的形式
	 * 1. 第一个SQL，是一个普通的执行，参数为P
	 * 2. 第二个SQL，也是一个普通的执行，但是参数为P的"role.roleId"属性，也就是Role对象的roleId属性值
	 * 2. 第三个SQL，是一个单SQL执行多次的批量，其中每次执行的批量参数为{"role":role, "index": index, "perm":permissions(index)}，这里的permissions为整个参数P的"permission"属性
	 */
	@Executes({
		@Execute(sqlRef=@SqlRef("dUpdateRole"), param=@BatchParam(false)),
		@Execute(sqlRef=@SqlRef("dDeleteRolePermission"), param=@BatchParam(value=false, property="role.roleId")),
		@Execute(sqlRef=@SqlRef("dInsertRolePermission"), param=@BatchParam(item="perm", property="permission"))
	})
	public int[] dUpdateRole(@Param("role")Role role, @Param("permission")List<Permission> permissions);
	
	/**
	 * 首先整个参数封装成P的形式（数组本身）
	 * 然后每一个SQL都是一个单SQL执行多次的批量，每一个批量的每一次执行的参数为数组对应索引处的值，类似{"index":index, roleId:roleIds[index]}
	 */
	@Executes({
		@Execute(sqlRef=@SqlRef("dDeleteRoleLimit"), param=@BatchParam(item = "roleId")),
		@Execute(sqlRef=@SqlRef("dDeleteRoleRoleAllot"), param=@BatchParam(item = "roleId")),
		@Execute(sqlRef=@SqlRef("dDeleteRolePermission"), param=@BatchParam(item = "roleId")),
		@Execute(sqlRef=@SqlRef("dDeleteUserRole"), param=@BatchParam(item = "roleId")),
		@Execute(sqlRef=@SqlRef("dDeleteRole"), param=@BatchParam(item = "roleId"))
	})
	public int[] dDeleteRole(List<Integer> roleIds);
	
	/**
	 * 可以实现上面方法类似的功能，但这是一个多SQL同参数的批量，每一个SQL的执行参数都是roleIds，因此在SqlMapper配置中需要使用<foreach>标签
	 */
	@SqlRefs({
		@SqlRef("dDeleteRoleLimit"),
		@SqlRef("dDeleteRoleRoleAllot"),
		@SqlRef("dDeleteRolePermission"), 
		@SqlRef("dDeleteUserRole"),
		@SqlRef("dDeleteRole")
	})
	public int[] dDeleteRoles(List<Integer> roleIds);
}

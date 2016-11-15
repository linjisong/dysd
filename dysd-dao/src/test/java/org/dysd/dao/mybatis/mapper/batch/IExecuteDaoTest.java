package org.dysd.dao.mybatis.mapper.batch;

import java.util.ArrayList;
import java.util.List;

import org.dysd.dao.SpringTestSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IExecuteDaoTest extends SpringTestSupport{

	@Autowired(required=false)
	private IExecuteDao dao;
	
	@Test
	public void testExecute() throws Exception {
		int roleId = 99;
		Role role = getRole(roleId);
		List<Permission> permissions = getPermissions(3);
		int[] rs = dao.dInsertRole(role, permissions);
		for(int r : rs){
			System.out.print(r+",");
		}
		System.out.println();
		
		
//		role.setRoleName("修改角色"+roleId);
//		role.setDes("测试修改"+roleId);
//		permissions = getPermissions(5);
//		rs = dao.dUpdateRole(role, permissions);
//		for(int r : rs){
//			System.out.print(r+",");
//		}
//		System.out.println();
//		
//		roleId = 100;
//		role = getRole(roleId);
//		permissions = getPermissions(4);
//		rs = dao.dInsertRole(role, permissions);
//		for(int r : rs){
//			System.out.print(r+",");
//		}
//		System.out.println();
//		
//		List<Integer> roleIds = new ArrayList<Integer>();
//		roleIds.add(99);
//		roleIds.add(100);
//		rs = dao.dDeleteRole(roleIds);
//		for(int r : rs){
//			System.out.print(r+",");
//		}
//		System.out.println();
	}

	private Role getRole(int roleId){
		Role role = new Role();
		role.setRoleId(roleId);
		role.setRoleName("随机角色"+roleId);
		role.setDes("测试"+roleId);
		return role;
	}
	
	private List<Permission> getPermissions(int count){
		List<Permission> permissions = new ArrayList<Permission>();
		for(int i = 1; i <= count; i++){
			Permission permission = new Permission();
			permission.setPermId(100 + i);
			permission.setPermType("MENU");
			permission.setPermTypeKey(""+(100+i));
			permission.setPermAttr("1");
			permissions.add(permission);
		}
		return permissions;
	}
}

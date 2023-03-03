package com.supermarket.back.repository;

import com.supermarket.back.entity.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuRepository extends JpaRepository<SysMenu,Integer> {
    @Query(value = "select * from `sys_menu` where parent_id = ?1 and mid in (select menu_id from `sys_menu_role` where role_id in ?2)",nativeQuery = true)
    List<SysMenu> getAllByParentIdAndRoleIn(Integer id, List<Integer> roleids);
}

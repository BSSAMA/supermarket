package com.supermarket.back.repository;

import com.supermarket.back.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleRepository extends JpaRepository<SysRole,Integer> {

    @Query(value = "select distinct name from sys_role",nativeQuery = true)
    List<String> getRoleList();

    List<SysRole> findByNameIn(List<String> rolename);

    @Query(value = "select rid from sys_role where name = ?1",nativeQuery = true)
    Integer findRidByName(String rolename);
}

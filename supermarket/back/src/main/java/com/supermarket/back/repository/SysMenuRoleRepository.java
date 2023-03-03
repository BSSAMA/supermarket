package com.supermarket.back.repository;

import com.supermarket.back.entity.IDClass.SysMenuRoleID;
import com.supermarket.back.entity.SysMenuRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuRoleRepository extends JpaRepository<SysMenuRole, SysMenuRoleID> {
    List<SysMenuRole> findAllByRoleIdIn(List<Integer> roleids);
}

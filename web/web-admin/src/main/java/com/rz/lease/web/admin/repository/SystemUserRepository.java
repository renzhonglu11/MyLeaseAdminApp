package com.rz.lease.web.admin.repository;

import com.rz.lease.model.entity.SystemUser;
import com.rz.lease.web.admin.vo.system.user.SystemUserItemVo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author rz
 * @description 针对表【system_user(员工信息表)】的数据库操作Mapper
 * @createDate 2023-07-24 15:48:00
 * @Entity com.atguigu.lease.model.SystemUser
 */
public interface SystemUserRepository extends BaseJpaRepository<SystemUser> {

    @Query(value = """
            select new com.rz.lease.web.admin.vo.system.user.SystemUserItemVo(
                          u.id,
                          u.username,
                          u.name,
                          u.type,
                          u.phone,
                          u.avatarUrl,
                          u.additionalInfo,
                          u.postId,
                          u.status,
                          p.name)
            from SystemUser u
            left join SystemPost p on p.id = u.postId
            where (u.isDeleted = 0 or u.isDeleted is null)
            	and (:name is null or :name = '' or lower(u.name) like lower(concat('%', :name, '%')))
            	and (:phone is null or :phone = '' or lower(u.phone) like lower(concat('%', :phone, '%')))
            """, countQuery = """
            select count(u)
            from SystemUser u
            where (u.isDeleted = 0 or u.isDeleted is null)
            	and (:name is null or :name = '' or lower(u.name) like lower(concat('%', :name, '%')))
            	and (:phone is null or :phone = '' or lower(u.phone) like lower(concat('%', :phone, '%')))
            """)
    Page<SystemUserItemVo> page(@Param("name") String name, @Param("phone") String phone, Pageable pageable);

    @Query("""
            select new com.rz.lease.web.admin.vo.system.user.SystemUserItemVo(
                    u.id,
                    u.username,
                    u.name,
                    u.type,
                    u.phone,
                    u.avatarUrl,
                    u.additionalInfo,
                    u.postId,
                    u.status,
                    p.name)
            from SystemUser u
            left join SystemPost p on p.id = u.postId
            where u.id = :id
             and (u.isDeleted = 0 or u.isDeleted is null)
            """)
    Optional<SystemUserItemVo> getItemById(@Param("id") Long id);

    @Query("""
            select (count(u) > 0)
            from SystemUser u
            where lower(u.username) = lower(:username)
              and (u.isDeleted = 0 or u.isDeleted is null)
            """)
    boolean existsByUsernameIgnoreCaseAndNotDeleted(@Param("username") String username);

}

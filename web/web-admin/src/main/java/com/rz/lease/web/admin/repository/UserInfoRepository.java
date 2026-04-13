package com.rz.lease.web.admin.repository;

import com.rz.lease.model.entity.UserInfo;
import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.web.admin.vo.user.UserInfoItemVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author rz
 * @description 针对表【user_info(用户信息表)】的数据库操作Mapper
 * @createDate 2023-07-24 15:48:00
 * @Entity com.atguigu.lease.model.UserInfo
 */
public interface UserInfoRepository extends BaseJpaRepository<UserInfo> {

    @Query("""
            select new com.rz.lease.web.admin.vo.user.UserInfoItemVo(
            		u.id, u.phone, u.avatarUrl, u.nickname, u.status
            )
            from UserInfo u
            where (:phone is null or :phone = '' or u.phone like concat('%', :phone, '%'))
            	and (:status is null or u.status = :status)
            order by u.id asc
            """)
    Page<UserInfoItemVo> pageItems(@Param("phone") String phone,
            @Param("status") BaseStatus status,
            Pageable pageable);

}

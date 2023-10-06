package com.ldx.blog.service.impl.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldx.blog.pojo.user.UserRole;
import com.ldx.blog.service.user.UserRoleService;
import com.ldx.blog.mapper.user.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author ldx
* @description 针对表【user_role】的数据库操作Service实现
* @createDate 2023-05-15 23:17:28
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}





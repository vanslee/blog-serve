package com.ldx.blog.service.impl.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldx.blog.pojo.user.Role;
import com.ldx.blog.service.user.RoleService;
import com.ldx.blog.mapper.user.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author ldx
* @description 针对表【role】的数据库操作Service实现
* @createDate 2023-05-15 23:17:28
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}





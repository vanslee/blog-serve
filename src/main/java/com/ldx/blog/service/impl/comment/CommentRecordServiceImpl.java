package com.ldx.blog.service.impl.comment;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldx.blog.pojo.comment.CommentRecord;
import com.ldx.blog.service.article.CommentRecordService;
import com.ldx.blog.mapper.comment.CommentRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author ldx
* @description 针对表【comment_record】的数据库操作Service实现
* @createDate 2023-05-15 23:17:28
*/
@Service
public class CommentRecordServiceImpl extends ServiceImpl<CommentRecordMapper, CommentRecord>
    implements CommentRecordService{

}





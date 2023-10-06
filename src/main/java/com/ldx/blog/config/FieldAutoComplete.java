package com.ldx.blog.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ldx.blog.pojo.article.Article;
import com.ldx.blog.pojo.category.Category;
import com.ldx.blog.pojo.comment.ArticleComment;
import com.ldx.blog.pojo.user.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Uaena
 * @date 2023/5/26 21:39
 */
@Component
@Slf4j
public class FieldAutoComplete implements MetaObjectHandler {
    @Value("${website.config.cdn}")
    private String CDN;

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert auto complete field ...");
        this.setFieldValByName("publishDate", System.currentTimeMillis() / 1000, metaObject);
        this.setFieldValByName("updateDate", System.currentTimeMillis() / 1000, metaObject);
        this.setFieldValByName("isDelete", false, metaObject);
        this.setFieldValByName("gender", false, metaObject);
        this.setFieldValByName("recentlyTime", System.currentTimeMillis() / 1000, metaObject);
        this.setFieldValByName("createTime", System.currentTimeMillis() / 1000, metaObject);
        this.setFieldValByName("updateTime", System.currentTimeMillis() / 1000, metaObject);
        if (metaObject.getOriginalObject() instanceof User) {
            this.setFieldValByName("avatarImgUrl", (((User) metaObject.getOriginalObject()).getAvatarImgUrl().replace(CDN, "")), metaObject);
        }
        if (metaObject.getOriginalObject() instanceof ArticleComment) {
            this.setFieldValByName("userAvatar", ((ArticleComment) metaObject.getOriginalObject()).getUserAvatar().replace(CDN, ""), metaObject);
        }
        if (metaObject.getOriginalObject() instanceof Article) {
            this.setFieldValByName("imgUrl", ((Article) metaObject.getOriginalObject()).getImgUrl().replace(CDN, ""), metaObject);
            this.setFieldValByName("mdUrl", ((Article) metaObject.getOriginalObject()).getMdUrl().replace(CDN, ""), metaObject);
        }
        if (metaObject.getOriginalObject() instanceof Category) {
            this.setFieldValByName("cover", ((Category) metaObject.getOriginalObject()).getCover().replace(CDN, ""), metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update auto complete field ...");
        this.setFieldValByName("updateDate", System.currentTimeMillis() / 1000, metaObject);
        this.setFieldValByName("recentlyTime", System.currentTimeMillis() / 1000, metaObject);
        this.setFieldValByName("updateTime", System.currentTimeMillis() / 1000, metaObject);
        if (metaObject.getOriginalObject() instanceof User) {
            String avatarImgUrl = ((User) metaObject.getOriginalObject()).getAvatarImgUrl();
            if (!Objects.isNull(avatarImgUrl)) {
                this.setFieldValByName("avatarImgUrl", avatarImgUrl.replace(CDN, ""), metaObject);
            }
        }
        if (metaObject.getOriginalObject() instanceof Article) {
            Article article = (Article) metaObject.getOriginalObject();
            if (!Objects.isNull(article.getImgUrl())) {
                this.setFieldValByName("imgUrl", article.getImgUrl().replace(CDN, ""), metaObject);
            }
        }
        if (metaObject.getOriginalObject() instanceof Category) {
            this.setFieldValByName("cover", ((Category) metaObject.getOriginalObject()).getCover().replace(CDN, ""), metaObject);
        }
    }
}

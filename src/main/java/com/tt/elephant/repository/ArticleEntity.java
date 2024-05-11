package com.tt.elephant.repository;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@JsonSerialize
@Entity
@Table(name = "article")
@Setter
@Getter
public class ArticleEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "org.hibernate.id.UUIDGenerator")
    private String articleId;
    private String title;
    private String content;
    private String author;
//    private String tag;
    private int type;
//    private String status;
    private long createTime;
    private long updateTime;
//    private String remark;
//    private String source;
//    private String sourceUrl;
//    private String sourceType;
//    private String imgUrl;
}

package com.tt.elephant.repository;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String id;
    private String title;
    private String content;
    private String author;
    private String tag;
    private String type;
    private String status;
    private String createTime;
    private String updateTime;
    private String remark;
    private String source;
    private String sourceUrl;
    private String sourceType;
    private String imgUrl;
}

package com.tsq.beans;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
public class Comment {
    @Id
    private String _id;
    private String articleId;
    private String content;
    private String userId;
    private String parentId;
    private Date createDate=new Date();
    private Integer thumbUp=0;

}

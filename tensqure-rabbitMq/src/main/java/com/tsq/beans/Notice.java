package com.tsq.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Notice {
    @Transient
    public static String ACTION_PUBLISH="publish";
    @Transient
    public static String TARGET_TYPE_ARTICLE="article";
    @Id
    @GeneratedValue(generator = "snowFlakeWorker")
    @GenericGenerator(name = "snowFlakeWorker", strategy = "com.tsq.utils.SnowFlakeWorker")
    private String id;
    private String receiverId;
    private String operatorId;
    @Transient
    private String operatorName;
    private String action;
    private String targetType;
    @Transient
    private String targetName;
    private String targetId;
    private String type;
    private Integer state=0;
    @Temporal(TemporalType.TIMESTAMP)
    //output time format
    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss"/*, timezone = "GMT+8"*/)
    private Date startTime=new Date();

}

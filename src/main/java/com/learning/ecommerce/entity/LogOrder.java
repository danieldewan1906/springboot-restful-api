package com.learning.ecommerce.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Data
public class LogOrder implements Serializable{
    
    @Id
    private String id;

    @JoinColumn
    @ManyToOne
    private Orders order;

    @JoinColumn
    @ManyToOne
    private Users user;

    private Integer logType;

    private String logMessage;

    @Temporal(TemporalType.DATE)
    private Date date;
}

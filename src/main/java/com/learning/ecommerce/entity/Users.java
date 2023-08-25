package com.learning.ecommerce.entity;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Users implements Serializable{
    
    @Id
    private String id;

    @JsonIgnore
    private String password;

    private String name;

    @JsonIgnore
    private String address;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String telp;

    @JsonIgnore
    private String roles;

    @JsonIgnore
    private Boolean isActive;

    
    public Users(String username){
        this.id = username;
    }
}

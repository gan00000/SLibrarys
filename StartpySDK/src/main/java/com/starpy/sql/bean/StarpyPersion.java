package com.starpy.sql.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;

/**
 * Entity mapped to table "StarpyPersion".
 */
@Entity
public class StarpyPersion {

    @Id(autoincrement = true)
    private Long id;

    @Unique
    private String name;
    @NotNull
    private String pwd;
    private Date date;
    @Generated(hash = 302423388)
    public StarpyPersion(Long id, String name, @NotNull String pwd, Date date) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.date = date;
    }
    @Generated(hash = 2025167300)
    public StarpyPersion() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPwd() {
        return this.pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }


}

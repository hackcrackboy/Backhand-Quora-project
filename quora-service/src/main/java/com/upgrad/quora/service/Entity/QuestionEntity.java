package com.upgrad.quora.service.Entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Entity
@Table(name = "question", schema = "public")
@NamedQueries(
        {@NamedQuery(name = "questionById", query = "select u from QuestionEntity u where u.id = :id"),
                @NamedQuery(name = "questionByUserId", query = "select u from QuestionEntity u where u.user_id = :user_id")
        }
)


public class QuestionEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;


    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user_id;


    @Column(name = "Content")
    @NotNull
    @Size(max = 500)
    private String content;

    @Column(name = "Date")
    @NotNull
    @Size(max = 30)
    private ZonedDateTime date;

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public UserEntity getUser() {
        return user_id;
    }

    public void setUser(UserEntity user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getDate() {
        return date;
    }


    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}






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
@Table(name = "answer", schema = "public")
@NamedQueries(
        {@NamedQuery(name = "answerById", query = "select u from AnsEntity u where u.ids = :id"),
                @NamedQuery(name = "answerByUserId", query = "select u from AnsEntity u where u.user_id = :user_id"),
                @NamedQuery(name = "answerByQuestionId", query = "select u from AnsEntity u where u.id = :question_id")
        }
)


public class AnsEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ids;

    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;


    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user_id;
    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private QuestionEntity id;

    @Column(name = "ANS")
    @NotNull
    @Size(max = 225)
    private String ans;

    @Column(name = "Date")
    @NotNull
    @Size(max = 30)
    private ZonedDateTime date;

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    public Integer getId() {
        return ids;
    }

    public void setId(Integer ids) {
        this.ids = ids;
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
    public QuestionEntity getQuestId() {
        return id;
    }

    public void setQuestId(QuestionEntity id) {
        this.id = id;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public ZonedDateTime getDate() {
        return date;
    }


    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}







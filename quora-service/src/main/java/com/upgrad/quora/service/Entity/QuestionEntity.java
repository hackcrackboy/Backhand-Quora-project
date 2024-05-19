package com.upgrad.quora.service.Entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Setter
@Getter
@Entity
@Table(name = "question", schema = "public")
@NamedQueries(
        {@NamedQuery(name = "questionById", query = "select u from QuestionEntity u where u.uuid = :uuid"),
                @NamedQuery(name = "questionByUserId", query = "select u from QuestionEntity u where u.user_id= :user_id")
        }
)


public class QuestionEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Setter
    @Getter
    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;



    @Setter
    @Getter
    @Column(name = "user_id")
    private Integer user_id;



    @Setter
    @Getter
    @Column(name = "Content")
    @NotNull
    @Size(max = 500)
    private String content;



    @Setter
    @Getter
    @Column(name = "Date")
@DateTimeFormat
    private ZonedDateTime date;

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }
}






package com.upgrad.quora.service.Entity;

import lombok.Getter;
import lombok.Setter;
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
        {@NamedQuery(name = "answerByUuid", query = "select u from AnsEntity u where u.uuid = :id"),
                @NamedQuery(name = "answerByUserId", query = "select u from AnsEntity u where u.user_id = :user_id"),
                @NamedQuery(name = "answerByQuestionId", query = "select u from AnsEntity u where u.id = :question_id")
        }
)


public class AnsEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ids;

    @Setter
    @Getter
    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;

    @Setter
    @Getter
    @JoinColumn(name = "USER_ID")
    private Integer user_id;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private QuestionEntity id;

    @Setter
    @Getter
    @Column(name = "ANS")
    @NotNull
    @Size(max = 225)
    private String ans;

    @Setter
    @Getter
    @Column(name = "Date")
    @NotNull

    private ZonedDateTime date;

}







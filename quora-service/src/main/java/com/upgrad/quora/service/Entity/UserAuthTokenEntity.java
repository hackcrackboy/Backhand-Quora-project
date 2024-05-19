package com.upgrad.quora.service.Entity;

import com.upgrad.quora.service.Entity.UserEntity;
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
import java.time.ZonedDateTime;

@Entity
@Table(name = "user_auth", schema = "public")
@NamedQueries({
        @NamedQuery(name = "userAuthTokenByAccessToken" , query = "select ut from UserAuthTokenEntity ut where ut.accessToken = :accessToken "),

})
public class UserAuthTokenEntity implements Serializable {


    @Setter
    @Getter
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user_id;

    @Setter
    @Getter
    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;
    @Setter
    @Getter
    @Column(name = "ACCESS_TOKEN")
    @NotNull
    @Size(max = 500)
    private String accessToken;

    @Setter
    @Getter
    @Column(name = "LOGIN_AT")
    @NotNull
    private ZonedDateTime loginAt;

    @Setter
    @Getter
    @Column(name = "EXPIRES_AT")
    @NotNull
    private ZonedDateTime expiresAt;

    @Setter
    @Getter
    @Column(name = "LOGOUT_AT")
    private ZonedDateTime logoutAt;


    public UserEntity getUser() {
        return user_id;
    }

    public void setUser(UserEntity user_id) {
        this.user_id = user_id;
    }




    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }


}




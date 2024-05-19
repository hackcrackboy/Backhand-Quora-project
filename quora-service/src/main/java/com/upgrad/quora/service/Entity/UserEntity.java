package com.upgrad.quora.service.Entity;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "Users", schema = "public")

@NamedQueries(
        {
                @NamedQuery(name = "userByUuid", query = "select u from UserEntity u where u.uuid = :uuid"),
                @NamedQuery(name = "userByEmail", query = "select u from UserEntity u where u.email =:email"),
                @NamedQuery(name = "userByUsername", query = "select u from UserEntity u where u.username =:username")
        })



public class UserEntity implements Serializable{



    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;
    @Column(name = "firstname")
    @NotNull
    @Size(max = 30)
    private String firstname;


    @JoinColumn(name = "role")
    @Size(max = 30)
    private String role;

    @Column(name = "email")
    @NotNull
    @Size(max = 50)
    private String email;

    //@ToStringExclude
    @Column(name = "Password")
    @Size(max = 255)
    private String password;



    @Column(name = "lastname")
    @NotNull
    @Size(max = 30)
    private String lastname;

    @Column(name = "contactnumber")
    @NotNull
    @Size(max = 30)
    private String contactNumber;

    @Column(name = "username")
    @NotNull
    @Size(max = 30)
    private String username;
    @Column(name = "COUNTRY")
    @NotNull
    @Size(max = 30)
    private String country;

    @Column(name = "aboutme")
    @NotNull
    @Size(max = 50)
    private String AboutMe;

    @Column(name = "dob")
    @NotNull
    @Size(max = 30)
    private String dob;

    @Column(name = "salt")
    @NotNull
    @Size(max = 200)
    //@ToStringExclude
    private String salt;

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }
    @Getter
    public Integer getId() {
        return id;
    }
    @Setter
    public void setId(Integer id) {
        this.id = id;
    }
    @Getter
    public String getUuid() {
        return uuid;
    }


    @Setter
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    @Getter
    public String getRole() {
        return role;
    }
    @Setter
    public void setRole(String role) {
        this.role = role;
    }
    @Getter
    public String getEmail() {
        return email;
    }
    @Setter
    public void setEmail(String email) {
        this.email = email;
    }
    @Getter
    public String getPassword() {
        return password;
    }
    @Setter
    public void setPassword(String password) {
        this.password = password;
    }
    @Getter
    public String getFirstName() {
        return firstname;
    }
    @Setter
    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }
    @Getter
    public String getUserName() {
        return username;
    }
    @Setter
    public void setUserName(String username) {
        this.username = username;
    }
    @Getter
    public String getCountry() {
        return country;
    }
    @Setter
    public void setCountry(String country) {
        this.country = country;
    }

    @Getter
    public String getAboutMe(){
        return AboutMe;
    }
    @Setter
    public void setAboutMe(String AboutMe) {
        this.AboutMe = AboutMe;
    }
    @Getter
    public String getDob() {
        return dob;
    }
    @Setter
    public void setDob(String dob) {
        this.dob = dob;
    }
    @Getter
    public String getLastName() {
        return lastname;
    }
    @Setter
    public void setLastName(String lastname) {
        this.lastname = lastname;
    }
    @Getter
    public String getContactNumber() {
        return this.contactNumber;
    }
    @Setter
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }





    @Getter
    public String getSalt() {
        return salt;
    }
    @Setter
    public void setSalt(String salt) {
        this.salt = salt;
    }


  


}



package com.upgrad.quora.service.Entity;

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
    @Column(name = "PASSWORD")
    @Size(max = 255)
    private String password;



    @Column(name = "LASTNAME")
    @NotNull
    @Size(max = 30)
    private String lastname;

    @Column(name = "ContactNumber")
    @NotNull
    @Size(max = 30)
    private String contactNumber;

    @Column(name = "USERNAME")
    @NotNull
    @Size(max = 30)
    private String username;
    @Column(name = "COUNTRY")
    @NotNull
    @Size(max = 30)
    private String country;

    @Column(name = "AboutMe")
    @NotNull
    @Size(max = 50)
    private String aboutme;

    @Column(name = "DOB")
    @NotNull
    @Size(max = 30)
    private String dob;

    @Column(name = "SALT")
    @NotNull
    @Size(max = 200)
    //@ToStringExclude
    private String salt;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public String getAboutMe() {
        return aboutme;
    }

    public void setAboutMe(String aboutme) {
        this.aboutme = aboutme;
    }public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }






    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }


}



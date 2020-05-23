package au.com.nab.users.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "user_table")
public class User {
    @Id
    private String fbId; // this is user's Facebook ID from FB JWT retrieved by FB authentication

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    @Enumerated
    private GenderEnum gender;

    @Column
    private Integer age;
}

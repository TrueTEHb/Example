package model;

import javax.persistence.*;

@Entity
@Table(name = "persons")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "money")
    private Long money;

    @Column(name = "role")
    private String role;

    public User() {

    }

    public User(long id, String name, String password, Long money, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.money = money;
        this.role = role;
    }

    public User(String name, String password, Long money, String role) {
        this.name = name;
        this.password = password;
        this.money = money;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }
}

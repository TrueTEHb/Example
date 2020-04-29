package myApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;

/*
@Entity
@Table(name = "roles")*/
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Role implements GrantedAuthority {

    /*@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)*/
    private Long id;

    /*@Column(name = "role")*/
    private String value;


    public Role() {
    }

    @Override
    public String getAuthority() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

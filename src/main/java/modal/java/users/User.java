package modal.java.users;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * Created by Eduan on 2015-01-16.
 */

@Entity
@Table
public class User {
    @Id
    @Size (max = 12)
    private String name;

    private String password;

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

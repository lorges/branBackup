package pl.com.peterpan.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity {

    private String name;

    @ManyToMany(mappedBy = "roleSet")
    private Set<User> userSet;
}

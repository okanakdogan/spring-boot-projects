package akdogan.usercheck.user;

import akdogan.usercheck.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
public class User extends BaseEntity{
    
    @Id @GeneratedValue()
    private Long id; 

    @Column(unique = true, nullable = false, length = 32)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

}

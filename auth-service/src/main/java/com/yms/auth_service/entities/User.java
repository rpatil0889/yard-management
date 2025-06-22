package com.yms.auth_service.entities;

import com.yms.auth_service.enums.UserStatus;
import com.yms.auth_service.utils.UserStatusConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * The Class User.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends CommonFields{

    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    private String password;

    @Convert(converter = UserStatusConverter.class)
    private UserStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> role;
}

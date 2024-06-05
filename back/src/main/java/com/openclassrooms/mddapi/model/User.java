package com.openclassrooms.mddapi.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "USERS")
@Data
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Size(max = 50)
    @Column(name = "username", unique = true)
    private String username;

    @NonNull
    @Size(max = 50)
    @Column(name = "email", unique = true)
    @Email
    private String email;


    @NonNull
    @Size(max = 120)
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Article> articles;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

package com.openclassrooms.mddapi.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TOPICS")
@Data
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Size(max = 50)
    private String title;

    @NonNull
    @Size(max = 2000)
    private String description;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Article> articles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "SUBSCRIPTIONS",
            joinColumns = @JoinColumn( name = "topic_id" ),
            inverseJoinColumns = @JoinColumn( name = "user_id" ) )
    private List<User> users;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

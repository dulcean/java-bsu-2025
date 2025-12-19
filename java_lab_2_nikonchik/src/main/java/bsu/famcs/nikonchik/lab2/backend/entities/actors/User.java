package bsu.famcs.nikonchik.lab2.backend.entities.actors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User extends Actor implements Comparable<User> {
    @ElementCollection
    @CollectionTable(
            name = "user_products",
            joinColumns = @JoinColumn(name = "owner_id")
    )
    @Column(name = "products_id")
    private List<UUID> productsOwned;

    protected User() {
        super();
        this.productsOwned = null;
    }

    public User(UUID actorId, String username,
                LocalDateTime createdDate, List<UUID> productsOwned) {
        super(actorId, username, createdDate, PermissionLevel.USER);
        this.productsOwned = Objects.requireNonNull(productsOwned,
                "List of products cannot be null");
    }

    @Override
    public int compareTo(User other) {
        return this.username.compareTo(other.username);
    }

    @Override
    public String toString() {
        return String.format("User{id='%s', username='%s', created=%s}",
                actorId, username, createdDate);
    }

    public List<UUID> getProductsOwned() { return productsOwned; }

    public void addProduct(UUID product) {
        productsOwned.add(
                Objects.requireNonNull(product, "Product cannot be null")
        );
    }
}



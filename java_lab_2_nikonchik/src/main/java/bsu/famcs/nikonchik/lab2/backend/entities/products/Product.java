package bsu.famcs.nikonchik.lab2.backend.entities.products;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "product_id", nullable = false, updatable = false)
    protected final UUID productId;

    @Column(name = "owner_id", nullable = false, updatable = false)
    protected final UUID ownerId;

    @Column(name = "created_date", nullable = false, updatable = false)
    protected final LocalDateTime createdDate;

    protected Product() {
        this.productId = null;
        this.ownerId = null;
        this.createdDate = null;
    }

    protected Product(UUID productId, UUID ownerId, LocalDateTime createdDate) {
        this.productId = Objects.requireNonNull(productId,
                "Product id cannot be null");
        this.ownerId = Objects.requireNonNull(ownerId,
                "Owner id cannot be null");
        this.createdDate = Objects.requireNonNull(createdDate,
                "Creation date cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product that = (Product) o;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    public UUID getProductId() { return productId; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public UUID getOwnerId() { return ownerId; }
}


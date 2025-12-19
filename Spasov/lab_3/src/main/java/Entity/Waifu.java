package waifu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "waifus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Waifu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String waifuName;

    @Column(columnDefinition = "TEXT") // Важно для хранения Base64
    private String imageUrl;
}
package com.example.cloudnative.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "Activity name is required")
    @Size(min = 2, max = 50, message = "Activity name must be between 2 and 50 characters")
    private String name;

    @Pattern(regexp = "^([0-9]{2}:[0-9]{2})$", message = "Duration must be in HH:mm format")
    private String duration;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    @Column(nullable = false)
    private boolean deleted = false;    // Logical deletion flag
}

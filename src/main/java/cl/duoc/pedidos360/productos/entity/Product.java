package cl.duoc.pedidos360.productos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA que mapea la tabla PRODUCTS en Oracle Autonomous DB.
 * Representa un producto del catalogo del sistema Pedidos360.
 */
@Entity
@Table(name = "PRODUCTS", schema = "ADMIN")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_seq")
    @SequenceGenerator(name = "products_seq", sequenceName = "PRODUCTS_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NotBlank
    @Size(max = 150)
    @Column(name = "NOMBRE", nullable = false, length = 150)
    private String nombre;

    @Size(max = 500)
    @Column(name = "DESCRIPCION", length = 500)
    private String descripcion;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "PRECIO", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @NotNull
    @Min(0)
    @Column(name = "STOCK", nullable = false)
    private Integer stock;

    @NotBlank
    @Size(max = 100)
    @Column(name = "CATEGORIA", nullable = false, length = 100)
    private String categoria;

    /**
     * Estado del producto: ACTIVO, INACTIVO, AGOTADO
     */
    @Column(name = "ESTADO", nullable = false, length = 20)
    @Builder.Default
    private String estado = "ACTIVO";

    @Column(name = "FECHA_CREACION", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;
}

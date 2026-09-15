package cl.duoc.pedidos360.productos.controller;

import cl.duoc.pedidos360.productos.dto.ProductDTO;
import cl.duoc.pedidos360.productos.entity.Product;
import cl.duoc.pedidos360.productos.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductController - CRUD de productos del catalogo Pedidos360.
 *
 * BASE PATH: /api/productos
 *
 * Todos los endpoints requieren JWT valido de Azure Entra ID.
 * Los endpoints de escritura (POST/PUT/DELETE) requieren SCOPE_Write.
 * Los endpoints de lectura (GET) requieren SCOPE_Read o APPROLE_Operador.
 */
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductRepository productRepository;

    /**
     * GET /api/productos
     * Lista todos los productos del catalogo.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('APPROLE_Operador') or hasAuthority('SCOPE_Read')")
    public ResponseEntity<List<Product>> getAllProducts(@AuthenticationPrincipal Jwt jwt) {
        log.info("[ProductController] GET /api/productos - Usuario: {}",
                jwt.getClaimAsString("preferred_username"));
        return ResponseEntity.ok(productRepository.findAll());
    }

    /**
     * GET /api/productos/{id}
     * Obtiene un producto por ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('APPROLE_Operador') or hasAuthority('SCOPE_Read')")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        log.info("[ProductController] GET /api/productos/{} - Usuario: {}", id,
                jwt.getClaimAsString("preferred_username"));
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/productos/disponibles
     * Lista productos activos con stock > 0.
     */
    @GetMapping("/disponibles")
    @PreAuthorize("hasAuthority('APPROLE_Operador') or hasAuthority('SCOPE_Read')")
    public ResponseEntity<List<Product>> getAvailableProducts() {
        return ResponseEntity.ok(productRepository.findAvailableProducts());
    }

    /**
     * GET /api/productos/categoria/{categoria}
     * Filtra productos por categoria.
     */
    @GetMapping("/categoria/{categoria}")
    @PreAuthorize("hasAuthority('APPROLE_Operador') or hasAuthority('SCOPE_Read')")
    public ResponseEntity<List<Product>> getByCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productRepository.findByCategoria(categoria));
    }

    /**
     * POST /api/productos
     * Crea un nuevo producto. Requiere scope Write.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_Write')")
    public ResponseEntity<Product> createProduct(
            @Valid @RequestBody ProductDTO dto,
            @AuthenticationPrincipal Jwt jwt) {
        log.info("[ProductController] POST /api/productos - Usuario: {}",
                jwt.getClaimAsString("preferred_username"));

        Product product = Product.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .stock(dto.getStock())
                .categoria(dto.getCategoria())
                .estado(dto.getEstado() != null ? dto.getEstado() : "ACTIVO")
                .build();

        Product saved = productRepository.save(product);
        log.info("[ProductController] Producto creado con ID: {}", saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * PUT /api/productos/{id}
     * Actualiza un producto existente. Requiere scope Write.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_Write')")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO dto,
            @AuthenticationPrincipal Jwt jwt) {
        log.info("[ProductController] PUT /api/productos/{} - Usuario: {}", id,
                jwt.getClaimAsString("preferred_username"));

        return productRepository.findById(id)
                .map(existing -> {
                    existing.setNombre(dto.getNombre());
                    existing.setDescripcion(dto.getDescripcion());
                    existing.setPrecio(dto.getPrecio());
                    existing.setStock(dto.getStock());
                    existing.setCategoria(dto.getCategoria());
                    if (dto.getEstado() != null) existing.setEstado(dto.getEstado());
                    return ResponseEntity.ok(productRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/productos/{id}
     * Elimina un producto. Requiere scope Write.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_Write')")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        log.info("[ProductController] DELETE /api/productos/{} - Usuario: {}", id,
                jwt.getClaimAsString("preferred_username"));

        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

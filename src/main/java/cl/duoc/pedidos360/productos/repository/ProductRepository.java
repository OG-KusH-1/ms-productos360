package cl.duoc.pedidos360.productos.repository;

import cl.duoc.pedidos360.productos.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio JPA para la entidad Product.
 * Spring Data genera la implementacion automaticamente.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /** Buscar por categoria */
    List<Product> findByCategoria(String categoria);

    /** Solo productos activos */
    List<Product> findByEstado(String estado);

    /** Buscar por nombre (case-insensitive) */
    List<Product> findByNombreContainingIgnoreCase(String nombre);

    /** Productos con stock disponible */
    @Query("SELECT p FROM Product p WHERE p.stock > 0 AND p.estado = 'ACTIVO'")
    List<Product> findAvailableProducts();

    /** Productos por categoria y estado */
    List<Product> findByCategoriaAndEstado(String categoria, String estado);
}

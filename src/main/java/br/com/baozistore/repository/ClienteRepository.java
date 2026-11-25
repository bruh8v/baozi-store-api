package br.com.baozistore.repository;

import br.com.baozistore.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de clientes. O JpaRepository ja fornece os metodos
 * save, findAll, findById, existsById e deleteById.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}

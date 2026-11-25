package br.com.baozistore.repository;

import br.com.baozistore.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de pedidos.
 * Os metodos existsBy... sao consultas derivadas do nome (Spring Data)
 * e sao usados para nao excluir clientes ou produtos que possuem pedidos.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    boolean existsByClienteId(Long clienteId);

    boolean existsByProdutoId(Long produtoId);
}

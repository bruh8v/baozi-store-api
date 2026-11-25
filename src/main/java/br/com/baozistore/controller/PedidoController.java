package br.com.baozistore.controller;

import br.com.baozistore.model.Pedido;
import br.com.baozistore.model.Produto;
import br.com.baozistore.repository.ClienteRepository;
import br.com.baozistore.repository.PedidoRepository;
import br.com.baozistore.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

/**
 * Endpoints REST da entidade Pedido.
 * URL base: /pedidos
 */
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(PedidoRepository pedidoRepository,
                            ClienteRepository clienteRepository,
                            ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    // POST /pedidos -> registra um novo pedido (201 Created ou 400 Bad Request)
    @PostMapping
    public ResponseEntity<Pedido> registrar(@Valid @RequestBody Pedido pedido) {
        validarClienteEProduto(pedido);
        pedido.setId(null);
        Pedido salvo = pedidoRepository.save(pedido);
        return ResponseEntity.created(URI.create("/pedidos/" + salvo.getId())).body(salvo);
    }

    // GET /pedidos -> lista todos os pedidos (200 OK)
    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    // GET /pedidos/{id} -> consulta um pedido pelo id (200 OK ou 404 Not Found)
    @GetMapping("/{id}")
    public Pedido buscarPorId(@PathVariable("id") Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido com id " + id + " não encontrado"));
    }

    // PUT /pedidos/{id} -> atualiza um pedido (opcional)
    @PutMapping("/{id}")
    public Pedido atualizar(@PathVariable("id") Long id, @Valid @RequestBody Pedido dados) {
        Pedido pedido = buscarPorId(id);
        validarClienteEProduto(dados);
        pedido.setClienteId(dados.getClienteId());
        pedido.setProdutoId(dados.getProdutoId());
        pedido.setQuantidade(dados.getQuantidade());
        return pedidoRepository.save(pedido);
    }

    // DELETE /pedidos/{id} -> exclui (cancela) um pedido (204 No Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Pedido com id " + id + " não encontrado");
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Regra de negocio: o pedido so pode ser feito por um cliente ja cadastrado,
     * para um produto ja cadastrado e que esteja disponivel em estoque.
     */
    private void validarClienteEProduto(Pedido pedido) {
        if (!clienteRepository.existsById(pedido.getClienteId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cliente com id " + pedido.getClienteId() + " não está cadastrado");
        }
        Produto produto = produtoRepository.findById(pedido.getProdutoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Produto com id " + pedido.getProdutoId() + " não está cadastrado"));
        if (!Boolean.TRUE.equals(produto.getEstoque())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O produto " + produto.getNome() + " está sem estoque");
        }
    }
}

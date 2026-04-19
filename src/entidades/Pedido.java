package entidades;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int numero;
    private int clienteId;
    private int empresaId;
    private String estado;
    private List<Produto> produtos;

    public Pedido() {
    }

    public Pedido(int numero, int clienteId, int empresaId) {
        this.numero = numero;
        this.clienteId = clienteId;
        this.empresaId = empresaId;
        this.estado = "aberto";
        this.produtos = new ArrayList<>();
    }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public int getEmpresaId() { return empresaId; }
    public void setEmpresaId(int empresaId) { this.empresaId = empresaId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }

    public void adicionarProduto(Produto p) {
        this.produtos.add(p);
    }

    public boolean removerProduto(String nomeProduto) {
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getNome().equals(nomeProduto)) {
                produtos.remove(i);
                return true;
            }
        }
        return false;
    }

    public double getValorTotal() {
        double total = 0;
        for (Produto p : produtos) {
            total += p.getValor();
        }
        return total;
    }
}
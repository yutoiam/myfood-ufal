package entidades;

import java.util.ArrayList;
import java.util.List;

public class Empresa {
    private int id;
    private int donoId;
    private String nome;
    private String tipoCozinha;
    private String endereco;
    private List<Produto> produtos;

    public Empresa() {
    }

    public Empresa(int id, int donoId, String nome, String tipoCozinha, String endereco) {
        this.id = id;
        this.donoId = donoId;
        this.nome = nome;
        this.tipoCozinha = tipoCozinha;
        this.endereco = endereco;
        this.produtos = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getDonoId() { return donoId; }
    public void setDonoId(int donoId) { this.donoId = donoId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTipoCozinha() { return tipoCozinha; }
    public void setTipoCozinha(String tipoCozinha) { this.tipoCozinha = tipoCozinha; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
}
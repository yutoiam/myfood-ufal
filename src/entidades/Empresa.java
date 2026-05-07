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
    private List<Integer> entregadores;
    private String abre;
    private String fecha;
    private String tipoMercado;
    private boolean aberto24Horas;
    private int numeroFuncionarios;
    private String tipoEmpresa;

    public Empresa() {
    }

    public Empresa(int id, int donoId, String nome, String tipoCozinha, String endereco) {
        this.id = id;
        this.donoId = donoId;
        this.nome = nome;
        this.tipoCozinha = tipoCozinha;
        this.endereco = endereco;
        this.produtos = new ArrayList<>();
        this.entregadores = new ArrayList<>();
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
    public List<Integer> getEntregadores() { return entregadores; }
    public void setEntregadores(List<Integer> entregadores) { this.entregadores = entregadores; }
    public String getAbre() { return abre; }
    public void setAbre(String abre) { this.abre = abre; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getTipoMercado() { return tipoMercado; }
    public void setTipoMercado(String tipoMercado) { this.tipoMercado = tipoMercado; }
    public boolean isAberto24Horas() { return aberto24Horas; }
    public void setAberto24Horas(boolean aberto24Horas) { this.aberto24Horas = aberto24Horas; }
    public int getNumeroFuncionarios() { return numeroFuncionarios; }
    public void setNumeroFuncionarios(int numeroFuncionarios) { this.numeroFuncionarios = numeroFuncionarios; }
    public String getTipoEmpresa() { return tipoEmpresa; }
    public void setTipoEmpresa(String tipoEmpresa) { this.tipoEmpresa = tipoEmpresa; }
}
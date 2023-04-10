package jp.martins;

public interface Conta {
    String getNumeroConta();
    String getNomeCorrentista();
    double getSaldo();
    Categoria getCategoria();
    boolean deposito(double valor);
    boolean retirada(double valor);
}

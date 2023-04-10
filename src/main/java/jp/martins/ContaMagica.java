package jp.martins;

public class ContaMagica implements Conta {
    private String numero;
    private String nomeCorrentista;
    private double saldo;
    private Categoria categoria;

    public ContaMagica(String numero, String nomeCorrentista) {
        this.numero = numero;
        this.nomeCorrentista = nomeCorrentista;
        this.saldo = 0.0;
        this.categoria = Categoria.SILVER;
        verificaNome(nomeCorrentista);
        verificaNroConta(numero);
    }

    @Override
    public String getNumeroConta() {
        return numero;
    }

    @Override
    public String getNomeCorrentista() {
        return nomeCorrentista;
    }

    @Override
    public double getSaldo() {
        return saldo;
    }

    @Override
    public Categoria getCategoria() {
        return categoria;
    }

    @Override
    public boolean deposito(double valor) {
        if (valor <= 0.0) {
            return false;
        }
        if (categoria == Categoria.SILVER && saldo < 50000) {
            saldo += valor;
            return true;
        }
        if (categoria == Categoria.SILVER && saldo >= 50000) {
            saldo += valor;
            categoria = Categoria.GOLD;
            return true;
        }
        if (categoria == Categoria.GOLD && saldo < 200000) {
            saldo += valor * 0.01;
            return true;
        }
        if (categoria == Categoria.GOLD && saldo >= 200000) {
            saldo += valor * 1.01;
            categoria = Categoria.PLATINUM;
            return true;
        }
        if (categoria == Categoria.PLATINUM) {
            saldo += valor * 1.02;
            return true;
        }
        return false;
    }

    @Override
    public boolean retirada(double valor) {
        if (valor <= 0.0) {
            return true;
        }
        if (categoria == Categoria.PLATINUM && saldo > 100000) {
            saldo -= valor;
        }
        if (categoria == Categoria.PLATINUM && saldo < 100000) {
            saldo -= valor;
            categoria = Categoria.GOLD;
        }
        if (categoria == Categoria.GOLD && saldo > 25000) {
            saldo -= valor * 0.025;
        }
        if (categoria == Categoria.GOLD && saldo <= 25000) {
            saldo += valor * 0.01;
            categoria = Categoria.SILVER;
        }
        if (categoria == Categoria.SILVER && saldo - valor > 0) {
            saldo -= valor;
        }
        return false;
    }

    @Override
    public String toString() {
        return "ContaMagica [categoria=" + categoria + ", nomeCorrentista=" + nomeCorrentista + ", numero=" + numero
                + ", saldo=" + saldo + "]";
    }

    private void verificaNroConta(String numero) {
        int posTraco = numero.indexOf('-');
        String nroStr = numero.substring(0, posTraco);
        int nroConta = Integer.parseInt(nroStr);
        int verificador = Integer.parseInt(numero.substring(posTraco + 1));

        if (nroConta < 99999 || nroConta > 999999) {
            throw new IllegalNumberException();
        }
        int soma = 0;
        for (int i = 0; i < nroStr.length(); i++) {
            soma += (Integer.parseInt(nroStr.charAt(i) + ""));
        }
        if (soma != verificador) {
            throw new IllegalNumberException();
        }
    }

    private void verificaNome(String nome) {
        if (nome.length() < 3) {
            throw new IllegalNameException();
        }
    }
}

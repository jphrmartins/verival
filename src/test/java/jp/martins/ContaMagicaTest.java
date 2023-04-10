package jp.martins;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

public class ContaMagicaTest {

    /**
     * - Criação de conta com Nome inválido.
     * -
     * - Criação de conta com o número inválido
     * - Criação de conta válida.
     * - Saque jamais pode deixar a conta negativa.
     * - Em conta Silver.
     * - Deposito de valor menor de 50.000 deve permanecer em estado Silver
     * - Quando saldo maior ou igual a 50.000 deve trocar para estado GOLD.
     * - Qualquer deposito deve render 0%.
     * - Saldos maiores que 200.000 devem trocar para estado Gold.
     * - Em conta Gold.
     * - Qualquer depoisto deve render 1%
     * - Saldo menor que 25.000 deve trocar estado para Silver
     * - Quando saldo maior ou igual a 200.000 deve trocar para estado Platinum
     * - Em conta Platinum.
     * - Qualque deposito deve render 2.5%.
     * - Quando saldo menor que 100.000 deve trocar para Silver.
     * - Quando Retirada que torne o saldo menor que 25.000 Ainda deve ser estado Silver.
     */

    private Conta conta;

    @BeforeEach
    public void setup() {
        conta = new ContaMagica("123456-21", "JpM");
    }

    @ParameterizedTest
    @MethodSource("argumetosParaCriacaoComFalha")
    public void deveCriarConta(String nome, String contaNumero, Class thorwType) {
        try {
            new ContaMagica(contaNumero, nome);
            fail("Deve retornar erro ao criar conta");
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), thorwType);
        }
    }

    public void criacaoDeContaDeveComecarZeradaENaCategoriaSilver() {
        Assertions.assertEquals(0, conta.getSaldo());
        Assertions.assertEquals(Categoria.SILVER, conta.getCategoria());
    }

    @Test
    public void contaSilverNaoDeveHaverRendimentoExtra() {
        conta.deposito(1000);
        Assertions.assertEquals(1000, conta.getSaldo());
        Assertions.assertEquals(Categoria.SILVER, conta.getCategoria());
    }

    @Test
    public void aoDepositar50000DeveFazerOUpgradeParaGoldEAplicar1pCentoDeRendimento() {
        conta.deposito(50000);
        Assertions.assertEquals(50500, conta.getSaldo());
        Assertions.assertEquals(Categoria.GOLD, conta.getCategoria());
    }

    @Test
    public void aoDepositar200000DeveFazerOUpgradeParaGoldAoInvesDePlatinumEAplicar1pCentoDeRendimento() {
        conta.deposito(200000);
        Assertions.assertEquals(202000, conta.getSaldo());
        Assertions.assertEquals(Categoria.GOLD, conta.getCategoria());
    }

    @Test
    public void emSilverDeveRetirarValor() {
        Assertions.assertTrue(conta.deposito(1000)); // Iniciando deposito
        Assertions.assertTrue(conta.retirada(500));
        Assertions.assertEquals(500, conta.getSaldo());
    }

    @Test
    public void emSilverPossivelARetiradaParaDeixarSaldo0() {
        Assertions.assertTrue(conta.deposito(1000)); // Iniciando deposito
        Assertions.assertTrue(conta.retirada(1000));
        Assertions.assertEquals(0, conta.getSaldo());
    }

    @Test
    public void saldoDaContaNuncaPodeSerNegativoEmContaSilver() {
        boolean result = conta.retirada(10);
        Assertions.assertFalse(result);
    }

    @Test
    public void emContaGoldSaldoMenorQue25000DeveOcorrerDowngradeParaSilver() {
        conta.deposito(50000);
        conta.retirada(500); // "ignorando" 1% de 50000 para execução correta dos testes.
        conta.retirada(25001);
        Assertions.assertEquals(24999, conta.getSaldo());
        Assertions.assertEquals(Categoria.SILVER, conta.getCategoria());
    }

    @Test
    public void emContaGoldDepositoDeveRender1pCento() {
        conta.deposito(50000);
        conta.retirada(500); // "ignorando" 1% de 50000 para execução correta dos testes.
        conta.deposito(1000);
        Assertions.assertEquals(51010, conta.getSaldo());
        Assertions.assertEquals(Categoria.GOLD, conta.getCategoria());
    }

    @Test
    public void emContaGoldSaldoDeveSerSemprePositivo() {
        conta.deposito(50000);
        conta.retirada(500); // "ignorando" 1% de 50000 para execução correta dos testes.
        Assertions.assertFalse(conta.retirada(50001));
    }

    @Test
    public void emContaGoldRetiradaDeTodoDinheiroDeveHaverDowngrade() {
        conta.deposito(50000);
        conta.retirada(500); // "ignorando" 1% de 50000 para execução correta dos testes.
        Assertions.assertTrue(conta.retirada(50000));
        Assertions.assertEquals(Categoria.SILVER, conta.getCategoria());
    }

    @Test
    public void emContaGoldQuandoSaldoMaiorIgual200000UpgradeParaPlatinumComRedimentoDe2eMeioPCento() {
        conta.deposito(50000);
        conta.retirada(500); // "ignorando" 1% de 50000 para execução correta dos testes.
        conta.deposito(150000);
        Assertions.assertTrue(conta.deposito(1000));
        Assertions.assertEquals(Categoria.PLATINUM, conta.getCategoria());
        Assertions.assertEquals(201025, conta.getSaldo());
    }

    @Test
    public void emContaPlatinumDepositoDeveRender2eMeioPCento() {
        conta.deposito(50000);
        conta.retirada(500); // "ignorando" 1% de 50.000 para execução correta dos testes.
        conta.deposito(150000);
        conta.retirada(3750); // "Ignorando 2.5% de 150.000 para execução correta dos testes
        Assertions.assertEquals(Categoria.PLATINUM, conta.getCategoria());

    }

    @Test
    public void valorDeSaldoEDepositoJamaisDeveSerNegativo() {
        conta.deposito(1000);
        int valor = -1;
        boolean resultadoDeposito = conta.deposito(valor);
        boolean resultadoSaque = conta.retirada(valor);
        Assertions.assertFalse(resultadoDeposito);
        Assertions.assertFalse(resultadoSaque);
    }


    private static Stream<Arguments> argumetosParaCriacaoComFalha() {
        return Stream.of(
                Arguments.of("NOME_VALIDO", "0", IllegalNumberException.class),
                Arguments.of("NOME_VALIDO", "99999", IllegalNumberException.class),
                Arguments.of("NOME_VALIDO", "999999-36", IllegalNumberException.class),
                Arguments.of("NOME_VALIDO", "1000000-01", IllegalNumberException.class),
                Arguments.of("NO", "123456-21", IllegalNameException.class)
        );
    }

    /*
    | Categoria | Saldo   | Deposito | Saque   |
|-----------|---------|----------|---------|
| SILVER    | 0       | 1.000    | 0       | V
| SILVER    | 0       | 50.000   | 0       | V
| SILVER    | 0       | 200.000  | 0       | V
| SILVER    | 0       | 0        | 10      | V
| SILVER    | 1000    | 0        | 500     | V
| SILVER    | 1000    | 0        | 1000    | V
| GOLD      | 50.000  | 0        | 25.001  | V
| GOLD      | 50.000  | 1000     | 0       | V
| GOLD      | 50.000  | 0        | 50.001  | V
| GOLD      | 50.000  | 0        | 50.000  | V
| GOLD      | 50.000  | 150000   | 0       | V
| PLATINUM  | 200.000 | 1000     | 0       |
| PLATINUM  | 200.000 | 0        | 100.001 |
| PLATINUM  | 200.000 | 0        | 200.001 |
| PLATINUM  | 200.000 | 0        | 175.001 |
| PLATINUM  | 200.000 | 0        | 200.000 |
     */

}

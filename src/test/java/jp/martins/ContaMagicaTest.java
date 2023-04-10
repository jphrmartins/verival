package jp.martins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("argumetosParaCriacaoComFalha")
    public void DeveCriarConta(String nome, String contaNumero, Class thorwType) {
        try {
            Conta conta = new ContaMagica(contaNumero, nome);
            fail("Deve retornar erro ao criar conta");
        } catch (Exception e) {
            
        }
    }


    private static Stream<Arguments> argumetosParaCriacaoComFalha() {
        return Stream.of(
                Arguments.of("NOME_VALIDO", "0", IllegalNumberException.class),
                Arguments.of("NOME_VALIDO", "99999", IllegalNumberException.class),
                Arguments.of("NOME_VALIDO", "99999-45", IllegalNumberException.class),
                Arguments.of("NOME_VALIDO", "999999-36", IllegalNumberException.class),
                Arguments.of("NOME_VALIDO", "999999 -54", IllegalNumberException.class),
                Arguments.of("NOME_VALIDO", "1000000-01", IllegalNumberException.class),
                Arguments.of("NOM", "123456-21", IllegalNameException.class)
        );
    }

}

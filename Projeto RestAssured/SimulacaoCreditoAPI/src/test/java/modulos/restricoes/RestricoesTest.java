package modulos.restricoes;

/**
 * Classe que agrega os casos de teste para avaliar o módulo de Restrições da API de Simulação de crédito.
 * Desse modo, ela contém todas as requições necessárias para os endpoints do módulo de Restrições.
 * */

import com.creditoAPI.baseAPI.BaseAPI;
import com.creditoAPI.baseSimulacao.BaseSimulacao;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import com.creditoAPI.support.RestricaoCPF;

@DisplayName("Testes do módulo de Restrições")
public class RestricoesTest extends BaseAPI {

    private String cpf_com_restricao;
    private String cpf_sem_restricao;

    @Test
    @DisplayName("Should be able to hit the health endpoint")
    void healthCheck() {
        when().
                get("/health").
                then().
                statusCode(HttpStatus.SC_OK).
                body("status", equalTo("UP"));
    }

    @Test
    @DisplayName("Consultar Restrição com CPF COM Restrição")
    public void testGetConsultarRestricaoCPFComRestricao(){

        this.cpf_com_restricao = new RestricaoCPF().getCpfComRestricao();

        given()
                .pathParam("cpf", this.cpf_com_restricao)
                .when()
                    .get("/restricoes/{cpf}")
                .then()
                    .assertThat()
                        .statusCode(HttpStatus.SC_OK)
                                .and()
                                        .body("mensagem", equalTo("O CPF " + this.cpf_com_restricao + " tem problema"));
    }

    @Test
    @DisplayName("Consultar Restrição com CPF SEM Restrição")
    public void testConsultarRestricaoCPFSemRestricao(){

        this.cpf_sem_restricao = new RestricaoCPF().getCpfSemRestricao();

        //Realizar consulta com CPF válido e com Restrição valiando a mensagem apresentada
        given()
                .pathParam("cpf", this.cpf_sem_restricao)
                .when()
                    .get("/restricoes/{cpf}")
                .then()
                    .assertThat()
                        .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}

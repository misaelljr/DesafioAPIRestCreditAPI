package modulos.restricoes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import com.creditoAPI.support.RestricaoCPF;

@DisplayName("Testes do módulo de Restrições")
public class RestricoesTest {

    private String cpf_com_restricao;
    private String cpf_sem_restricao;

    @BeforeAll
    public static void setup() {
        baseURI = "http://localhost";
        port = 8080;
        basePath = "/api/v1";
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
                        .statusCode(200)
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
                        .statusCode(204);
    }
}

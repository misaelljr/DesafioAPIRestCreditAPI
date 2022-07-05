package modulos.simulacoes;

import java.security.SecureRandom;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.creditoAPI.utils.GeraValidaCPF;
import com.creditoAPI.support.SimulacaoData;

import java.util.List;

@DisplayName("Testes do módulo de Simulações")
public class SimulacoesTest {
    SimulacaoData simulacaoDataAPI;
    SecureRandom random;
    GeraValidaCPF geradorCPF;
    JSONObject requestParams;

    @BeforeAll
    public static void setup() {
        baseURI = "http://localhost";
        port = 8080;
        basePath = "/api/v1";
    }

    @Test
    @DisplayName("Criar simulação COM SUCESSO")
    public void testPOSTCriarSimulacaoValida(){

        requestParams = new JSONObject();
        simulacaoDataAPI = new SimulacaoData();

        requestParams = simulacaoDataAPI.getValidoJSONRandomFromDataPOST();

                given()
                .contentType("application/json")
                .body(requestParams.toJSONString())
                .when()
                .post("/simulacoes/")
                .then()
                .assertThat()
                        .statusCode(201);
    }

    @Test
    @DisplayName("Criar simulação SEM SUCESSO")
    public void testPOSTCriarSimulacaoInvalida(){

        requestParams = new JSONObject();
        simulacaoDataAPI = new SimulacaoData();

        requestParams = simulacaoDataAPI.getInvalidoJSONRandomFromDataPOST();

        given()
                .contentType("application/json")
                .body(requestParams.toJSONString())
                .when()
                .post("/simulacoes")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DisplayName("Criar simulação com CPF DUPLICADO")
    public void testPOSTCriarSimulacaoCPFDuplicado(){

        requestParams = new JSONObject();
        simulacaoDataAPI = new SimulacaoData();
        random = new SecureRandom();

        List<String> getSetCPFCadastrado = simulacaoDataAPI.getSetCPFCadastrado();
        String cpfDuplicado = String.valueOf(getSetCPFCadastrado.get(random.nextInt(getSetCPFCadastrado.size())));

        requestParams = simulacaoDataAPI.getInvalidoCPFDuplicadoJSONRandomFromDataPOST(cpfDuplicado);

        given()
                .contentType("application/json")
                .body(requestParams.toJSONString())
                .when()
                .post("/simulacoes/")
                .then()
                .assertThat()
                .statusCode(409);
    }

    @Test
    @DisplayName("Atualizar Simulação COM SUCESSO")
    public void testPUTAtualizarSimulacaoValida(){

        requestParams = new JSONObject();
        simulacaoDataAPI = new SimulacaoData();
        random = new SecureRandom();

        List<String> getSetCPFCadastrado = simulacaoDataAPI.getSetCPFCadastrado();
        String cpfExistente = String.valueOf(getSetCPFCadastrado.get(random.nextInt(getSetCPFCadastrado.size())));

        requestParams = simulacaoDataAPI.getValidoAtualizarSimulacaoJSONRandomFromDataPOST(cpfExistente);

        given()
                .pathParam("cpf", cpfExistente)
                .contentType("application/json")
                .body(requestParams.toJSONString())
                .when()
                .put("/simulacoes/{cpf}")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @DisplayName("Atualizar Simulação com CPF INVÁLIDO")
    public void testPUTAtualizarSimulacaoCPFInvalido(){

        simulacaoDataAPI = new SimulacaoData();
        random = new SecureRandom();
        geradorCPF = new GeraValidaCPF();
        String cpfInvalido = geradorCPF.cpf(false);

        List<String> getSetCPFCadastrado = simulacaoDataAPI.getSetCPFCadastrado();

        if (getSetCPFCadastrado.contains(cpfInvalido)) {
            cpfInvalido = geradorCPF.cpf(false);
        }

        requestParams = simulacaoDataAPI.getValidoAtualizarSimulacaoJSONRandomFromDataPOST(cpfInvalido);

        given()
                .pathParam("cpf", cpfInvalido)
                .contentType("application/json")
                .body(requestParams.toJSONString())
                .when()
                .put("/simulacoes/{cpf}")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    @DisplayName("Retornar todas as simulações existentes COM itens cadastrados")
    public void testGetRetornarSimulacoesComItemCadastrado(){

        simulacaoDataAPI = new SimulacaoData();
        List<String[]> getSimulacoesEsperadas = simulacaoDataAPI.getSetSimulacoesCadastradas();

        List<String[]> getSimulacoesObtidas =
                given()
                .when()
                .get("/simulacoes")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response().jsonPath().getList("$");

        Assert.assertTrue(getSimulacoesEsperadas.containsAll(getSimulacoesObtidas));
    }

    @Test
    @DisplayName("Retornar simulação a partir de um CPF VÁLIDO")
    public void testGetRetornarSimulacaoPorCPFValido(){

        simulacaoDataAPI = new SimulacaoData();
        random = new SecureRandom();

        List<String> getSetCPFCadastrado = simulacaoDataAPI.getSetCPFCadastrado();
        String cpfValido = String.valueOf(getSetCPFCadastrado.get(random.nextInt(getSetCPFCadastrado.size())));

        given()
                .pathParam("cpf", cpfValido)
                .when()
                .get("/simulacoes/{cpf}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("cpf", equalTo(cpfValido));
    }

    @Test
    @DisplayName("Retornar simulação a partir de um CPF INVÁLIDO")
    public void testGetRetornarSimulacaoPorCPFInvalido(){

        simulacaoDataAPI = new SimulacaoData();
        random = new SecureRandom();
        geradorCPF = new GeraValidaCPF();
        String cpfInvalido = geradorCPF.cpf(false);

        List<String> getSetCPFCadastrado = simulacaoDataAPI.getSetCPFCadastrado();

        if (getSetCPFCadastrado.contains(cpfInvalido)) {
            cpfInvalido = geradorCPF.cpf(false);
        }

        given()
                .pathParam("cpf", cpfInvalido)
                .when()
                .get("/simulacoes/{cpf}")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    @DisplayName("Remover simulação a partir de um ID VÁLIDO")
    public void testDeleteRemoverSimulacaoPorIDValido(){

        simulacaoDataAPI = new SimulacaoData();
        random = new SecureRandom();
        List<Integer> getSetIDCadastrado = simulacaoDataAPI.getSetIDCadastrado();

        Integer idValido = getSetIDCadastrado.get(random.nextInt(getSetIDCadastrado.size()));

        given()
                .pathParam("id", idValido)
                .when()
                    .delete("/simulacoes/{id}")
                .then()
                    .assertThat()
                    .statusCode(204);
    }

    @Test
    @DisplayName("Remover simulação a partir de um ID INVÁLIDO")
    public void testDeleteRemoverSimulacaoPorIDInvalido(){

        simulacaoDataAPI = new SimulacaoData();
        random = new SecureRandom();
        List<Integer> getSetIDCadastrado = simulacaoDataAPI.getSetIDCadastrado();

        Integer idInvalido = random.nextInt(100);

        if (getSetIDCadastrado.contains(idInvalido)){
            idInvalido = random.nextInt(100);
        }

        given()
                .pathParam("id", idInvalido)
                .when()
                    .delete("/simulacoes/{id}")
                .then()
                .assertThat()
                .statusCode(404);
    }

}

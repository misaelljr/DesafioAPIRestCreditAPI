package modulos.simulacoes;

/**
 * Classe que agrega os casos de teste para avaliar o módulo de Simulações da API de Simulação de crédito.
 * Desse modo, ela contém todas as requições necessárias para os endpoints do módulo de simulações.
 */

import java.security.SecureRandom;

import com.creditoAPI.baseAPI.BaseAPI;
import com.creditoAPI.baseSimulacao.BaseSimulacao;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.creditoAPI.utils.GeraValidaCPF;
import com.creditoAPI.support.SimulacaoData;

import java.util.List;

@DisplayName("Testes do módulo de Simulações")
public class SimulacoesTest extends BaseAPI {
    SimulacaoData simulacaoDataAPI;
    SecureRandom random;
    GeraValidaCPF geradorCPF;
    JSONObject requestParams;
    BaseSimulacao baseSimulacao;

    @Test
    @DisplayName("Criar simulação COM SUCESSO")
    public void testPOSTCriarSimulacaoValida() {

        simulacaoDataAPI = new SimulacaoData();
        baseSimulacao = new BaseSimulacao();

        baseSimulacao = simulacaoDataAPI.getValidoJSONRandomFromDataPOST();

        given()
                .contentType("application/json")
                .body(baseSimulacao)
                .when()
                .post("/simulacoes/")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    @DisplayName("Criar simulação SEM SUCESSO")
    public void testPOSTCriarSimulacaoInvalida() {

        simulacaoDataAPI = new SimulacaoData();
        baseSimulacao = new BaseSimulacao();

        baseSimulacao = simulacaoDataAPI.getInvalidoJSONRandomFromDataPOST();

        given()
                .contentType("application/json")
                .body(baseSimulacao)
                .when()
                .post("/simulacoes")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Criar simulação com CPF DUPLICADO")
    public void testPOSTCriarSimulacaoCPFDuplicado() {

        simulacaoDataAPI = new SimulacaoData();
        random = new SecureRandom();
        baseSimulacao = new BaseSimulacao();

        List<String> getSetCPFCadastrado = simulacaoDataAPI.getSetCPFCadastrado();
        String cpfDuplicado = String.valueOf(getSetCPFCadastrado.get(random.nextInt(getSetCPFCadastrado.size())));

        baseSimulacao = simulacaoDataAPI.getInvalidoCPFDuplicado(cpfDuplicado);

        given()
                .contentType("application/json")
                .body(baseSimulacao)
                .when()
                .post("/simulacoes/")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    @DisplayName("Atualizar Simulação COM SUCESSO")
    public void testPUTAtualizarSimulacaoValida() {

        requestParams = new JSONObject();
        simulacaoDataAPI = new SimulacaoData();
        random = new SecureRandom();
        baseSimulacao = new BaseSimulacao();

        List<String> getSetCPFCadastrado = simulacaoDataAPI.getSetCPFCadastrado();
        String cpfExistente = String.valueOf(getSetCPFCadastrado.get(random.nextInt(getSetCPFCadastrado.size())));

        baseSimulacao = simulacaoDataAPI.getValidoAtualizarSimulacao(cpfExistente);

        given()
                .pathParam("cpf", cpfExistente)
                .contentType("application/json")
                .body(baseSimulacao)
                .when()
                .put("/simulacoes/{cpf}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Atualizar Simulação com CPF INVÁLIDO")
    public void testPUTAtualizarSimulacaoCPFInvalido() {

        simulacaoDataAPI = new SimulacaoData();
        random = new SecureRandom();
        geradorCPF = new GeraValidaCPF();
        String cpfInvalido = geradorCPF.cpf(false);
        baseSimulacao = new BaseSimulacao();

        List<String> getSetCPFCadastrado = simulacaoDataAPI.getSetCPFCadastrado();

        if (getSetCPFCadastrado.contains(cpfInvalido)) {
            cpfInvalido = geradorCPF.cpf(false);
        }

        baseSimulacao = simulacaoDataAPI.getValidoAtualizarSimulacao(cpfInvalido);

        given()
                .pathParam("cpf", cpfInvalido)
                .contentType("application/json")
                .body(baseSimulacao)
                .when()
                .put("/simulacoes/{cpf}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Retornar todas as simulações existentes COM itens cadastrados")
    public void testGetRetornarSimulacoesComItemCadastrado() {

        simulacaoDataAPI = new SimulacaoData();
        List<BaseSimulacao[]> getSimulacoesEsperadas = simulacaoDataAPI.getSetSimulacoesCadastradas();

        List<String[]> getSimulacoesObtidas =
                given()
                        .when()
                        .get("/simulacoes/")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract()
                        .response().jsonPath().getList("$");

        Assert.assertTrue(getSimulacoesEsperadas.containsAll(getSimulacoesObtidas));
    }

    @Test
    @DisplayName("Retornar simulação a partir de um CPF VÁLIDO")
    public void testGetRetornarSimulacaoPorCPFValido() {

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
                .statusCode(HttpStatus.SC_OK)
                .body("cpf", equalTo(cpfValido));
    }

    @Test
    @DisplayName("Retornar simulação a partir de um CPF INVÁLIDO")
    public void testGetRetornarSimulacaoPorCPFInvalido() {

        simulacaoDataAPI = new SimulacaoData();
        random = new SecureRandom();
        geradorCPF = new GeraValidaCPF();
        String cpfInvalido = geradorCPF.cpf(false);

        List<String> getSetCPFCadastrado = simulacaoDataAPI.getSetCPFCadastrado();

        //apenas para garantir que o CPF gerado não seja igual aos CPF já registrados
        if (getSetCPFCadastrado.contains(cpfInvalido)) {
            cpfInvalido = geradorCPF.cpf(false);
        }

        given()
                .pathParam("cpf", cpfInvalido)
                .when()
                .get("/simulacoes/{cpf}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Remover simulação a partir de um ID VÁLIDO")
    public void testDeleteRemoverSimulacaoPorIDValido() {

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
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Remover simulação a partir de um ID INVÁLIDO")
    public void testDeleteRemoverSimulacaoPorIDInvalido() {

        simulacaoDataAPI = new SimulacaoData();
        random = new SecureRandom();
        List<Integer> getSetIDCadastrado = simulacaoDataAPI.getSetIDCadastrado();

        Integer idInvalido = random.nextInt(100);

        //apenas para garantir que o ID inválido gerado não seja igual aos ID já registrados
        if (getSetIDCadastrado.contains(idInvalido)) {
            idInvalido = random.nextInt(100);
        }

        given()
                .pathParam("id", idInvalido)
                .when()
                .delete("/simulacoes/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}

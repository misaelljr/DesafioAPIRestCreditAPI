package com.creditoAPI.support;

/**
 * Classe responsável por criar simulações de dados para auxiliar na construção dos casos de teste do módulo de
 * Simulações. Por exemplo, métodos para retornar todos os CPFs ou IDs das simulações cadastradas na API.
 */

import com.creditoAPI.baseAPI.BaseAPI;
import com.creditoAPI.baseSimulacao.BaseSimulacao;
import com.creditoAPI.utils.GeraValidaCPF;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.*;

public class SimulacaoData extends BaseAPI {

    Faker faker;
    GeraValidaCPF geradorCPF;
    BaseSimulacao baseSimulacao;

    public List<BaseSimulacao[]> getSetSimulacoesCadastradas() {

        List<BaseSimulacao[]> getSetSimulacoesCadastradas =
                given()
                        .when()
                        .get("/simulacoes/")
                        .then()
                        .assertThat()
                        .extract()
                        .response().jsonPath().getList("$");

        return getSetSimulacoesCadastradas;
    }

    public List<String> getSetCPFCadastrado() {

        List<String> getSetCPFCadastrado =
                given()
                        .when()
                        .get("/simulacoes/")
                        .then()
                        .assertThat()
                        .extract()
                        .response().jsonPath().getList("cpf");


        return getSetCPFCadastrado;
    }

    public List<Integer> getSetIDCadastrado() {

        List<Integer> getSetIDCadastrado =
                given()
                        .when()
                        .get("/simulacoes/")
                        .then()
                        .assertThat()
                        .extract()
                        .response().jsonPath().getList("id");

        return getSetIDCadastrado;
    }

    public BaseSimulacao getValidoJSONRandomFromDataPOST() {

        faker = new Faker();
        geradorCPF = new GeraValidaCPF();
        baseSimulacao = new BaseSimulacao();

        baseSimulacao.setNome(faker.name().firstName());
        baseSimulacao.setCpf(geradorCPF.cpf(false));
        baseSimulacao.setEmail(faker.internet().emailAddress());
        baseSimulacao.setValor(new BigDecimal(faker.number().numberBetween(1000, 40000)));
        baseSimulacao.setParcelas(faker.number().numberBetween(2, 48));
        baseSimulacao.setSeguro(faker.bool().bool());

        return baseSimulacao;
    }

    public BaseSimulacao getInvalidoJSONRandomFromDataPOST() {

        faker = new Faker();
        geradorCPF = new GeraValidaCPF();
        baseSimulacao = new BaseSimulacao();

        baseSimulacao.setNome(StringUtils.EMPTY);
        baseSimulacao.setCpf(StringUtils.EMPTY);
        baseSimulacao.setEmail(faker.internet().emailAddress());
        baseSimulacao.setValor(new BigDecimal(faker.number().numberBetween(2, 500)));
        baseSimulacao.setParcelas(faker.number().numberBetween(0, 1));
        baseSimulacao.setSeguro(faker.bool().bool());

        return baseSimulacao;
    }

    public BaseSimulacao getInvalidoCPFDuplicado(String cpf) {

        faker = new Faker();
        geradorCPF = new GeraValidaCPF();
        baseSimulacao = new BaseSimulacao();

        baseSimulacao.setNome(faker.name().firstName());
        baseSimulacao.setCpf(cpf);
        baseSimulacao.setEmail(faker.internet().emailAddress());
        baseSimulacao.setValor(new BigDecimal(faker.number().numberBetween(1000, 40000)));
        baseSimulacao.setParcelas(faker.number().numberBetween(2, 48));
        baseSimulacao.setSeguro(faker.bool().bool());

        return baseSimulacao;
    }

    public BaseSimulacao getValidoAtualizarSimulacao(String cpf) {

        faker = new Faker();
        geradorCPF = new GeraValidaCPF();
        baseSimulacao = new BaseSimulacao();

        baseSimulacao.setNome(faker.name().firstName());
        baseSimulacao.setCpf(cpf);
        baseSimulacao.setEmail(faker.internet().emailAddress());
        baseSimulacao.setValor(new BigDecimal(faker.number().numberBetween(1000, 40000)));
        baseSimulacao.setParcelas(faker.number().numberBetween(2, 48));
        baseSimulacao.setSeguro(faker.bool().bool());

        return baseSimulacao;
    }
}

package com.example.mini.autorizador;

import com.example.mini.autorizador.dto.CartaoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(classes = MiniAutorizadorApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartaoControllerTests {

	private CartaoDTO cartao = CartaoDTO.builder().senha("1234").numeroCartao("6549873025634501").saldo(500.0).build();

	@Autowired protected MockMvc mockMvc;

	@Autowired protected ObjectMapper objectMapper;

	@BeforeAll
	void cadastrarCartaoTest() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
						.contentType("application/json")
						.content(this.objectMapper.writeValueAsString(cartao)))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andReturn();

		String content = mvcResult.getResponse().getContentAsString();

		Assertions.assertNotNull(content);

		Assertions.assertEquals(cartao.getSenha(), new JSONObject(content).getString("senha"));
		Assertions.assertEquals(cartao.getNumeroCartao(), new JSONObject(content).getString("numeroCartao"));
	}

	@Test
	void cadastrarCartaoCadastradoTest() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
						.contentType("application/json")
						.content(this.objectMapper.writeValueAsString(cartao)))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andReturn();

		String content = mvcResult.getResponse().getContentAsString();

		Assertions.assertNotNull(content);

		Assertions.assertEquals(cartao.getSenha(), new JSONObject(content).getString("senha"));
		Assertions.assertEquals(cartao.getNumeroCartao(), new JSONObject(content).getString("numeroCartao"));
	}

	@Test
	void consultarSaldoCartaoValidoTest() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/cartoes/{numeroCartao}", this.cartao.getNumeroCartao()).contentType("application/json"))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String content = mvcResult.getResponse().getContentAsString();

		Assertions.assertNotNull(content);
	}

	@Test
	void consultarSaldoCartaoInvalidoTest() throws Exception {
		this.cartao.setNumeroCartao("6549873025634500");
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cartoes/{numeroCartao}", this.cartao.getNumeroCartao()).contentType("application/json"))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andReturn();
	}

	@Test
	void debitarSaldoCartaoValidoTest() throws Exception {

		this.cartao.setSaldo(100.0);

		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/transacoes")
						.contentType("application/json")
						.content(this.objectMapper.writeValueAsString(this.cartao)))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andReturn();

		String content = mvcResult.getResponse().getContentAsString();

		Assertions.assertNotNull(content);
	}

	@Test
	void debitarSaldoCartaoInvalidoTest() throws Exception {

		this.cartao.setNumeroCartao("6549873025634500");

		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/transacoes")
						.contentType("application/json")
						.content(this.objectMapper.writeValueAsString(this.cartao)))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andReturn();

		String content = mvcResult.getResponse().getContentAsString();

		Assertions.assertNotNull(content);
	}

	@Test
	void debitarSaldoCartaoSenhaInvalidaTest() throws Exception {

		this.cartao.setSenha("12345");

		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/transacoes")
						.contentType("application/json")
						.content(this.objectMapper.writeValueAsString(this.cartao)))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andReturn();

		String content = mvcResult.getResponse().getContentAsString();

		Assertions.assertNotNull(content);
	}

	@Test
	void debitarSaldoCartaoSaldoInvalidaTest() throws Exception {

		this.cartao.setSaldo(600.0);

		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/transacoes")
						.contentType("application/json")
						.content(this.objectMapper.writeValueAsString(this.cartao)))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andReturn();

		String content = mvcResult.getResponse().getContentAsString();

		Assertions.assertNotNull(content);
	}

	@BeforeEach
	void defaultData() {
		this.cartao = CartaoDTO.builder().senha("1234").numeroCartao("6549873025634501").saldo(500.0).build();
	}
}

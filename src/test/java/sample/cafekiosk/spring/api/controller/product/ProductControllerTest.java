package sample.cafekiosk.spring.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.ControllerTestSupport;
import sample.cafekiosk.spring.api.controller.order.OrderController;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;

//@WebMvcTest(controllers = {
//        ProductController.class
//})
public class ProductControllerTest extends ControllerTestSupport {

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        // when // then
        mockMvc.perform(
            post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())       // 자세한 로그 볼때 쓰는 함수. 꼭 안써도 되긴함
            .andExpect(status().isOk());
    }

    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
    @Test
    void createProductWithoutType() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/products/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())       // 자세한 로그 볼때 쓰는 함수. 꼭 안써도 되긴함
            .andExpect(status().isBadRequest())     // 예외가 발생했을 때 status가 400인지 확인
            .andExpect(jsonPath("$.code").value("400"))    // 예외가 발생했을 때 code가 400인지 확인
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))    // 예외가 발생했을 때 code가 400인지 확인
            .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 상품을 등록할 때 상품 판매상태는 필수값이다.")
    @Test
    void createProductWithoutSellingStatus() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .name("아메리카노")
                .price(4000)
                .build();

        // when // then
        mockMvc.perform(
                    post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())       // 자세한 로그 볼때 쓰는 함수. 꼭 안써도 되긴함
                .andExpect(status().isBadRequest())     // 예외가 발생했을 때 status가 400인지 확인
                .andExpect(jsonPath("$.code").value("400"))    // 예외가 발생했을 때 code가 400인지 확인
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))    // 예외가 발생했을 때 code가 400인지 확인
                .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 상품을 등록할 때 상품 이름은 필수값이다.")
    @Test
    void createProductWithoutName() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .price(4000)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())       // 자세한 로그 볼때 쓰는 함수. 꼭 안써도 되긴함
                .andExpect(status().isBadRequest())     // 예외가 발생했을 때 status가 400인지 확인
                .andExpect(jsonPath("$.code").value("400"))    // 예외가 발생했을 때 code가 400인지 확인
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))    // 예외가 발생했을 때 code가 400인지 확인
                .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 상품을 등록할 때 상품 가격은 양수이다.")
    @Test
    void createProductWithZeroPrice() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(0)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())       // 자세한 로그 볼때 쓰는 함수. 꼭 안써도 되긴함
                .andExpect(status().isBadRequest())     // 예외가 발생했을 때 status가 400인지 확인
                .andExpect(jsonPath("$.code").value("400"))    // 예외가 발생했을 때 code가 400인지 확인
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))    // 예외가 발생했을 때 code가 400인지 확인
                .andExpect(jsonPath("$.message").value("상품 가격은 0보다 커야합니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 상품을 등록할 때 상품 가격은 양수이다.")
    @Test
    void createProductWithNegativePrice() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(-1000)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())       // 자세한 로그 볼때 쓰는 함수. 꼭 안써도 되긴함
                .andExpect(status().isBadRequest())     // 예외가 발생했을 때 status가 400인지 확인
                .andExpect(jsonPath("$.code").value("400"))    // 예외가 발생했을 때 code가 400인지 확인
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))    // 예외가 발생했을 때 code가 400인지 확인
                .andExpect(jsonPath("$.message").value("상품 가격은 0보다 커야합니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("판매 상품을 조회한다.")
    @Test
    void getSellingProducts() throws Exception {
        // given
        List<ProductResponse> result = List.of();
        when(productService.getSellingProducts()).thenReturn(result);

        // when // then
        mockMvc.perform(
                        get("/api/v1/products/selling")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray());
    }

}

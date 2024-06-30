package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;


import java.util.List;
import java.util.stream.Collectors;

/**
 * Transactional에 대한 설명
 * readOnly = true : 읽기 전용으로 한다.
 * 이 것을 하면 읽기전용으로 트랜잭션을 만들게 된다. 이로써 데이터를 조회하는 SELECT 쿼리(Read)만을 실행할 수 있고, CUD작업은 허용되지 않는다.
 * 이로써 JPA에서 성능 이점이 생기게 된다.
 * JPA에서 CUD를 하도록 하면 스냅샷 저장과 변경감지등의 커밋을 하게되는데, readOnly = true를 하면 이것을 하지 않아 성능이 좋아진다.
 * CQRS 패턴에서 사용한다.
 * CQRS는 Command Query Responsibility Segregation의 약자로, 명령과 조회를 분리한다는 의미이다.
 * CQRS 패턴은 명령을 처리하는 부분(Command)과 조회를 처리하는 부분(Query)을 분리함으로써 서비스의 성능을 향상시킬 수 있다.
 * 서비스를 할 때 Command보다 Query가 훨씬 많기 떄문에, Query를 더 빠르게 처리하기 위해 사용한다.
 *
 * 결론 : readOnly = true를 사용하면 쿼리용 서비스와 커맨드용 서비스를 분리할 수 있어 성능향상에 도움을 줄 수 있다.
 * ps : readOnly = false가 default 값이다.
 * ps2 : 클래스에 @Transactional(readOnly = true)를 걸어버리면 클래스 내의 모든 메서드가 readOnly = true가 된다.
 *      커맨드 작업이 필요한 메서드는 @Transactional을 붙여서 readOnly = false로 해줘야 한다.
 *      이렇게 했을 때, 일단 모든 메서드에서 ReadOnly가 적용되어 성능이 향상되고, 커맨드 작업이 필요한 메서드에서는 readOnly = false를 붙여
 *      커맨드 작업을 함으로써, ReadOnly가 적용되지 않아 성능이 떨어지는 것을 방지할 수 있다.
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional  // 이 메서드는 커맨드 작업을 수행하기 때문에 readOnly = false를 해준다.
    public ProductResponse createProduct(ProductCreateServiceRequest request) {
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if(latestProductNumber==null) {
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        return String.format("%03d", nextProductNumberInt);
    }

}

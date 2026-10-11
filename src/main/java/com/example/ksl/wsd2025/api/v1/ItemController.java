
package com.example.ksl.wsd2025.api.v1;

import com.example.ksl.wsd2025.api.common.ApiResponse;
import com.example.ksl.wsd2025.api.dto.ItemDto;
import com.example.ksl.wsd2025.api.request.ItemCreateRequest;
import com.example.ksl.wsd2025.api.request.ItemUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final Map<Long, ItemDto> store = new HashMap<>();
    private long sequence = 1L;

    // 1. GET: 전체 상품 조회 (200)
    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemDto>>> getItems() {
        return ResponseEntity.ok(
                ApiResponse.success("전체 조회 성공", new ArrayList<>(store.values()))
        );
    }

    // 2. GET: 상품 단건 조회 (200, 404)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemDto>> getItem(@PathVariable Long id) {
        ItemDto item = store.get(id);

        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("상품을 찾을 수 없습니다."));
        }

        return ResponseEntity.ok(ApiResponse.success("조회 성공", item));
    }

    // 3. POST: 상품 생성 (201, 400)
    @PostMapping
    public ResponseEntity<ApiResponse<ItemDto>> createItem(
            @RequestBody ItemCreateRequest request) {

        if (request.getName() == null || request.getName().isBlank()
                || request.getPrice() == null || request.getPrice() < 0) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("상품명과 올바른 가격을 입력해야 합니다."));
        }

        ItemDto item = new ItemDto();
        item.setId(sequence++);
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        store.put(item.getId(), item);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("상품 생성 성공", item));
    }

    // 4. POST: 여러 상품 생성 (201)
    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<List<ItemDto>>> createItems(
            @RequestBody List<ItemCreateRequest> requests) {

        List<ItemDto> createdItems = new ArrayList<>();

        for (ItemCreateRequest request : requests) {
            if (request.getName() == null || request.getName().isBlank()
                    || request.getPrice() == null || request.getPrice() < 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("상품명과 올바른 가격을 입력해야 합니다."));
            }
        }

        for (ItemCreateRequest request : requests) {
            ItemDto item = new ItemDto();
            item.setId(sequence++);
            item.setName(request.getName());
            item.setPrice(request.getPrice());
            store.put(item.getId(), item);
            createdItems.add(item);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("여러 상품 생성 성공", createdItems));
    }

    // 5. PUT: 상품 정보 수정 (200, 404)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemDto>> updateItem(
            @PathVariable Long id,
            @RequestBody ItemUpdateRequest request) {

        ItemDto item = store.get(id);

        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("수정할 상품을 찾을 수 없습니다."));
        }

        if (request.getName() != null) {
            if (request.getName().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("상품명은 비어 있을 수 없습니다."));
            }
            item.setName(request.getName());
        }

        if (request.getPrice() != null) {
            if (request.getPrice() < 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("가격은 0 이상이어야 합니다."));
            }
            item.setPrice(request.getPrice());
        }

        return ResponseEntity.ok(ApiResponse.success("상품 수정 성공", item));
    }

    // 6. PUT: 상품명 변경 (200, 404)
    @PutMapping("/{id}/name")
    public ResponseEntity<ApiResponse<ItemDto>> updateItemName(
            @PathVariable Long id,
            @RequestBody ItemUpdateRequest request) {

        ItemDto item = store.get(id);

        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("상품을 찾을 수 없습니다."));
        }

        if (request.getName() == null || request.getName().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("변경할 상품명을 입력해야 합니다."));
        }

        item.setName(request.getName());

        return ResponseEntity.ok(ApiResponse.success("상품명 변경 성공", item));
    }

    // 7. DELETE: 상품 삭제 (200, 404)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteItem(@PathVariable Long id) {
        ItemDto removed = store.remove(id);

        if (removed == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("삭제할 상품을 찾을 수 없습니다."));
        }

        return ResponseEntity.ok(ApiResponse.success("상품 삭제 성공", id));
    }

    // 8. DELETE: 전체 상품 삭제 (200)
    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse<Integer>> deleteAllItems() {
        int count = store.size();
        store.clear();

        return ResponseEntity.ok(
                ApiResponse.success(count + "개 상품 삭제 성공", count)
        );
    }

    // 9. GET: 서버 오류 응답 테스트 (500)
    @GetMapping("/test/server-error")
    public ResponseEntity<ApiResponse<Void>> testServerError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("서버 내부 오류 테스트"));
    }

    // 10. GET: 서비스 이용 불가 응답 테스트 (503)
    @GetMapping("/test/service-unavailable")
    public ResponseEntity<ApiResponse<Void>> testServiceUnavailable() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.error("서비스 이용 불가 테스트"));
    }
}

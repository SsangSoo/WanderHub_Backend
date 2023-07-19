package wanderhub.server.global.response;

import lombok.*;

import java.util.List;

@Getter
public class PageResponseDto<T> {
    private final List<T> data;
    private final long totalPage;       // 전체 페이지
    private final long totalElements;   // 전체 요소
    private final long currentElements; // 현재 페이지 내 요소 갯수
    private final long currentPage;     // 현재 페이지
    private final boolean first;        // 첫 번째 페이지 여부
    private final boolean last;         // 마지막 페이지 여부

    private PageResponseDto(List<T> data, long totalPage, long totalElements, long currentElements, long currentPage) {
        this.data = data;
        this.totalPage = totalPage;
        this.totalElements = totalElements;
        this.currentElements = currentElements;
        this.currentPage = currentPage;
        this.first = currentPage==1;
        this.last = currentPage==totalPage;
    }

    public static PageResponseDto of(List data, long totalPage, long totalElements, long currentElements, long currentPage) {
        return new PageResponseDto<>(data, totalPage, totalElements, currentElements, currentPage);
    }
}
package wanderhub.server.global.response;

import lombok.Getter;

import java.util.List;

@Getter
public class PageTempResponseDto<T> {
    List<T> data;
    PageInfo pageInfo;

    public PageTempResponseDto(List<T> data, PageInfo pageInfo) {
        this.data = data;
        this.pageInfo = pageInfo;
    }
}

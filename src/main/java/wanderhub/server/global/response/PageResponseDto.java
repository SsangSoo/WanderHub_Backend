package wanderhub.server.global.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto<T> {
    List<T> data;
    PageInfo pageInfo;
}

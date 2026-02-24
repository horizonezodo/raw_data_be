package ngvgroup.com.bpmn.utils;

import ngvgroup.com.bpmn.dto.PageDTO.PageableDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class PageUtils {
    public static Pageable toPageable(PageableDTO request) {
        PageableDTO pageableRequest = Optional.ofNullable(request)
                .orElse(new PageableDTO());
        return PageRequest.of(
                pageableRequest.getPage(),
                pageableRequest.getSize(),
                Sort.by(Sort.Direction.fromString(pageableRequest.getSortDirection()),
                        pageableRequest.getSortField())
        );
    }
}

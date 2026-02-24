package com.ngv.zns_service.dto.response.goiDv;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ZSSGoiDvuDtos {
    private String maDvi;
    private List<ZSSGoiDvuDto> goiDvuDtoList;
}

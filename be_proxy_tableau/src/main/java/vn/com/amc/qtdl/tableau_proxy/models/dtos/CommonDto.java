package vn.com.amc.qtdl.tableau_proxy.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommonDto  {


    @NotBlank(message = "Mã loại dùng chung không được để trống")
    @Size(max = 64)
    private String commonTypeCode;

    @NotBlank(message = "Mã danh mục dùng chung không được để trống")
    @Size(max = 64)
    private String commonCode;

    @NotBlank(message = "Tên loại danh mục dùng chung không được để trống")
    @Size(max = 256)
    private String commonName;

    @NotBlank(message = "Giá trị không được để trống")
    @Size(max = 64)
    private String commonValue;

    @Size(max = 512)
    private String description;


}

package
com.naas.admin_service.features.users.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    @NotBlank(message = "username không được để trống")
    @Size(min = 3, max = 50, message = "username phải có ít nhất {min} ký tự và nhiều nhất {max} ký tự")
    private String username;

    @NotBlank(message = "password không được để trống")
    @Size(min = 6, max = 50,  message = "password phải có ít nhất {min} ký tự và nhiều nhất {max} ký tự")
    private String password;

    @Size(max = 50, message = "firstName không được vượt quá {max} ký tự")
    private String firstName;


    @Size(max = 50, message = "lastname không được vượt quá {max} ký tự")
    private String lastName;

    @Email(message = "email không hợp lệ")
    private String email;
}
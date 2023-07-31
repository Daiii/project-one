package cn.project.one.api.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterParam {

    private String ID;

    private String Name;

    private String Address;

    private int Port = 8080;
}

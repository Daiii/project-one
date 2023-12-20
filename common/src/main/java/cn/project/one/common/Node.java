package cn.project.one.common;

import cn.hutool.core.lang.Dict;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Node extends Dict {

    private String ID;

    private String Name;

    private String Address;

    private int Port = 8080;
}

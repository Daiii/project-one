package cn.project.one.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Node{

    private String ID;

    private String Name;

    private String Address;

    private int Port = 8080;
}

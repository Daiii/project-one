package cn.project.one.common.instance;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instance {

    private String ID;

    private String Service;

    private String[] Tags;

    private List<String> Meta;

    private Integer Port;

    private String Address;

    private Weights Weights;

    private boolean EnableTagOverride;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class Weights {
        private int Passing;
        private int Warning;
    }
}
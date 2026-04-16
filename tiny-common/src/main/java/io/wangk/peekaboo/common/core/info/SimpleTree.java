package io.wangk.peekaboo.common.core.info;

import lombok.Data;

import java.util.List;

@Data
public class SimpleTree {

    private String code;

    private String name;

    private int sn;

    private String parentCode;

    private List<SimpleTree> childrens;
}

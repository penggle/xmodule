package com.penglecode.xmodule.master4j.java.basics.enums;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 枚举示例
 *
 * 0、通过反编译可知enum是继承自java.lang.Enum类的
 *
 * 1、enum可以有成员变量
 *
 * 2、enum的构造方法只能是private的
 *
 * 3、enum可以实现接口并重写方法
 *
 * 4、enum只能重写toString()方法
 *
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/14 19:15
 */
public class EnumExample {

    public static interface Payment {

        public Set<String> getPaymentType();

    }

    public static enum ProductType implements Payment {

        VIRTUAL_PRODUCT(1, "虚拟商品"),
        MATERIAL_PRODUCT(1, "实物商品") {
            @Override
            public Set<String> getPaymentType() {
                Set<String> types = new HashSet<>(super.getPaymentType());
                types.add("货到付款");
                return types;
            }
        };

        private Integer typeCode;

        private String typeName;

        ProductType(Integer typeCode, String typeName) {
            this.typeCode = typeCode;
            this.typeName = typeName;
        }

        public Integer getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(Integer typeCode) {
            this.typeCode = typeCode;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }



        @Override
        public String toString() {
            return typeName + "(" + typeCode + ")";
        }

        @Override
        public Set<String> getPaymentType() {
            return Collections.singleton("在线支付");
        }
    }

    public static void main(String[] args) {

    }

}

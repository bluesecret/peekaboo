package io.wangk.peekaboo.component.encrypt.core.annotation;


import io.wangk.peekaboo.component.encrypt.core.enums.AlgorithmType;
import io.wangk.peekaboo.component.encrypt.core.enums.EncodeType;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptField {

    /**
     * 开启加密
     *
     * @return
     */
    boolean encrypt() default true;

    /**
     * 开启解密
     *
     * @return
     */
    boolean decrypt() default true;

    /**
     * 加密算法
     */
    AlgorithmType algorithm() default AlgorithmType.DEFAULT;

    /**
     * 秘钥。AES、SM4需要
     */
    String password() default "";

    /**
     * 公钥。RSA、SM2需要
     */
    String publicKey() default "";

    /**
     * 私钥。RSA、SM2需要
     */
    String privateKey() default "";

    /**
     * 编码方式。对加密算法为BASE64的不起作用
     */
    EncodeType encodeType() default EncodeType.DEFAULT;

}

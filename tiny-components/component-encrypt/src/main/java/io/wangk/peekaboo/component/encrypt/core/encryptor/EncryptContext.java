package io.wangk.peekaboo.component.encrypt.core.encryptor;

import io.wangk.peekaboo.component.encrypt.core.enums.AlgorithmType;
import io.wangk.peekaboo.component.encrypt.core.enums.EncodeType;
import lombok.Data;

import java.util.Objects;

@Data
public class EncryptContext {

    /**
     * 默认算法
     */
    private AlgorithmType algorithm;

    /**
     * 安全秘钥
     */
    private String password;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 编码方式，base64/hex
     */
    private EncodeType encodeType;

    @Override
    public int hashCode() {
        return Objects.hash(algorithm, password, publicKey, privateKey, encodeType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        EncryptContext other = (EncryptContext) obj;
        return algorithm == other.algorithm && Objects.equals(password, other.password)
                && Objects.equals(publicKey, other.publicKey) && Objects.equals(privateKey, other.privateKey) && encodeType == other.encodeType;
    }

}

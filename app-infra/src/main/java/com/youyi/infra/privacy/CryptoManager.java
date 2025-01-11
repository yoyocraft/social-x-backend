package com.youyi.infra.privacy;

import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.ConfigKey;
import com.youyi.common.type.InfraCode;
import com.youyi.common.util.AESUtil;
import com.youyi.common.util.IvGenerator;

import static com.youyi.infra.config.core.SystemConfigService.getStringConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
public class CryptoManager {


    public static String aesEncrypt(String plainText) {
        String aesKey = getStringConfig(ConfigKey.AES_KEY);
        String aesAlgo = getStringConfig(ConfigKey.AES_ALGORITHM);

        try {
            return AESUtil.encrypt(aesKey, aesAlgo, plainText, IvGenerator.generateIv(plainText));
        } catch (Exception e) {
            throw AppSystemException.of(InfraCode.ENCRYPT_ERROR);
        }
    }

    public static String aesDecrypt(String cipherText, String iv) {
        String aesKey = getStringConfig(ConfigKey.AES_KEY);
        String aesAlgo = getStringConfig(ConfigKey.AES_ALGORITHM);

        try {
            return AESUtil.decrypt(aesKey, aesAlgo, cipherText, iv);
        } catch (Exception e) {
            throw AppSystemException.of(InfraCode.DECRYPT_ERROR);
        }
    }
}

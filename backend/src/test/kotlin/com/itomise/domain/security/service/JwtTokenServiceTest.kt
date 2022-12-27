package com.itomise.domain.security.service

import com.itomise.com.itomise.domain.security.interfaces.IJwtTokenService
import com.itomise.com.itomise.module.serviceModule
import com.itomise.com.itomise.util.getKoinInstance
import com.nimbusds.jose.EncryptionMethod
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jose.JWEObject
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.crypto.DirectDecrypter
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.RSAKey
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class JwtTokenServiceTest {
    private lateinit var nestedJwtService: IJwtTokenService

    @BeforeTest
    fun prepare() {
        startKoin {
            modules(serviceModule)
        }
        nestedJwtService = getKoinInstance()
    }

    @AfterTest
    fun cleanup() {
        stopKoin()
    }

    private val privateJwkJsonString =
        "{\"kty\":\"RSA\",\"n\":\"tvN64jUQPceepr4t3f_RmDcifIHLV2qOKgeblnTpBav_wr8tccmRui2njODZQnRNYlQFq72mmsxYchNXoNvNHvbGaoah3pljC1CpDDDW1gjx-0X1UHnTHM5Jt9CZd8mEqr3lMJjIQwW-Td-n7S9zSOrtn5Dftp3bF0iu4pUMlGqxd-1x1n59O5jRN8S58Et54_x63LnYe323JL2qMWxHDmuzuUxsvfR1UqKPoF1xKU2GtEDcznItEYoSn07LshzIo6NMbo2s6WSN1EzXj4aKYZzSa3rlMZLL4eZAIhy-g39xlpNtXAy6wczlK7mdJtWS9KtdbLrEdzDTfgOlh_McSQ\",\"e\":\"AQAB\",\"d\":\"MUNIsIaXU_tfhgipsyCCYJlgCOSuS2Ce3URlKdNbN7LD0L3Hz972BYrrleyps9I7UCHN0RvlM8OYgUciDmeBSTgr311XUug8mjxi-Kxh_Qo1lnKVBtF2_kex4_QgjvfNaGhvwXEyY5G1V6TExAJF6bmQuKm7A6Jn71Ib_VkjJQdMkrSiBPY0LvjRawZQyCiWDw6ICwymQBcaxh7Ie6yf78diarq24MwPzSW1M7LdVQodBWHBaiFWBR46mCwsi7k-I9DcGNZQJkfaPz54R3JPQoE7RbdiM3tmQDjBpwbnt7Y_B_MxeWcPU1cZ87hMtYB1nGLIb6ofARZLZGMM3F_tPQ\",\"p\":\"73grPL9-ZjlSN0vCDEUl75zU-ioTldQ1f-lkYQcwuUnMr3mIfOiToouR_9Na_qQ6f9eK5e1zzffAPMj3Z6xAnFJ42FTVjCDxqjhUbwYKhrH-cfvwgJQdXQfRA1ZaZUeczgZZHKQ2CF6HVSkC9uTtXw23F7E6OKNViogpja8HBNM\",\"q\":\"w5SJWFUhHpucmx0em0vRU1TpoVClhCP6Bspn6hEa_eniXlHTB48fgWRTGVVFKcyOlGHqKZiUaYn60GuiIiY9-re-KYLmyCciU14acrFl-6Mxm8xFw1EdnP0d13-p0kiURwH4wNisksHg2kmeX5zVKxWWaqGKohekYnfAzeo7WPM\",\"dp\":\"Svm919RA1_KkeimfBMZ1nTLmP7kCzstVQMorInHk4G5pxprvj-QlrSOzv3xGJin3IuWyWFDjDB7AsoddZrk4tXjg2yKchbYe0-O1E9m5W_jIqJlv8GHvW5xB5aCYxtMIFj1Ikz4aqL4n8xLXGcQNSdB6fdGAYBFLjAcbs3-UW_8\",\"dq\":\"ifpRhvLP4cMQkdHSVOcBL1P5WugFTRsAeifzdYtJypZlL_tz8KJFLMi1Y2rn11xLvlzxkRTmpypDuyQkBwmJskZMrwZ9f_kz2zLJX97NPuDbz7kmAmxIgweDNRQIn6S6jvnSH2L9JLAIzJchVvzS2olpS6LZpg8d1qX7bInyAoc\",\"qi\":\"cG-8G6zISqAi71fMLMbOjxelDZMI3DlKxmX9zoauGeIkiuklCiHXhZzogNwuBdGQ6KRTjv0uV7txSPu6EeF0AEzoTIKcvgZieL67Fc0bH2TNdWEcqQaMUxdffPzbzeGnmEg_RliJriX4-vv18ptgGRixBXbfJpgxxfoY34302SQ\"}"
    private val publicJwkJsonString =
        "{\"use\":\"sig\",\"kty\":\"RSA\",\"n\":\"tvN64jUQPceepr4t3f_RmDcifIHLV2qOKgeblnTpBav_wr8tccmRui2njODZQnRNYlQFq72mmsxYchNXoNvNHvbGaoah3pljC1CpDDDW1gjx-0X1UHnTHM5Jt9CZd8mEqr3lMJjIQwW-Td-n7S9zSOrtn5Dftp3bF0iu4pUMlGqxd-1x1n59O5jRN8S58Et54_x63LnYe323JL2qMWxHDmuzuUxsvfR1UqKPoF1xKU2GtEDcznItEYoSn07LshzIo6NMbo2s6WSN1EzXj4aKYZzSa3rlMZLL4eZAIhy-g39xlpNtXAy6wczlK7mdJtWS9KtdbLrEdzDTfgOlh_McSQ\",\"e\":\"AQAB\",\"kid\":\"12345\",\"alg\":\"RS256\"}"

    @Test
    fun `正しくencryptとdecryptができること`() {
        val privateKey = RSAKey.parse(privateJwkJsonString)

        // 32 文字 ( 256bit )
        val encryptionKey = "P!YK.qMi2GQpojPA*mPFfxFQZ2tCewTr".toByteArray()

        val claims = mapOf(
            "userName" to "Penguin"
        )

        // NestedJWT 生成
        val nestedJwt = nestedJwtService.generate(privateKey, encryptionKey, claims)

        // 検証
        val jweObject = JWEObject.parse(nestedJwt)

        // 暗号化方式と暗号アルゴリズムの検証
        jweObject.header.also {
            assert(JWEAlgorithm.DIR == it.algorithm)
            assert(EncryptionMethod.A256GCM == it.encryptionMethod)
        }

        // デコード
        jweObject.decrypt(DirectDecrypter(encryptionKey))

        // 署名検証および "userName" に正しい値がセットされているかどうか検証
        val publicKey = RSAKey.parse(publicJwkJsonString)

        jweObject.payload.toSignedJWT().also {
            assert(JWSAlgorithm.RS256 == it.header.algorithm)
            assert(it.verify(RSASSAVerifier(publicKey)))
            claims.forEach { (key, value) ->
                assert(value == it.jwtClaimsSet.getClaim(key))
            }
        }
    }

    @Test
    fun `tokenを正しく検証できること`() {
        val privateKey = RSAKey.parse(privateJwkJsonString)

        // 32 文字 ( 256bit )
        val encryptionKey = "P!YK.qMi2GQpojPA*mPFfxFQZ2tCewTr".toByteArray()

        val claims = mapOf(
            "userName" to "Penguin"
        )

        val token = nestedJwtService.generate(privateKey, encryptionKey, claims)

        val publicKey = RSAKey.parse(publicJwkJsonString)
        assert(nestedJwtService.verify(token, publicKey, encryptionKey))
    }

    @Test
    fun `tokenが正しくない場合verifyがfalseになること`() {
        val privateKey = RSAKey.parse(privateJwkJsonString)

        // 32 文字 ( 256bit )
        val encryptionKey = "P!YK.qMi2GQpojPA*mPFfxFQZ2tCewTr".toByteArray()

        val claims = mapOf(
            "userName" to "Penguin"
        )

        val token = nestedJwtService.generate(privateKey, encryptionKey, claims)

        val invalidToken = token.split(".")[0] + "." + token.split(".")[1]
        val publicKey = RSAKey.parse(publicJwkJsonString)

        assert(!nestedJwtService.verify(invalidToken, publicKey, encryptionKey))
    }

    @Test
    fun `encryptionKeyが正しくない場合verifyがfalseになること`() {
        val privateKey = RSAKey.parse(privateJwkJsonString)

        // 32 文字 ( 256bit )
        val encryptionKey = "P!YK.qMi2GQpojPA*mPFfxFQZ2tCewTr".toByteArray()
        val invalidEncryptionKey = "P!YK.qMF3j5pojPA*mPFfxFQZ2tCewTr".toByteArray()

        val claims = mapOf(
            "userName" to "Penguin"
        )

        val token = nestedJwtService.generate(privateKey, encryptionKey, claims)

        val publicKey = RSAKey.parse(publicJwkJsonString)
        assert(!nestedJwtService.verify(token, publicKey, invalidEncryptionKey))
    }
}
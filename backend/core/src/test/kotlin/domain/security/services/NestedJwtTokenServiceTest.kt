package domain.security.services

import com.itomise.core.domain.security.services.NestedJwtTokenTokenService
import com.nimbusds.jose.EncryptionMethod
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jose.JWEObject
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.crypto.DirectDecrypter
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class
NestedJwtTokenServiceTest : KoinTest {
    private val nestedJwtTokenService by inject<NestedJwtTokenTokenService>()

    @BeforeTest
    fun prepare() {
        startKoin {
            modules(module { single { NestedJwtTokenTokenService() } })
        }
    }

    @AfterTest
    fun cleanup() {
        stopKoin()
    }

    private val publicJwks = """
        {
          "keys": [
            {
              "use": "sig",
              "kty": "RSA",
              "n": "ql5HLRkh27pA0zONngdyk_mRo7TgcR7Et31YbqDsWDcfLYN_P1XrdTetg89HHuSC_P_G5rabx3NIzenK_Ej8lZES0F7lpagIwaMZQjPO0urEp53MuRhoogppBr6uxRP_Mkv-bESK_cTNaTgG5nfkWzjfq6Bx1wjNOhWQDONAd81V9jFt8vm0oDzdErsBKUmuysRo7Seuol2mgD5D8AyBYyv79NRaP1anuje4h0DUo45yevxOjjdyAwCcM6uZPPNtDN7AoM469il--rgV-17DJSz3lKnYUdwepqn0ULeqCPORRjJ0p-B5VDJI0_CuYMiO8XsQh43y-znq8pYiIkISow",
              "e": "AQAB",
              "kid": "673a5ea3-a4ae-422b-be55-a9c98e674550",
              "alg": "RS256"
            }
          ]
        }
    """.trimIndent()
    private val privatePKCS8String =
        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCqXkctGSHbukDTM42eB3KT+ZGjtOBxHsS3fVhuoOxYNx8tg38/Vet1N62Dz0ce5IL8/8bmtpvHc0jN6cr8SPyVkRLQXuWlqAjBoxlCM87S6sSnncy5GGiiCmkGvq7FE/8yS/5sRIr9xM1pOAbmd+RbON+roHHXCM06FZAM40B3zVX2MW3y+bSgPN0SuwEpSa7KxGjtJ66iXaaAPkPwDIFjK/v01Fo/Vqe6N7iHQNSjjnJ6/E6ON3IDAJwzq5k8820M3sCgzjr2KX76uBX7XsMlLPeUqdhR3B6mqfRQt6oI85FGMnSn4HlUMkjT8K5gyI7xexCHjfL7OeryliIiQhKjAgMBAAECggEAQ0mRGwK+GKZrS/8kg7G1UzRSjTG3I3Zfg4kCEE+GiSDcM4GoFD+/C0C+SzzmfhEKmxn6WMzuocuqiYk5HoNFH1147MtOMCs5qiDcrQUBPtRybzEn1kMJsFYkPQG6zSOkuxzAk8pOiwPbjR8Pup7Hli/UR1jLHX7gb3xstGpc3/OYMSM/XrbiDeamSZX+l26CLxApZm9ihH1F/MJIOMNTQarqTBP8UyDCbbaE29EoDkQfBydzclxrh8AQXVOVrTyjqTq8MTZcep0BbytPcUJ3u4zygWPObmKp4+f93idskeV6xAD+bSFcawWB9uzwit9QyP2u1CpCdV6q6GDuUqv9wQKBgQDc+YayBxcA5nokkc55QzhZnoGk2NnxXzdGIa3bSGtRSG60hz3WUYEWd1vE2lQOBF3yO9ctLv433LzlHPRp2Blt0DLJ6iGL/p35U45tGxipytUJwEuSm8nOowBsq8OG94rAALos5Ho8gnCDbQAJ8HJN9SYUHstp3PIxnuSMO/gyaQKBgQDFX0xgMwiiMTpuSNzxKB+3i7z3NMaMrrf59xHNtA5qfKG0jC+IpUS4AbFRsG0IxhOHiHeCrKVeWKE8jd/I4goiTymipY8TNIiYFjQT9l6d+ztYdpXlopi9DxOnoMl2kYcTPqDOcqg/8N9aYo8/hS/iCTRZjrAxYBlGxHHJSxtjKwKBgAPVbGnQR6zjUsEpqmxeJy8e2d7zSCyw2zOjR/Hi+sGpQQpuwjripRuvrG7o3sZzevHF6gDFrPFpKfnLm83uStql3wuzvStBmpoFwhXQf4gKldGafXUosMKd/1bjT/wadVTJbnFy8U/1b2BIVtTVUBZcJ3H36s2GYMlNmWd5bt/pAoGAfnqUSe1KMg2T4QpmM0/AC3HLtSqntY1dLhzK1uP0fP7C7R0MdOdbgwLqq2uAY6fy6d5kn2OdUTlAkbpk1qkrT1MlXinz5p26jqNj2gr+FQTiwcy0QASTFkwsDzZ1A7s8MPPoPoOxF9B2gMg5/b2Osq2tU1qhw/JOIFBQjmrH8SMCgYEAjinWsRZi/yA8zfZ0RP3Ru8fU2JHfnMPdp0eyhSIrXNq9VWbCumYyN1r5nCxS+WLrhXTGBrb1JA4sKQ4VsW4xOtQkFF5R3fWqWON+00FTGR2JrjJo6h4UsJfRQwIBoCqr0HaEBd1A3W3qQpb5E+/HzFTHSmVR7a2AFi6TWAbDxlI="
    private val encryptionKey = "P!YK.qMi2GQpojPA*mPFfxFQZ2tCewTr".toByteArray()

    private fun getPrivateKey(): PrivateKey {
        val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privatePKCS8String))
        return KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)
    }

    private fun getPublicKey(): RSAKey {
        val publicKeys = JWKSet.parse(publicJwks)
        return publicKeys.getKeyByKeyId("673a5ea3-a4ae-422b-be55-a9c98e674550")
            .toPublicJWK().toRSAKey()
    }

    @Test
    fun `正しくencryptとdecryptができること`() {
        val privateKey = getPrivateKey()

        val claims = mapOf(
            "userName" to "Penguin"
        )

        // NestedJWT 生成
        val nestedJwt = nestedJwtTokenService.generate(privateKey, encryptionKey, claims)

        // 検証
        val jweObject = JWEObject.parse(nestedJwt)

        // 暗号化方式と暗号アルゴリズムの検証
        jweObject.header.also {
            assert(JWEAlgorithm.DIR == it.algorithm)
            assert(EncryptionMethod.A256GCM == it.encryptionMethod)
        }

        // デコード
        jweObject.decrypt(DirectDecrypter(encryptionKey))

        val publicKey = getPublicKey()
        // 署名検証および "userName" に正しい値がセットされているかどうか検証
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
        val privateKey = getPrivateKey()

        val claims = mapOf(
            "userName" to "Penguin"
        )

        val token = nestedJwtTokenService.generate(privateKey, encryptionKey, claims)

        val publicKey = getPublicKey()
        assert(nestedJwtTokenService.verify(token, publicKey, encryptionKey) != null)
    }

    @Test
    fun `tokenが正しくない場合verifyがfalseになること`() {
        val privateKey = getPrivateKey()

        val claims = mapOf(
            "userName" to "Penguin"
        )

        val token = nestedJwtTokenService.generate(privateKey, encryptionKey, claims)

        val invalidToken = token.split(".")[0] + "." + token.split(".")[1]
        val publicKey = getPublicKey()

        assert(nestedJwtTokenService.verify(invalidToken, publicKey, encryptionKey) == null)
    }

    @Test
    fun `encryptionKeyが正しくない場合verifyがfalseになること`() {
        val privateKey = getPrivateKey()

        val invalidEncryptionKey = "P!YK.qMF3j5pojPA*mPFfxFQZ2tCewTr".toByteArray()

        val claims = mapOf(
            "userName" to "Penguin"
        )

        val token = nestedJwtTokenService.generate(privateKey, encryptionKey, claims)

        val publicKey = getPublicKey()
        assert(nestedJwtTokenService.verify(token, publicKey, invalidEncryptionKey) == null)
    }
}
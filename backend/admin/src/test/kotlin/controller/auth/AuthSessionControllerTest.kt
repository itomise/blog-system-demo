package controller.auth

import com.auth0.jwk.Jwk
import com.auth0.jwk.JwkProvider
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.admin.controller.requestModels.LoginRequestModel
import com.itomise.admin.controller.requestModels.SignUpRequestModel
import com.itomise.admin.controller.responseModels.MeResponseModel
import com.itomise.admin.controller.responseModels.SignUpResponseModel
import com.itomise.admin.module.jwkProvider
import controller.BaseTestApplication
import controller.BaseTestApplication.Companion.appTestApplication
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.mockk.every
import io.mockk.mockk
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AuthSessionControllerTest {
    @AfterTest
    fun after() = BaseTestApplication.cleanup()

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `サインアップーログインが正しく行えること`() = appTestApplication {
        val mockJwkProvider = mockk<JwkProvider>()
        every { mockJwkProvider.get(any()) } returns Jwk.fromValues(
            mapOf(
                "use" to "sig",
                "kty" to "RSA",
                "n" to "ql5HLRkh27pA0zONngdyk_mRo7TgcR7Et31YbqDsWDcfLYN_P1XrdTetg89HHuSC_P_G5rabx3NIzenK_Ej8lZES0F7lpagIwaMZQjPO0urEp53MuRhoogppBr6uxRP_Mkv-bESK_cTNaTgG5nfkWzjfq6Bx1wjNOhWQDONAd81V9jFt8vm0oDzdErsBKUmuysRo7Seuol2mgD5D8AyBYyv79NRaP1anuje4h0DUo45yevxOjjdyAwCcM6uZPPNtDN7AoM469il--rgV-17DJSz3lKnYUdwepqn0ULeqCPORRjJ0p-B5VDJI0_CuYMiO8XsQh43y-znq8pYiIkISow",
                "e" to "AQAB",
                "kid" to "673a5ea3-a4ae-422b-be55-a9c98e674550",
                "alg" to "RS256"
            )
        )

        jwkProvider = mockJwkProvider

        val client = createClient { install(HttpCookies) }

        val name = "テスト太郎"
        val email = "${UUID.randomUUID()}@example.com"
        val password = UUID.randomUUID()

        val signUpRes = client.post("/api/auth/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    SignUpRequestModel(
                        email = email,
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }
        val signUpResBody = objectMapper.readValue<SignUpResponseModel>(signUpRes.bodyAsText())

        client.post("/activate-for-testing") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    BaseTestApplication.Companion.ActivateTestUserRequest(
                        id = signUpResBody.userId,
                        name = name,
                        email = email,
                        password = password.toString()
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }

        client.post("/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    LoginRequestModel(
                        email = email,
                        password = password.toString()
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }

        client.get("/api/auth/me").apply {
            assertEquals(HttpStatusCode.OK, this.status)
            val body = objectMapper.readValue<MeResponseModel>(this.bodyAsText())

            assertEquals(signUpResBody.userId, body.id)
            assertEquals(name, body.name)
            assertEquals(email, body.email)
        }
    }

    @Test
    fun `未ログイン状態でMeを叩くとUnauthorizedになること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        client.get("/api/auth/me").apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }
}
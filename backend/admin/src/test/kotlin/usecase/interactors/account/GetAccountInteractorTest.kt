package usecase.interactors.account

import com.itomise.admin.usecase.interfaces.account.ICreateAccountUseCase
import com.itomise.admin.usecase.interfaces.account.IGetAccountListUseCase
import com.itomise.admin.usecase.interfaces.account.IGetAccountUseCase
import helper.UnitTestHelper
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.core.component.inject
import org.koin.test.KoinTest
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class GetAccountInteractorTest : KoinTest {
    private val createUserUseCase by inject<ICreateAccountUseCase>()
    private val getListUserUseCase by inject<IGetAccountListUseCase>()
    private val getUserUseCase by inject<IGetAccountUseCase>()

    private val userEmail = "${UUID.randomUUID()}@test.test"
    private lateinit var userId: UUID


    @BeforeTest
    fun prepare() {
        UnitTestHelper.prepare(withDatabase = true)
        runBlocking {
            userId = createUserUseCase.handle(
                ICreateAccountUseCase.Command(email = userEmail)
            )
        }
    }

    @AfterTest
    fun after() = UnitTestHelper.cleanup(withDatabase = true)

    @Test
    fun `ユーザーが取得できること`() = runBlocking {
        val user = getUserUseCase.handle(
            IGetAccountUseCase.Command(
                userId = userId
            )
        )

        assert(user != null)
        assertEquals(userEmail, user!!.email)
    }
}
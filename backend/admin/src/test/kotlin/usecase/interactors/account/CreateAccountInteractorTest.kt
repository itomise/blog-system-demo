package usecase.interactors.account

import com.itomise.admin.usecase.interfaces.account.ICreateAccountUseCase
import com.itomise.admin.usecase.interfaces.account.IGetAccountListUseCase
import helper.UnitTestHelper
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.core.component.inject
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

internal class CreateAccountInteractorTest : KoinTest {
    private val createUserUseCase by inject<ICreateAccountUseCase>()
    private val getListUserUseCase by inject<IGetAccountListUseCase>()

    @BeforeTest
    fun prepare() {
        UnitTestHelper.prepare(withDatabase = true)
    }

    @AfterTest
    fun after() = UnitTestHelper.cleanup(withDatabase = true)

    @Test
    fun `ユーザーが作成できること`() = runBlocking {
        val beforeUsers = getListUserUseCase.handle()
        assertEquals(0, beforeUsers.users.size)

        createUserUseCase.handle(
            ICreateAccountUseCase.Command(
                email = "test@test.test"
            )
        )

        val afterUsers = getListUserUseCase.handle()
        assertEquals(1, afterUsers.users.size)
    }
}
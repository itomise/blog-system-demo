package helper

import com.itomise.blogDb.lib.DataBaseFactory
import com.itomise.test.helper.DatabaseTestHelper
import com.itomise.test.mock.SendGridClientMock
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.koin.core.context.GlobalContext
import org.koin.core.context.stopKoin
import java.util.*

object BlogApiTestApplication {
    fun appTestApplication(
        block: suspend ApplicationTestBuilder.() -> Unit
    ) {
        // test が fail すると Koin が残ったままになるため
        if (GlobalContext.getOrNull() != null) {
            stopKoin()
        }
        unmockkAll()

        testApplication {
            environment {
                config = ApplicationConfig("application.test.conf")
            }
            application {
                val schemaName = "test_${UUID.randomUUID().toString().replace("-", "")}"
                mockkObject(DataBaseFactory)
                every { DataBaseFactory.getMainSchema() } returns schemaName

                // application が起動してからでないと DB などが立ち上がっていないため
                DatabaseTestHelper.setUpSchema()

                SendGridClientMock.execute()
            }
        }
    }
}
package com.itomise.test.mock

import com.itomise.eventBus.lib.SendGridClient
import io.mockk.every
import io.mockk.mockkObject

object SendGridClientMock {
    fun execute() {
        mockkObject(SendGridClient)
        every { SendGridClient.send(any()) } answers {}
    }
}
package me.karun.toggles.fsbt

import org.scalatest.{Matchers, WordSpec}

class InvalidTogglesExceptionTest extends WordSpec with Matchers {
  private val invalidKeys = List("key-2", "key-2")

  "has proper message" when {
    "given invalid keys" in {
      InvalidTogglesException(invalidKeys).getMessage shouldBe expectedMessage(invalidKeys.mkString(", "))
    }

    "given single key" in {
      val invalidKey = "key"
      InvalidTogglesException(invalidKey).getMessage shouldBe expectedMessage(invalidKey)
    }
  }

  "has sorted keys in message" when {
    "given invalid keys" in {
      InvalidTogglesException(invalidKeys).getMessage shouldBe expectedMessage(invalidKeys.sorted.mkString(", "))
    }
  }

  private def expectedMessage(invalidKeys: String) = {
    s"Following toggles are mapped to states do not have a state definition: $invalidKeys"
  }
}

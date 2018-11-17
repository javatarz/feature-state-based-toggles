package me.karun.toggles.fsbt

import me.karun.toggles.fsbt.EnvironmentTogglesTest.{createEnvironment, createEnvironmentWithInvalidFile}
import org.scalatest.{Matchers, WordSpec}

class EnvironmentTogglesTest extends WordSpec with Matchers {
  private val uatEnv = createEnvironment("user_acceptance_test")

  "should be on" when {
    "environment state == toggle state" in {
      uatEnv.toggleIsEnabled("signed-off-toggle") shouldBe true

      val itEnv = createEnvironment("integration_test")
      itEnv.toggleIsEnabled("signed-off-toggle") shouldBe true
    }

    "environment state < toggle state" in {
      uatEnv.toggleIsEnabled("ready-for-release-toggle") shouldBe true
    }
  }

  "should be off" when {
    "environment state > toggle state" in {
      uatEnv.toggleIsEnabled("in-dev-toggle") shouldBe false
    }
  }

  "should throw exception" when {
    "config is invalid" in {
      val expectedMessage = "Following toggles are mapped to states do not have a state definition: in-qa-1-toggle, in-qa-2-toggle, signed-off-toggle"

      val caught = intercept[RuntimeException] {
        createEnvironmentWithInvalidFile("local")
      }

      caught.getMessage shouldBe expectedMessage
    }

    "environment is invalid" in {
      val expectedMessage = "invalid-environment is not mapped to a state"

      val caught = intercept[RuntimeException] {
        createEnvironment("invalid-environment")
      }

      caught.getMessage shouldBe expectedMessage
    }
  }
}

object EnvironmentTogglesTest {
  def createEnvironment(envName: String) = new EnvironmentToggles(fileName = "valid-toggles.conf", baseKey = "me.karun.toggles", environmentName = envName)

  def createEnvironmentWithInvalidFile(envName: String) = new EnvironmentToggles(fileName = "invalid-toggles.conf", baseKey = "me.karun.toggles", environmentName = envName)
}
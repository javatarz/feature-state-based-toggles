package me.karun.toggles.fsbt

import me.karun.toggles.fsbt.EnvironmentTogglesTest.{createEnvironment, createEnvironmentWithInvalidFile}
import org.scalatest.{Matchers, WordSpec}

class EnvironmentTogglesTest extends WordSpec with Matchers {
  private val uatEnv = createEnvironment("user_acceptance_test")

  "should be on" when {
    "environment state == toggle state" in {
      uatEnv.toggleIsEnabled("feature-4") shouldBe true

      val itEnv = createEnvironment("integration_test")
      itEnv.toggleIsEnabled("feature-4") shouldBe true
    }

    "environment state < toggle state" in {
      uatEnv.toggleIsEnabled("feature-5") shouldBe true
    }
  }

  "should be off" when {
    "environment state > toggle state" in {
      uatEnv.toggleIsEnabled("feature-2") shouldBe false
    }
  }

  "should throw exception" when {
    "config is invalid" in {
      val expectedMessage = "Following toggles are mapped to states do not have a state definition: in-qa-1-toggle, in-qa-2-toggle, signed-off-toggle"

      val caught = intercept[InvalidTogglesException] {
        createEnvironmentWithInvalidFile("local")
      }

      caught.getMessage shouldBe expectedMessage
    }

    "environment is invalid" in {
      val expectedMessage = "invalid-environment was not mapped to a state in the configuration"

      val caught = intercept[UnmappedEnvironmentException] {
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
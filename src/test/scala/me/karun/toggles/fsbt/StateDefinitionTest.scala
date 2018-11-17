package me.karun.toggles.fsbt

import org.scalatest.{Matchers, WordSpec}

class StateDefinitionTest extends WordSpec with Matchers {
  val state = StateDefinition(1, "state-value", List("env-1", "env-2", "env-3"))

  "can run" should {
    val higherState = StateDefinition(10, "higher-state", List())

    "return true" when {
      "env is lower than toggle state" in {
        state.canRun(higherState) shouldBe true
      }
      "env is equal to toggle state" in {
        state.canRun(higherState) shouldBe true
      }
    }

    "return false" when {
      "env is higher than toggle state" in {
        higherState.canRun(state) shouldBe false
      }
    }
  }

  "can handle environment" should {
    "return true" when {
      "environment is in the state definition" in {
        state.canHandleEnvironment("env-2") shouldBe true
      }

      "environment is in the state definition but with different case" in {
        state.canHandleEnvironment("ENV-2") shouldBe true
      }
    }

    "return false" when {
      "environment is in the state definition" in {
        state.canHandleEnvironment("env-10") shouldBe false
      }
    }
  }

  "can handle state" should {
    "return true" when {
      "state string is equal" in {
        state.canHandleState("state-value") shouldBe true
      }
    }

    "return false" when {
      "state string is not equal" in {
        state.canHandleState("other-state-value") shouldBe false
      }

      "state string is the same but with different case" in {
        state.canHandleState("STATE-VALUE") shouldBe false
      }
    }
  }
}

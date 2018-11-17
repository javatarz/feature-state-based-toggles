package me.karun.toggles.fsbt

import org.scalatest.{Matchers, WordSpec}

class UnmappedEnvironmentExceptionTest extends WordSpec with Matchers {
  "has proper message" in {
    new UnmappedEnvironmentException("env-name").getMessage shouldBe "env-name was not mapped to a state in the configuration"
  }
}

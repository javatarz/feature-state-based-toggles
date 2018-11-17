package me.karun.toggles.fsbt

private class UnmappedEnvironmentException(environmentName: String) extends RuntimeException(s"$environmentName was not mapped to a state in the configuration")

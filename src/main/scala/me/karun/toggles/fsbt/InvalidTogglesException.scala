package me.karun.toggles.fsbt

private class InvalidTogglesException(invalidKeys: List[String]) extends RuntimeException(s"Following toggles are mapped to states do not have a state definition: ${invalidKeys.mkString(", ")}")

private object InvalidTogglesException {
  def apply(invalidKeys: Iterable[String]): InvalidTogglesException = new InvalidTogglesException(invalidKeys.toList.sorted)

  def apply(invalidKey: String): InvalidTogglesException = new InvalidTogglesException(List(invalidKey))
}

package me.karun.toggles.fsbt

import java.util

import scala.collection.JavaConverters.iterableAsScalaIterableConverter

case class StateDefinition(private val position: Int, private val state: String, private val environments: List[String]) {
  def canRun(otherState: StateDefinition): Boolean = position <= otherState.position

  def canHandleEnvironment(environment: String): Boolean = environments.exists(_.equalsIgnoreCase(environment))

  def canHandleState(state: String): Boolean = state == this.state
}

object StateDefinition {
  def apply(map: Map[String, Any], position: Int): StateDefinition = {
    val state = map("state").toString
    val environments = map("environments").asInstanceOf[util.List[String]].asScala.toList
    new StateDefinition(position = position, state = state, environments = environments)
  }
}
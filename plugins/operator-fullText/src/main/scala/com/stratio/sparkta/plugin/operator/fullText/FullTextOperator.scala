/**
 * Copyright (C) 2014 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.sparkta.plugin.operator.fullText

import java.io.{Serializable => JSerializable}
import com.stratio.sparkta.sdk._
import com.stratio.sparkta.sdk.ValidatingPropertyMap._

import scala.util.Try

class FullTextOperator(properties: Map[String, JSerializable]) extends Operator(properties) {

  override val typeOp = Some(TypeOp.String)

  private val inputField = if(properties.contains("inputField")) Some(properties.getString("inputField")) else None

  override val key : String = if(inputField.isDefined) inputField.get else "undefined"

  override val writeOperation = WriteOp.FullText

  override def processMap(inputFields: Map[String, JSerializable]): Option[String] = {
    if ((inputField.isDefined) && (inputFields.contains(inputField.get))) {
      Some(inputFields.get(inputField.get).get.asInstanceOf[String])
    } else FullTextOperator.SOME_EMPTY
  }

  override def processReduce(values : Iterable[Option[Any]]): Option[String] = {
    Try(Some(values.map(_.get.asInstanceOf[String]).reduce(_ + FullTextOperator.SEPARATOR + _)))
      .getOrElse(FullTextOperator.SOME_EMPTY)
  }
}

private object FullTextOperator {
  val SOME_EMPTY = Some("")
  val SEPARATOR = " "
}

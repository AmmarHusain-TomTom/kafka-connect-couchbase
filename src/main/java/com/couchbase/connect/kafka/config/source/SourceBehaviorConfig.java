/*
 * Copyright 2020 Couchbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.couchbase.connect.kafka.config.source;

import com.couchbase.connect.kafka.StreamFrom;
import com.couchbase.connect.kafka.filter.Filter;
import com.couchbase.connect.kafka.handler.source.SourceHandler;
import com.couchbase.connect.kafka.util.config.annotation.Default;

import java.util.List;

public interface SourceBehaviorConfig {
  /**
   * Name of the Kafka topic to publish data to.
   */
  String topic();

  /**
   * The class name of the source handler to use.
   * The source handler determines how the Couchbase document is converted into a Kafka record.
   */
  @Default("com.couchbase.connect.kafka.handler.source.DefaultSchemaSourceHandler")
  Class<? extends SourceHandler> sourceHandler();

  /**
   * The class name of the event filter to use.
   * The event filter determines whether or not a database change event is ignored.
   */
  @Default("com.couchbase.connect.kafka.filter.AllPassFilter")
  Class<? extends Filter> eventFilter();

  /**
   * Controls maximum size of the batch for writing into topic.
   */
  @Default("2000")
  int batchSizeMax();

  /**
   * If true, the library will use name in the offsets to allow multiple connectors for the same bucket.
   */
  @Default("true")
  boolean connectorNameInOffsets();

  /**
   * Controls when in history then connector starts streaming from.
   */
  @Default("SAVED_OFFSET_OR_BEGINNING")
  StreamFrom streamFrom();

  /**
   * If you wish to stream from all collections within a scope, specify the scope name here.
   * <p>
   * If you specify neither "couchbase.scope" nor "couchbase.collections",
   * the connector will stream from all collections of all scopes in the bucket.
   * <p>
   * Requires Couchbase Server 7.0 or later.
   */
  @Default
  String scope();

  /**
   * If you wish to stream from specific collections, specify the fully qualified collection
   * names here, separated by commas. A fully qualified name is the name of the scope
   * followed by a dot (.) and then the name of the collection. For example:
   * "tenant-foo.invoices".
   * <p>
   * If you specify neither "couchbase.scope" nor "couchbase.collections",
   * the connector will stream from all collections of all scopes in the bucket.
   * <p>
   * Requires Couchbase Server 7.0 or later.
   */
  @Default
  List<String> collections();
}
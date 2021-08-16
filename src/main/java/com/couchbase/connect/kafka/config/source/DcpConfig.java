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

import com.couchbase.client.dcp.config.CompressionMode;
import com.couchbase.connect.kafka.handler.source.DocumentEvent;
import com.couchbase.connect.kafka.util.config.DataSize;
import com.couchbase.connect.kafka.util.config.annotation.Default;
import com.couchbase.connect.kafka.util.config.annotation.Group;

import java.time.Duration;

@Group("Database Change Protocol")
public interface DcpConfig {
  /**
   * To reduce bandwidth usage, Couchbase Server 5.5 and later can send documents
   * to the connector in compressed form. (Messages are always published to the
   * Kafka topic in uncompressed form, regardless of this setting.)
   */
  @Default("ENABLED")
  CompressionMode compression();

  /**
   * When a Couchbase Server node fails over, documents on the failing node
   * that haven't been fully replicated may be "rolled back" to a previous state.
   * To ensure consistency between Couchbase and the Kafka topic, the connector
   * can defer publishing a document to Kafka until it has been saved to disk
   * on all replicas.
   * <p>
   * To enable this feature, specify a non-zero persistence polling interval.
   * The interval is how frequently the connector asks each Couchbase node
   * which changes have been fully replicated and persisted. This ensures
   * consistency between Couchbase and Kafka, at the cost of additional latency
   * and bandwidth usage.
   * <p>
   * To disable this feature, specify a zero duration (`0`). In this mode the
   * connector publishes changes to Kafka immediately, without waiting for
   * replication. This is fast and uses less network bandwidth, but can result
   * in publishing "phantom changes" that don’t reflect the actual state of
   * a document in Couchbase after a failover.
   * <p>
   * CAUTION: When connecting to an ephemeral bucket, always disable
   * persistence polling by setting this config option to `0`,
   * otherwise the connector will never publish any changes.
   */
  @Default("100ms")
  Duration persistencePollingInterval();

  /**
   * How much heap space should be allocated to the flow control buffer.
   * Specify an integer followed by a size qualifier (example: 128m)
   */
  @Default("128m")
  DataSize flowControlBuffer();

  /**
   * Should filters and source handlers have access to a document's extended attributes?
   *
   * @see DocumentEvent#xattrs()
   */
  @Default("false")
  boolean xattrs();
}

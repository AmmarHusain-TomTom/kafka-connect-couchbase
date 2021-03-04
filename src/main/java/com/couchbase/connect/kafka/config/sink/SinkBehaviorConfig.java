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

package com.couchbase.connect.kafka.config.sink;

import com.couchbase.client.core.annotation.Stability;
import com.couchbase.connect.kafka.sink.DocumentMode;
import com.couchbase.connect.kafka.sink.N1qlMode;
import com.couchbase.connect.kafka.sink.SubDocumentMode;
import com.couchbase.connect.kafka.util.ScopeAndCollection;
import com.couchbase.connect.kafka.util.TopicMap;
import com.couchbase.connect.kafka.util.config.annotation.Default;
import org.apache.kafka.common.config.ConfigDef;

import java.time.Duration;
import java.util.List;

import static com.couchbase.connect.kafka.util.config.ConfigHelper.validate;

public interface SinkBehaviorConfig {

  /**
   * Qualified name (scope.collection) of the destination collection for messages
   * from topics that don't have a "topic to collection" map entry.
   */
  @Default("_default._default")
  @Stability.Uncommitted
  String defaultCollection();

  @SuppressWarnings("unused")
  static ConfigDef.Validator defaultCollectionValidator() {
    return validate(ScopeAndCollection::parse, "A collection name qualified by a scope name (scope.collection)");
  }

  /**
   * A map from Kafka topic to Couchbase collection.
   * <p>
   * Topic and collection are joined by an equals sign.
   * Map entries are delimited by commas.
   * <p>
   * For example, if you want to write messages from topic "topic1"
   * to collection "scope-a.invoices", and messages from topic "topic2"
   * to collection "scope-a.widgets", you would write:
   * "topic1=scope-a.invoices,topic2=scope-a.widgets".
   * <p>
   * Defaults to an empty map, with all documents going to the collection
   * specified by `couchbase.default.collection`.
   */
  @Default
  @Stability.Uncommitted
  List<String> topicToCollection();

  @SuppressWarnings("unused")
  static ConfigDef.Validator topicToCollectionValidator() {
    return validate(TopicMap::parse, "topic=scope.collection,...");
  }

  /**
   * Format string to use for the Couchbase document ID (overriding the message key).
   * May refer to document fields via placeholders like ${/path/to/field}
   */
  @Default
  String documentId();

  /**
   * Whether to remove the ID identified by 'couchbase.documentId' from the document before storing in Couchbase.
   */
  @Default("false")
  boolean removeDocumentId();

  /**
   * Setting to indicate an update to the entire document or a sub-document.
   */
  @Default("DOCUMENT")
  DocumentMode documentMode();

  /**
   * JSON Pointer to the property of the Kafka message whose value is
   * the subdocument path to use when modifying the Couchbase document.
   */
  @Default
  String subdocumentPath();

  /**
   * Setting to indicate the type of update to a sub-document.
   */
  @Default("UPSERT")
  SubDocumentMode subdocumentOperation();

  /**
   * Setting to indicate the type of update to use when 'couchbase.documentMode' is 'N1QL'.
   */
  @Default("UPDATE")
  N1qlMode n1qlOperation();

  /**
   * When using the UPDATE_WHERE operation, this is the list of document fields that must match the Kafka message in order for the document to be updated with the remaining message fields.
   * To match against a literal value instead of a message field, use a colon to delimit the document field name and the target value.
   * For example, "type:widget,color" matches documents whose 'type' field  is 'widget' and whose 'color' field matches the 'color' field of the Kafka message.
   */
  @Default
  List<String> n1qlWhereFields();

  /**
   * Whether to add the parent paths if they are missing in the document.
   */
  @Default("true")
  boolean subdocumentCreatePath();

  /**
   * When `couchbase.documentMode` is SUBDOCUMENT or N1QL, this property controls
   * whether to create the document if it does not exist.
   */
  @Default("true")
  boolean createDocument();

  /**
   * Document expiration time specified as an integer followed by a time unit (s = seconds, m = minutes, h = hours, d = days).
   * For example, to have documents expire after 30 minutes, set this value to "30m".
   * <p>
   * By default, documents do not expire.
   * <p>
   * Only Applies only to the DOCUMENT and SUBDOCUMENT modes.
   */
  @Default("0")
  Duration documentExpiration();
}

/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gnd.vo;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.Date;

@AutoValue
public abstract class PlaceUpdate {
  // TODO: Move to top level and rename?
  public enum Operation {
    NO_CHANGE,
    CREATE,
    UPDATE,
    DELETE
  }

  public abstract Place getPlace();

  public abstract Operation getOperation();

  public abstract Date getClientTimestamp();

  public abstract ImmutableList<RecordUpdate> getRecordUpdatesList();

  public static Builder newBuilder() {
    return new AutoValue_PlaceUpdate.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setPlace(Place newPlace);

    public abstract Builder setOperation(Operation newOperation);

    public abstract Builder setClientTimestamp(Date newClientTimestamp);

    public abstract Builder setRecordUpdatesList(ImmutableList<RecordUpdate> newRecordUpdatesList);

    public abstract PlaceUpdate build();
  }

  @AutoValue
  public abstract static class RecordUpdate {
    public abstract Record getRecord();

    public abstract Operation getOperation();

    public abstract ImmutableList<ValueUpdate> getValueUpdatesList();

    public static Builder newBuilder() {
      return new AutoValue_PlaceUpdate_RecordUpdate.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
      public abstract Builder setRecord(Record newRecord);

      public abstract Builder setOperation(Operation newOperation);

      public abstract Builder setValueUpdatesList(ImmutableList<ValueUpdate> newValueUpdatesList);

      public abstract RecordUpdate build();
    }

    @AutoValue
    public abstract static class ValueUpdate {
      public abstract String getElementId();

      public abstract Record.Value getValue();

      public abstract Operation getOperation();

      public static Builder newBuilder() {
        return new AutoValue_PlaceUpdate_RecordUpdate_ValueUpdate.Builder();
      }

      @AutoValue.Builder
      public abstract static class Builder {
        public abstract Builder setElementId(String newElementId);

        public abstract Builder setValue(Record.Value newValue);

        public abstract Builder setOperation(Operation newOperation);

        public abstract ValueUpdate build();
      }
    }
  }
}

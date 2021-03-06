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
import com.google.auto.value.extension.memoized.Memoized;

@AutoValue
public abstract class Place {
  public abstract String getId();

  public abstract PlaceType getPlaceType();

  // TODO: Rename to getExternalId() or similar.
  public abstract String getCustomId();

  // TODO: Rename to getLabel().
  public abstract String getCaption();

  public abstract Point getPoint();

  public abstract Timestamps getServerTimestamps();

  public abstract Timestamps getClientTimestamps();

  public abstract Builder toBuilder();

  @Memoized
  @Override
  public abstract int hashCode();

  public static Builder newBuilder() {
    return new AutoValue_Place.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setId(String newId);

    public abstract Builder setPlaceType(PlaceType newPlaceType);

    public abstract Builder setCustomId(String newCustomId);

    public abstract Builder setCaption(String newCaption);

    public abstract Builder setPoint(Point newPoint);

    public abstract Builder setServerTimestamps(Timestamps newServerTimestamps);

    public abstract Builder setClientTimestamps(Timestamps newClientTimestamps);

    public abstract Place build();
  }
}

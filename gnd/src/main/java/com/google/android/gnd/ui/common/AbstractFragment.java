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

package com.google.android.gnd.ui.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;

public abstract class AbstractFragment extends Fragment implements HasSupportFragmentInjector {
  /**
   * Keeps track of fields bound to views so that they can be set to null when the view is
   * destroyed, freeing up memory.
   */
  private Unbinder unbinder;

  @Inject DispatchingAndroidInjector<Fragment> childFragmentInjector;

  @Override
  public void onAttach(Context context) {
    logLifecycleEvent("onAttach()");
    AndroidSupportInjection.inject(this);
    super.onAttach(context);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    logLifecycleEvent("onCreate()");
    super.onCreate(savedInstanceState);
    obtainViewModels();
  }

  protected void obtainViewModels() {}

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    logLifecycleEvent("onCreateView()");
    View view = createView(inflater, container, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
    setUpView();
    observeViewModels();
    return view;
  }

  protected void setUpView() {}

  protected View createView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    throw new UnsupportedOperationException(
        "Subclasses much override either createView or onCreateView");
  }

  protected void observeViewModels() {}

  @Override
  public void onStart() {
    logLifecycleEvent("onStart()");
    super.onStart();
  }

  @Override
  public void onResume() {
    logLifecycleEvent("onResume()");
    super.onResume();
  }

  @Override
  public void onPause() {
    logLifecycleEvent("onPause()");
    super.onPause();
  }

  @Override
  public void onStop() {
    logLifecycleEvent("onStop()");
    super.onStop();
  }

  @Override
  public void onDestroyView() {
    logLifecycleEvent("onDestroyView()");
    if (unbinder != null) {
      unbinder.unbind();
    }
    super.onDestroyView();
  }

  @Override
  public void onDestroy() {
    logLifecycleEvent("onDestroy()");
    super.onDestroy();
  }

  @Override
  public void onDetach() {
    logLifecycleEvent("onDetach()");
    super.onDetach();
  }

  @Override
  public AndroidInjector<Fragment> supportFragmentInjector() {
    return childFragmentInjector;
  }

  protected final void replaceFragment(@IdRes int containerViewId, Fragment fragment) {
    getChildFragmentManager().beginTransaction().replace(containerViewId, fragment).commit();
  }

  private void logLifecycleEvent(String event) {
    Log.d(getClass().getSimpleName(), "Lifecycle event: " + event);
  }
}

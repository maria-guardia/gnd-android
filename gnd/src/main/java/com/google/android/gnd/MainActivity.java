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

package com.google.android.gnd;

import static com.google.android.gnd.rx.RxAutoDispose.autoDisposable;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import androidx.navigation.fragment.NavHostFragment;
import butterknife.ButterKnife;
import com.google.android.gnd.repository.GndDataRepository;
import com.google.android.gnd.rx.RxErrors;
import com.google.android.gnd.service.DataService;
import com.google.android.gnd.system.PermissionsManager;
import com.google.android.gnd.system.PermissionsManager.PermissionsRequest;
import com.google.android.gnd.system.SettingsManager;
import com.google.android.gnd.system.SettingsManager.SettingsChangeRequest;
import com.google.android.gnd.ui.common.GndViewModelFactory;
import com.google.android.gnd.vo.Record;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.plugins.RxJavaPlugins;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Inject GndViewModelFactory viewModelFactory;

  @Inject PermissionsManager permissionsManager;

  @Inject SettingsManager settingsManager;

  @Inject DataService dataService;

  @Inject GndDataRepository model;
  @Inject
  DispatchingAndroidInjector<Fragment> fragmentInjector;

  private NavHostFragment navHostFragment;

  private MainViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // Prevent RxJava from force-quitting when multiple Completables terminate with onError.
    RxJavaPlugins.setErrorHandler(t -> RxErrors.logEnhancedStackTrace(t));

    super.onCreate(savedInstanceState);

    AndroidInjection.inject(this);

    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    navHostFragment =
      (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

    ViewCompat.setOnApplyWindowInsetsListener(
      getWindow().getDecorView().getRootView(), viewModel::onApplyWindowInsets);

    permissionsManager
      .getPermissionsRequests()
      .as(autoDisposable(this))
      .subscribe(this::onPermissionsRequest);

    settingsManager
      .getSettingsChangeRequests()
      .as(autoDisposable(this))
      .subscribe(this::onSettingsChangeRequest);

    viewModel.getViewState().observe(this, this::setViewState);
  }

  private void setViewState(MainViewModel.MainViewState state) {
    switch (state.getView()) {
      case MAP:
        break;
      case PLACE_SHEET:
        break;
      case VIEW_RECORD:
        showViewRecordFragment(state.getRecord());
        break;
    }
  }

  private void showViewRecordFragment(Record record) {
    // TODO: Pass data in and show.
    navHostFragment.getNavController().navigate(R.id.view_record_fragment);
  }

  private void onPermissionsRequest(PermissionsRequest permissionsRequest) {
    Log.d(TAG, "Sending permissions request to system");
    ActivityCompat.requestPermissions(
        this, permissionsRequest.getPermissions(), permissionsRequest.getRequestCode());
  }

  private void onSettingsChangeRequest(SettingsChangeRequest settingsChangeRequest) {
    try {
      // The result of this call is received by {@link #onActivityResult}.
      Log.d(TAG, "Sending settings resolution request");
      settingsChangeRequest
          .getException()
          .startResolutionForResult(this, settingsChangeRequest.getRequestCode());
    } catch (SendIntentException e) {
      // TODO: Report error.
      Log.e(TAG, e.toString());
    }
  }

  /**
   * The Android permissions API requires this callback to live in an Activity; here we dispatch the
   * result back to the PermissionManager for handling.
   */
  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    Log.d(TAG, "Permission result received");
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  /**
   * The Android settings API requires this callback to live in an Activity; here we dispatch the
   * result back to the SettingsManager for handling.
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    settingsManager.onActivityResult(requestCode, resultCode);
  }

  @Override
  public final AndroidInjector<Fragment> supportFragmentInjector() {
    return fragmentInjector;
  }
}

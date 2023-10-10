/*
 * Copyright 2023 The Android Open Source Project
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

package com.sewon.officehealth.screen.device.components

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState


/**
 * A variation of PermissionScreen that takes a list of permissions and only calls [onGranted] when
 * all the requiredPermissions are granted.
 *
 * By default it assumes that all [permissions] are required.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
  modifier: Modifier = Modifier,
  permissions: List<String>,
  requiredPermissions: List<String> = permissions,
  description: String? = null,
  contentAlignment: Alignment = Alignment.TopStart,
  onGranted: @Composable BoxScope.(List<String>) -> Unit,
) {
  val context = LocalContext.current
  var errorText by remember {
    mutableStateOf("")
  }

  val permissionState = rememberMultiplePermissionsState(permissions = permissions) { map ->
    val rejectedPermissions = map.filterValues { !it }.keys
    errorText = if (rejectedPermissions.none { it in requiredPermissions }) {
      ""
    } else {
      "${rejectedPermissions.joinToString()} required for the sample"
    }
  }
  val allRequiredPermissionsGranted =
    permissionState.revokedPermissions.none { it.permission in requiredPermissions }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .then(modifier),
    contentAlignment = if (allRequiredPermissionsGranted) {
      contentAlignment
    } else {
      Alignment.Center
    },
  ) {
    if (allRequiredPermissionsGranted) {
      onGranted(
        permissionState.permissions
          .filter { it.status.isGranted }
          .map { it.permission },
      )
    } else {
      PermissionRequestView(
        permissionState,
        description,
        errorText,
      )

      FloatingActionButton(
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(16.dp),
        onClick = {
          val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse("package:${context.packageName}")
          }
          context.startActivity(intent)
        },
      ) {
        Icon(imageVector = Icons.Rounded.Settings, contentDescription = "App settings")
      }
    }
  }
}

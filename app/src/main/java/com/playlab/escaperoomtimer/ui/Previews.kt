package com.playlab.escaperoomtimer.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Nexus 10",
    group = "Devices",
    device = Devices.NEXUS_10,
    uiMode = UI_MODE_NIGHT_YES,
    showSystemUi = true
)
@Preview(
    name = "Pixel 2",
    group = "Devices",
    device = Devices.PIXEL_2,
    uiMode = UI_MODE_NIGHT_YES,
    fontScale = 0.9f,
    showSystemUi = true
)
annotation class DevicesPreviews
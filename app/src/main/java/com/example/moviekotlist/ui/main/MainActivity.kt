package com.example.moviekotlist.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.moviekotlist.R
import com.example.moviekotlist.advanced.RateReminderWorker
import java.util.concurrent.TimeUnit
import android.widget.Button


class MainActivity : AppCompatActivity() {


    private val requestNotifPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted: Boolean ->
            if (granted) {
                scheduleRateReminder()
            } else {
                showNotificationsDeniedDialog()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Use the new flow only
        ensureNotificationPermissionThenSchedule()
    }

    private fun ensureNotificationPermissionThenSchedule() {
        // Android 12 and below: no runtime permission needed
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            scheduleRateReminder()
            return
        }

        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            scheduleRateReminder()
        } else {
            showNotificationsRationaleDialog()
        }
    }

    private fun showNotificationsRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Enable notifications?")
            .setMessage("We use notifications to remind you to rate movies you added without a rating.")
            .setPositiveButton("Allow") { _, _ ->
                // we only call it on API 33+
                requestNotifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton("Not now", null)
            .show()
    }

    private fun showNotificationsDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Notifications are off")
            .setMessage("To get rating reminders, enable notifications in Settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun scheduleRateReminder() {
        WorkManager.getInstance(this).cancelUniqueWork("rate_reminder_work")

        val request = PeriodicWorkRequestBuilder<RateReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "rate_reminder_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }


}

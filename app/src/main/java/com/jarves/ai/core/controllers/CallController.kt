package com.jarves.ai.core.controllers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract

class CallController(private val context: Context) {

    fun makeCall(contactNameOrNumber: String): String {
        val phoneNumber = resolveContactNumber(contactNameOrNumber) ?: contactNameOrNumber

        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneNumber")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        return try {
            context.startActivity(intent)
            "Calling $contactNameOrNumber..."
        } catch (e: SecurityException) {
            // Fallback to dialer if CALL_PHONE permission is missing
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(dialIntent)
            "Opening dialer for $contactNameOrNumber"
        } catch (e: Exception) {
            "Failed to place call: ${e.localizedMessage}"
        }
    }

    private fun resolveContactNumber(name: String): String? {
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?",
            arrayOf("%$name%"),
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                if (numberIndex != -1) {
                    return it.getString(numberIndex)
                }
            }
        }
        return null
    }
}

package com.example.voicerecorder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;

public class Sharing {
    public static void shareFile(Context context, File file)
    {
        Uri uri = FileProvider.getUriForFile(context, "com.example.voicerecorder.fileprovider", file);
        Intent shareIntent = new Intent();

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("audio/*");
        context.startActivity(Intent.createChooser(shareIntent, "Share..."));
    }
}
package com.example.demoapp.utils.extension

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.webkit.URLUtil
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.demoapp.R
import java.io.File
import java.io.FileOutputStream
import java.net.URLConnection
import java.text.DecimalFormat

const val MEDIA_TYPE_IMAGE = 1


fun Context.getOutputMediaFileUri(type: Int): Uri? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return FileProvider.getUriForFile(
            this,
            this.applicationContext.packageName + ".provider",
            getOutputMediaFile(type)!!
        )
    }

    try {
        return Uri.fromFile(getOutputMediaFile(type))
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

}

private fun getOutputMediaFile(type: Int): File? {
    // External sdcard location
    val mediaStorageDir =
        File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "demo_Profile"
        )
    if (!mediaStorageDir.exists()) {
        if (!mediaStorageDir.mkdirs()) {
            return null
        }
    }
    val mediaFile: File
    if (type == MEDIA_TYPE_IMAGE) {
        mediaFile =
            File(mediaStorageDir.path + File.separator + System.currentTimeMillis() + ".jpeg")
    } else {
        return null
    }
    return mediaFile
}


private fun Context.saveImageToSDCard(bitmap: Bitmap, IMAGE_CAPTURE_URI: Uri): File? {
    try {
        val current: String = File(IMAGE_CAPTURE_URI.path!!).name
        val file: File
        /* File myNewFolder = new File(IMAGE_CAPTURE_URI.getPath());
        if (!myNewFolder.isDirectory())
            myNewFolder.mkdir();*/

        val segments = IMAGE_CAPTURE_URI.path!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val idStr = segments[segments.size - 1]

        file = File(IMAGE_CAPTURE_URI.path!!.replace(current, ""), current)

        if (file.exists())
            file.delete()

        val fOut = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
        fOut.flush()
        fOut.close()

        /* final File tempFile = file;
        MediaScannerConnection.scanFile(context, new String[]{Uri.fromFile(file).getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String newFilepath, Uri uri) {
                //tempFile = new File(uri.getPath());
            }
        });*/
        this.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
        return file
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

}

fun isImageFromUri(path: String): Boolean {

    return if (TextUtils.isEmpty(path)) false else path.contains(".jpg") ||
            path.contains(".jpeg") ||
            path.contains(".png") ||
            path.contains(".JPG") ||
            path.contains(".PNG") ||
            path.contains(".JPEG") ||
            path.contains("gif")
}

fun isImageFile(path: String): Boolean {
    val mimeType = URLConnection.guessContentTypeFromName(path)
    return mimeType != null && mimeType.indexOf("image") == 0
}

fun isVideoFile(path: String): Boolean {
    val mimeType = URLConnection.guessContentTypeFromName(path)
    return mimeType != null && mimeType.indexOf("video") == 0
}

fun getMediaPathFromSDCard(filePath: String, folderPath: String): String {
    val file = File(filePath)
    val path = File(folderPath)
    if (!path.exists()) {
        path.mkdirs()
    }
    val mediaPath: String
    if (file.exists()) {
        mediaPath = file.absolutePath
    } else {
        mediaPath = ""
    }
    return mediaPath
}

fun getFileNameFromUrl(fileUrl: String): String {

    var fileName = ""
    if (fileUrl.isEmpty())
        return fileUrl

    val lastFileName: String = URLUtil.guessFileName(fileUrl, null, null)
    val lastData = lastFileName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    if (lastData.size > 2 && !TextUtils.isEmpty(lastData[1])) {
        fileName = lastData[1]
    }
    return fileName
}

fun getFileSize(size: Long): String {
    if (size <= 0) return "0"
    val units = arrayOf("B", "kB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(
        size / Math.pow(
            1024.0,
            digitGroups.toDouble()
        )
    ) + WHITE_SPACE + units[digitGroups]
}

/**
 * Method to Load string url in load.
 * Show place holder until image is ready to display
 * Shoe image place holder if image loading fails
 * set High priority to load and display image
 *
 * @param url string from City model class
 */
fun Context.loadImageInGlide(
    url: String,
    ivImage: AppCompatImageView,
    placeHolder: Int = 0,
    error: Int = 0
) {
    val requestOptions = RequestOptions()
        .fitCenter()
        .placeholder(placeHolder)
        .error(error)
        .priority(Priority.LOW)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

    Glide.with(this)
        .load(url)
        .apply(requestOptions)
        .into(ivImage)
}

/**
 * Method to Load file path in load.
 * Show place holder until image is ready to display
 * Shoe image place holder if image loading fails
 * set High priority to load and display image
 *
 * @param url string from City model class
 */
fun Context.loadFileImageInGlide(
    url: File,
    ivImage: AppCompatImageView,
    placeHolder: Int = 0,
    error: Int = 0
) {
    val requestOptions = RequestOptions()
        .fitCenter()
        .placeholder(placeHolder)
        .error(error)
        .priority(Priority.LOW)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

    Glide.with(this)
        .load(url)
        .apply(requestOptions)
        .into(ivImage)
}

/**
 * Method to Load string url in load.
 * Show place holder until image is ready to display
 * Shoe image place holder if image loading fails
 * set High priority to load and display image
 *
 * @param url string from City model class
 *//*

fun Context.loadCircleImageInGlide(
    url: String,
    ivImage: CircleImageView,
    placeHolder: Int = 0,
    error: Int = 0
) {
    val requestOptions = RequestOptions()
        .fitCenter()
        .placeholder(placeHolder)
        .error(error)
        .priority(Priority.LOW)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

    Glide.with(this)
        .load(url)
        .apply(requestOptions)
        .into(ivImage)
}
*/


fun Context.loadResourcesInGlide(resources: Int, ivImage: AppCompatImageView, placeHolder: Int) {
    val requestOptions = RequestOptions()
        .centerInside()
        .placeholder(placeHolder)

    Glide.with(this)
        .load(resources)
        .apply(requestOptions)
        .into(ivImage)
}


/* Get uri related content real local file path. */
fun Context.getPath(uri: Uri): String? {
    var ret: String?
    ret = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Android OS above sdk version 19.
            getUriRealPathAboveKitkat(uri)
        } else {
            // Android OS below sdk version 19
            getRealPath(contentResolver, uri, null)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.d("DREG", "FilePath Catch: $e")
        getFilePathFromURI(uri)
    }

    return ret
}

private fun Context.getFilePathFromURI(contentUri: Uri): String? {
    //copy file and send new file path
    val fileName = getFileName(contentUri)
    if (!TextUtils.isEmpty(fileName)) {
        val TEMP_DIR_PATH = Environment.getExternalStorageDirectory().path
        val copyFile = File(TEMP_DIR_PATH + File.separator + fileName)
        Log.d("DREG", "FilePath copyFile: $copyFile")
        copy(contentUri, copyFile)
        return copyFile.absolutePath
    }
    return null
}

fun getFileName(uri: Uri?): String? {
    if (uri == null) return null
    var fileName: String? = null
    val path = uri.path
    val cut = path!!.lastIndexOf('/')
    if (cut != -1) {
        fileName = path.substring(cut + 1)
    }
    return fileName
}

fun Context.copy(srcUri: Uri, dstFile: File) {
    try {
        val inputStream = contentResolver.openInputStream(srcUri) ?: return
        val outputStream = FileOutputStream(dstFile)
        //     IOUtils.copyStream(inputStream, outputStream) // org.apache.commons.io
        inputStream.close()
        outputStream.close()
    } catch (e: Exception) { // IOException
        e.printStackTrace()
    }

}

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
private fun Context.getUriRealPathAboveKitkat(uri: Uri?): String? {
    var ret: String? = ""

    if (uri != null) {
        //  content://media/external/images/media/392
        if (isContentUri(uri)) {
            if (isGooglePhotoDoc(uri.authority)) {
                ret = uri.lastPathSegment
            } else {
                ret = getRealPath(contentResolver, uri, null)
            }
        } else if (isFileUri(uri)) {
            ret = uri.path
        } else if (isDocumentUri(uri)) {

            // Get uri related document id.
            val documentId = DocumentsContract.getDocumentId(uri)

            // Get uri authority.
            val uriAuthority = uri.authority

            if (isMediaDoc(uriAuthority)) {
                val idArr =
                    documentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (idArr.size == 2) {
                    // First item is document type.
                    val docType = idArr[0]

                    // Second item is document real id.
                    val realDocId = idArr[1]

                    // Get content uri by document type.
                    var mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    if ("image" == docType) {
                        mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == docType) {
                        mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == docType) {
                        mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    // Get where clause with real document id.
                    val whereClause = MediaStore.Images.Media._ID + " = " + realDocId

                    ret = getRealPath(contentResolver, mediaContentUri, whereClause)
                }

            } else if (isDownloadDoc(uriAuthority)) {
                // Build download uri.
                val downloadUri = Uri.parse("content://downloads/public_downloads")

                // Append download document id at uri end.
                val downloadUriAppendId =
                    ContentUris.withAppendedId(downloadUri, java.lang.Long.valueOf(documentId))

                ret = getRealPath(contentResolver, downloadUriAppendId, null)

            } else if (isExternalStoreDoc(uriAuthority)) {
                val idArr =
                    documentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (idArr.size == 2) {
                    val type = idArr[0]
                    val realDocId = idArr[1]

                    if ("primary".equals(type, ignoreCase = true)) {
                        ret = Environment.getExternalStorageDirectory().toString() + "/" + realDocId
                    }
                }
            }
        }
    }

    return ret
}

/* Check whether this uri represent a document or not. */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
private fun Context.isDocumentUri(uri: Uri?): Boolean {
    var ret = false
    if (uri != null) {
        ret = DocumentsContract.isDocumentUri(this, uri)
    }
    return ret
}

/* Check whether this uri is a content uri or not.
     *  content uri like content://media/external/images/media/1302716
     *  */
private fun isContentUri(uri: Uri?): Boolean {
    var ret = false
    if (uri != null) {
        val uriSchema = uri.scheme
        if ("content".equals(uriSchema!!, ignoreCase = true)) {
            ret = true
        }
    }
    return ret
}

/* Check whether this uri is a file uri or not.
     *  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
     * */
private fun isFileUri(uri: Uri?): Boolean {
    var ret = false
    if (uri != null) {
        val uriSchema = uri.scheme
        if ("file".equals(uriSchema!!, ignoreCase = true)) {
            ret = true
        }
    }
    return ret
}

/* Check whether this document is provided by ExternalStorageProvider. */
private fun isExternalStoreDoc(uriAuthority: String?): Boolean {
    var ret = false

    if ("com.android.externalstorage.documents" == uriAuthority) {
        ret = true
    }

    return ret
}

/* Check whether this document is provided by DownloadsProvider. */
private fun isDownloadDoc(uriAuthority: String?): Boolean {
    var ret = false

    if ("com.android.providers.downloads.documents" == uriAuthority) {
        ret = true
    }

    return ret
}

/* Check whether this document is provided by MediaProvider. */
private fun isMediaDoc(uriAuthority: String?): Boolean {
    var ret = false

    if ("com.android.providers.media.documents" == uriAuthority) {
        ret = true
    }

    return ret
}

/* Check whether this document is provided by google photos. */
private fun isGooglePhotoDoc(uriAuthority: String?): Boolean {
    var ret = false

    if ("com.google.android.apps.photos.content" == uriAuthority) {
        ret = true
    }

    return ret
}

/* Return uri represented document file real local path.*/
@SuppressLint("Recycle")
private fun getRealPath(contentResolver: ContentResolver, uri: Uri, whereClause: String?): String {
    var ret = ""

    // Query the uri with condition.
    val cursor = contentResolver.query(uri, null, whereClause, null, null)

    if (cursor != null) {
        val moveToFirst = cursor.moveToFirst()
        if (moveToFirst) {

            // Get columns name by uri type.
            var columnName = MediaStore.Images.Media.DATA

            if (uri === MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                columnName = MediaStore.Images.Media.DATA
            } else if (uri === MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                columnName = MediaStore.Audio.Media.DATA
            } else if (uri === MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                columnName = MediaStore.Video.Media.DATA
            }

            // Get column index.
            val columnIndex = cursor.getColumnIndex(columnName)

            // Get column value which is the uri related file local path.
            ret = cursor.getString(columnIndex)
        }
    }

    return ret
}

fun toUri(file: File, context: Context): Uri {
    return FileProvider.getUriForFile(
        context,
        context.packageName + context.getString(R.string.about_us),
        file
    )
}

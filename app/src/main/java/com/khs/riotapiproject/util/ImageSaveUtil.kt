package com.khs.riotapiproject.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.ConnectException
import java.net.URL

class ImageSaveUtil(val context: Context) {

    fun imageToCache(nameOrID: String, url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            // 최신버전 정보는 https://ddragon.leagueoflegends.com/api/versions.json 에서 확인가능.
            val iconURL =  "${url}${nameOrID}.png"
            try {
                val inputStream = URL(iconURL).openStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                imgToCacheAndGetURI(nameOrID, bitmap)
            } catch (e: ConnectException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun imgToCacheAndGetURI(nameOrID: String, bitmap: Bitmap): Uri? {
        val fileName = "${nameOrID}.png"
        val cachePath = "${context.cacheDir}/file"
        val dir = File(cachePath)

        if(dir.exists().not()) {
            dir.mkdirs() // 폴더 없을경우 폴더 생성
        }

        val fileItem = File("$dir/$fileName")

        //존재하지 않은 Icon일 때만 생성.
        try {
            fileItem.createNewFile()
            //0KB 파일 생성.

            val fos = FileOutputStream(fileItem) // 파일 아웃풋 스트림

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            //파일 아웃풋 스트림 객체를 통해서 Bitmap 압축.

            fos.close() // 파일 아웃풋 스트림 객체 close

            MediaScannerConnection.scanFile(context.applicationContext, arrayOf(fileItem.toString()), null, null)

            //sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fileItem)))
            // 브로드캐스트 수신자에게 파일 미디어 스캔 액션 요청. 그리고 데이터로 추가된 파일에 Uri를 넘겨준다. - Deprecated 위 코드로 수정
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return FileProvider.getUriForFile(context.applicationContext, "com.khs.riotapiproject.fileprovider", fileItem)
    }

    fun checkAlreadySaved(nameOrID: String): Boolean {
        val fileName = "${nameOrID}.png"
        val cachePath = "${context.cacheDir}/file"
        val dir = File(cachePath)

        if(dir.exists().not()) {
            return false
        }

        val fileItem = File("$dir/$fileName")
        if(fileItem.exists()) {
            return true
        }

        return false
    }
}
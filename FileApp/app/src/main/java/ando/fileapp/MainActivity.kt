package ando.fileapp

import ando.file.core.FileGlobal.OVER_LIMIT_EXCEPT_OVERFLOW
import ando.file.core.FileOperator
import ando.file.core.FileUtils
import ando.file.selector.FileSelectCallBack
import ando.file.selector.FileSelectCondition
import ando.file.selector.FileSelectOptions
import ando.file.selector.FileSelectResult
import ando.file.selector.FileSelector
import ando.file.selector.FileType
import ando.file.selector.IFileType
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var mBtChooseFile: Button
    private lateinit var mTvResult: TextView
    private var mFileSelector: FileSelector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTvResult = findViewById(R.id.tvResult)
        mBtChooseFile = findViewById(R.id.btChooseFile)
        mBtChooseFile.noShake {
            PermissionManager.requestStoragePermission(this) {
                if (it) chooseFile()
            }
        }
    }

    //v3.0.0 开始支持 ActivityResultLauncher 跳转页面
    private val mStartForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            mFileSelector?.obtainResult(12, result.resultCode, result.data)
        }

    override fun onDestroy() {
        mStartForResult.unregister()
        super.onDestroy()
    }

    private fun chooseFile() {
        val optionsImage = FileSelectOptions().apply {
            fileType = FileType.IMAGE
            fileTypeMismatchTip = "File type mismatch !"
            singleFileMaxSize = 5242880
            singleFileMaxSizeTip = "The largest picture does not exceed 5M !"
            allFilesMaxSize = 104857600
            //单选条件下无效,只做单个图片大小判断
            //EN:Invalid under single selection conditions, only single image size judgment
            allFilesMaxSizeTip = "The total picture size does not exceed 100M !"

            fileCondition = object : FileSelectCondition {
                override fun accept(fileType: IFileType, uri: Uri?): Boolean {
                    //Filter out gif
                    return (fileType == FileType.IMAGE && uri != null && !uri.path.isNullOrBlank() && !FileUtils.isGif(uri))
                }
            }
        }

        mFileSelector = FileSelector
            .with(this, launcher = mStartForResult)
            .setRequestCode(12)
            .setTypeMismatchTip("File type mismatch !")
            .setMinCount(1, "Choose at least one file !")
            .setMaxCount(10, "Choose up to ten files !")//单选条件下无效, 只做最少数量判断 Invalid under single selection condition, only judge the minimum number
            .setOverLimitStrategy(OVER_LIMIT_EXCEPT_OVERFLOW)
            .setSingleFileMaxSize(10485760, "The size cannot exceed 10M !")//单选条件下无效, 使用 FileSelectOptions.singleFileMaxSize
            .setAllFilesMaxSize(104857600, "Total size cannot exceed 100M !")//单选条件下无效, 只做单个图片大小判断 setSingleFileMaxSize
            .setExtraMimeTypes("image/*")
            .applyOptions(optionsImage)

            //优先使用 FileSelectOptions 中设置的 FileSelectCondition
            .filter(object : FileSelectCondition {
                override fun accept(fileType: IFileType, uri: Uri?): Boolean {
                    return when (fileType) {
                        FileType.IMAGE -> (uri != null && !uri.path.isNullOrBlank() && !FileUtils.isGif(uri))
                        FileType.VIDEO -> false
                        FileType.AUDIO -> false
                        else -> false
                    }
                }
            })
            .callback(object : FileSelectCallBack {
                override fun onSuccess(results: List<FileSelectResult>?) {
                    // ResultUtils.resetUI(mTvResult)
                    if (results.isNullOrEmpty()) {
                        toastLong("No file selected")
                        return
                    }
                    // showSelectResult(results)
                    mTvResult.text = results[0].toString()
                }

                override fun onError(e: Throwable?) {
                    // FileLogger.e("FileSelectCallBack onError ${e?.message}")
                    //ResultUtils.setErrorText(mTvError, e)
                    toastLong("Error: ${e?.message}")
                }
            })
            .choose()
    }
}
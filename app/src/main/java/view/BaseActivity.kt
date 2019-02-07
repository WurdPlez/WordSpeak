package view

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import util.CommonUtil


abstract class BaseActivity : AppCompatActivity() {

    private var mProgressDialog: ProgressDialog? = null

    protected fun showLoading() {
        hideProgress()
        mProgressDialog = CommonUtil.showLoadingDialog(this)
    }

    protected fun hideProgress() {
       mProgressDialog?.let { if (it.isShowing) it.cancel() }
    }
}
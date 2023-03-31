package com.umc.personal.ui.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.amazonaws.regions.Regions
import com.umc.personal.API
import com.umc.personal.R
import com.umc.personal.data.dto.home.post.OpenGraphDto
import com.umc.personal.data.dto.home.post.ProjectUploadDto
import com.umc.personal.databinding.ActivityUploadBinding
import com.umc.personal.databinding.ActivityUploadLinkDialogBinding
import com.umc.personal.ui.adapter.image.ImageUploadAdapter
import com.umc.personal.ui.viewmodel.UploadViewModel
import com.umc.personal.util.BlackToast
import com.umc.personal.util.CrawlingTask
import com.umc.personal.util.S3Util
import com.umc.personal.util.Utils
import com.umc.personal.util.Utils.categoryMapReverse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private val viewModel by viewModels<UploadViewModel>()

    /**Open graph Manger*/
    private lateinit var manager: InputMethodManager

    //다이얼로그 초기화
    private lateinit var linkString : String
    private lateinit var linkDialogBinding : ActivityUploadLinkDialogBinding
    private lateinit var linkButton : ImageButton
    private lateinit var linkEraseButton : ImageButton
    private lateinit var opengraphText : TextView
    private lateinit var opengraphUrl : TextView
    private lateinit var opengraphImage : ImageView
    private lateinit var opengraphId : ConstraintLayout
    private lateinit var linkDialog: Dialog
    private lateinit var dialogCancelButton : Button
    private lateinit var dialogConfirmButton : Button
    private lateinit var linkDialogEditText : EditText

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Open Graph manager 초기화
        manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        //opengraph 초기화
        binding.openGraph.isVisible = false

        //카테고리 선택
        select_category()

        //이미지 선택시 실행되는 메서드
        observe_pic()

        //이미지 선택 이벤트
        image_upload_event()

        //move to back home
        back_to_home()

        //link observer
        link_observe()

        //opengraph observer
        opengraph_observe()

        //제출 버튼 클릭 이벤트 후 approval fragment 로 이동
        upload_item()

        //링크 첨부 다이얼로그
        linkButton = binding.uploadLinkBtn
        linkButton.setOnClickListener{
            showLinkDialog()
        }
    }

    /**
     * Uplaod
     * */
    private fun upload_item() {
        binding.uploadSubmitBtn.setOnClickListener {

            var uploadFile = ProjectUploadDto(title = binding.uploadTitleEt.text.toString()
                , content = binding.uploadContentEt.text.toString())

            if (viewModel.category.value != 2) {
                uploadFile.category = viewModel.category.value

                CoroutineScope(Dispatchers.IO).launch {
                    //링크가 있을 경우
                    if (viewModel.opengraph.value != null) {
                        uploadFile.linkTitle = viewModel.opengraph.value!!.title
                        uploadFile.linkUrl = viewModel.opengraph.value!!.url
                        uploadFile.linkImage = viewModel.opengraph.value!!.image
                    }
                    //사진이 있을 경우
                    if (viewModel.pic.value != null) {
                        uploadFile.image = viewModel.image.value
                        S3_connect()
                    }
                    viewModel.post_project(uploadFile)
                }
                Handler(Looper.myLooper()!!).postDelayed({
                    finish()
                }, 700)
            } else {
                BlackToast.createToast(this, "부서를 선택해주세요").show()
            }
        }
    }

    /**
     * Category
     * */
    private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
    }

    fun Spinner.avoidDropdownFocus() {
        val listPopup = Spinner::class.java
            .getDeclaredField("mPopup")
            .apply { isAccessible = true }
            .get(this)
        if (listPopup is ListPopupWindow) {
            val popup = ListPopupWindow::class.java
                .getDeclaredField("mPopup")
                .apply { isAccessible = true }
                .get(listPopup)
            if (popup is PopupWindow) {
                popup.isFocusable = false
                popup.height = 100
            }
        }
    }

    private fun select_category() {
        var departments = categoryMapReverse.keys
        val adapter = object : ArrayAdapter<String>(this, R.layout.item_upload_spinner) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                if (position == count) {
                    //마지막 포지션의 textView 를 힌트 용으로 사용합니다.
                    (v.findViewById<View>(R.id.tvItemSpinner) as TextView).text = ""
                    //아이템의 마지막 값을 불러와 hint로 추가해 줍니다.
                    (v.findViewById<View>(R.id.tvItemSpinner) as TextView).hint = getItem(count)
                }
                return v
            }
            override fun getCount(): Int {
                //마지막 아이템은 힌트용으로만 사용하기 때문에 getCount에 1을 빼줍니다.
                return super.getCount() - 1
            }
        }
        adapter.addAll(departments.toMutableList())
        adapter.add("카테고리를 선택해주세요.")
        val newsSourceSpinner = findViewById<View>(R.id.upload_department_spinner) as Spinner
        val popup = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        // Get private mPopup member variable and try cast to ListPopupWindow
        val popupWindow = popup[newsSourceSpinner] as ListPopupWindow
        // set popup height
        popupWindow.height = 250
        binding.uploadDepartmentSpinner.avoidDropdownFocus()
        binding.uploadDepartmentSpinner.adapter = adapter
        binding.uploadDepartmentSpinner.setSelection(adapter.count)
        binding.uploadDepartmentSpinner.dropDownVerticalOffset = dipToPixels(40f).toInt()
        binding.uploadDepartmentSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.setCategory(position)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    /**
     * move
     * */
    private fun back_to_home() {
        binding.backToApproval.setOnClickListener {
            finish()
        }
    }

    /**
     * link
     * */
    private fun showLinkDialog() {
        linkDialog = Dialog(this)
        linkDialogBinding = ActivityUploadLinkDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(linkDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)

        //Dialog 초기화
        dialogCancelButton = linkDialogBinding.uploadLinkDialogCancelButton
        dialogConfirmButton = linkDialogBinding.uploadLinkDialogConfirmButton
        linkDialogEditText = linkDialogBinding.uploadLinkDialogEt
        linkEraseButton = linkDialogBinding.uploadLinkEraseBtn
        opengraphText = linkDialogBinding.openGraphText
        opengraphUrl = linkDialogBinding.openGraphUrl
        opengraphImage = linkDialogBinding.openGraphImage
        opengraphId = linkDialogBinding.openGraph

        //Dialog Opengraph 초기화
        opengraphId.isVisible = false

        linkString = ""

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialogEditText.setText(linkString) //초기화
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener {
            linkString = linkDialogEditText.text.toString()
            linkDialog.dismiss()
        }

        linkEraseButton.setOnClickListener{
            linkString = ""
            linkDialogEditText.setText(linkString)
        }

        /*링크 첨부 다이얼로그*/
        linkButton = binding.uploadLinkBtn
        linkButton.setOnClickListener{
            linkDialog.show()
        }

        linkDialogEditText.setText(linkString)

        /*link url 바뀔때 마다 적용*/
        editLinkUrl()
    }

    private fun opengraph_observe() {
        viewModel.opengraph.observe(this) {
            opengraphId.isVisible = true
            opengraphText.setText(it.title)
            opengraphUrl.setText(it.url)
            opengraphImage.load(it.image)

            binding.openGraph.isVisible = true
            binding.uploadLinkTv.isVisible = false
            binding.openGraphText.setText(it.title)
            binding.openGraphUrl.setText(it.url)
            binding.openGraphImage.load(it.image)
        }
    }

    private fun link_observe() {
        viewModel.link.observe(this) {

            //link 변경시 opengraph 초기화
            opengraphId.isVisible = false
            binding.openGraph.isVisible = false
            binding.uploadLinkTv.isVisible = true

            manager.hideSoftInputFromWindow(
                currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            var openGraphDto = OpenGraphDto("", "", "")

            CoroutineScope(Dispatchers.IO).launch {
                val elements = CrawlingTask.getElements(it)
                elements?.let {
                    it.forEach { el ->
                        when (el.attr("property")) {
                            "og:url" -> {
                                el.attr("content")?.let { content ->
                                    openGraphDto.url = content
                                }
                            }
                            "og:title" -> {
                                el.attr("content")?.let { content ->
                                    openGraphDto.title = content
                                }
                            }
                            "og:image" -> {
                                el.attr("content")?.let { content ->
                                    openGraphDto.image = content
                                }
                            }
                        }
                    }
                }
                if (openGraphDto.title.toString() != "") {
                    if (openGraphDto.url.toString() == "") {
                        openGraphDto.url = viewModel.link.value.toString()
                        viewModel.setOpengraph(openGraphDto)
                    } else {
                        viewModel.setOpengraph(openGraphDto)
                    }
                }
            }
        }
    }

    /**
     * Camera
     * */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun image_upload_event() {
        binding.uploadImageBtn.setOnClickListener {
            when {
                // 갤러리 접근 권한이 있는 경우
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> showGallery(this)

                // 갤러리 접근 권한이 없는 경우 && 교육용 팝업을 보여줘야 하는 경우
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                -> showPermissionContextPopup()

                // 권한 요청 하기
                else -> requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Utils.PICK_IMAGE_FROM_GALLERY_PERMISSION
                )
            }
        }
    }

    //이미지 옵저버 패턴
    private fun observe_pic() {
        viewModel.pic.observe(this) {
            val imageRVAdapter = ImageUploadAdapter(listOf(it))
            binding.uploadItem.adapter = imageRVAdapter
            binding.uploadItem.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            val random = UUID.randomUUID().toString()
            val image = "https://approval-please.s3.ap-northeast-2.amazonaws.com/" + random
            viewModel.setImages(image)
        }
    }

    //갤러리 보여주기 함수
    private fun showGallery(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        activity.startActivityForResult(intent, Utils.PICK_IMAGE_FROM_GALLERY)
    }

    //권한이 없을때 권한 등록 팝업 함수
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Utils.PICK_IMAGE_FROM_GALLERY_PERMISSION
                )
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    //사진 선택(갤러리에서 나온) 이후 실행되는 함수
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Utils.PICK_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            data?.let { it ->
                if (it.clipData != null) {   // 사진을 여러개 선택한 경우
                    val count = it.clipData!!.itemCount
                    if (count > 1) {
                        BlackToast.createToast(this, "사진은 1장까지 선택 가능합니다.").show()
                        return
                    }
                    val imageUri = it.clipData!!.getItemAt(0).uri
                    viewModel.setImage(imageUri)
                } else {      // 1장 선택한 경우
                    val imageUri = it.data!!
                    viewModel.setImage(imageUri)
                }
            }
        }
    }

    //권한 요청 승인 이후 실행되는 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            Utils.PICK_IMAGE_FROM_GALLERY_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    showGallery(this)
                else
                    BlackToast.createToast(this, "권한을 거부하셨습니다.").show()
            }
        }
    }

    /**
     * S3
     * */
    private fun S3_connect() {

        val route = viewModel.image.value!!

        val realPathFromURI = getRealPathFromURI(viewModel.pic.value!!)
        val file = File(realPathFromURI)

        S3Util().getInstance()
            ?.setKeys(API.S3_ACCESS_KEY, API.S3_ACCESS_SECRET_KEY)
            ?.setRegion(Regions.AP_NORTHEAST_2)
            ?.uploadWithTransferUtility(
                this,
                "approval-please", file, route
            )
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val buildName = Build.MANUFACTURER
        if(buildName.equals("Xiaomi")) {
            return uri.path.toString()
        }
        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = contentResolver.query(uri, proj, null, null, null)

        if(cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        return cursor.getString(columnIndex)
    }

    /**
     * link
     * */
    private fun getUrl(url: String) : String {
        return if(url.contains("http://") || url.contains("https://")) url
        else "https://".plus(url)
    }

    private fun editLinkUrl() {
        linkDialogEditText.addTextChangedListener { text: Editable? ->
            text?.let {
                var url = it.toString()
                viewModel.setLink(getUrl(url.trim()))
            }
        }
    }
}
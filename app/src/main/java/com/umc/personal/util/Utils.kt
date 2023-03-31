package com.umc.personal.util

object Utils {

    /*camera*/
    val PICK_IMAGE_FROM_GALLERY = 1000
    val PICK_IMAGE_FROM_GALLERY_PERMISSION = 1010

    /*category*/
    val categoryMap = mapOf<Int, String>(0 to "개인 프로젝트", 1 to "팀 프로젝트")
    val categoryMapReverse = mapOf<String, Int>("개인 프로젝트" to 0,"팀 프로젝트" to 1)
}
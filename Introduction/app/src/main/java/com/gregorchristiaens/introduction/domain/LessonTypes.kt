package com.gregorchristiaens.introduction.domain

enum class LessonTypes(val gear: ArrayList<String>) {
    KobudoNunchaku(arrayListOf("nunchaku")),
    KobudoSai(arrayListOf("sai")),
    KobudoTonfa(arrayListOf("tonfa")),
    KobudoBo(arrayListOf("bo")),
    Kumite(arrayListOf("boxing gloves","shin guard")),
    StandardIppan(arrayListOf(""))
}
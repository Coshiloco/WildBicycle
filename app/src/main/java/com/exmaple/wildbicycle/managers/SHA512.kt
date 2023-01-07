package com.exmaple.wildbicycle.managers

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object SHA512 {
    fun String.SHA512Hash(): String {
        return sha512(this)
    }

    private fun sha512(password: String): String {
        var sha: MessageDigest? = null
        var hash: ByteArray? = null
        try {
            sha = MessageDigest.getInstance("SHA-512")
            hash = sha.digest(password.toByteArray(charset("UTF-8")))
        } catch (e: NoSuchAlgorithmException) {
            System.err.println(e)
        } catch (e: UnsupportedEncodingException) {
            System.err.println(e)
        }
        return convertToHex(hash)
    }

    private fun convertToHex(raw: ByteArray?): String {
        val sb = StringBuffer()
        for (i in raw!!.indices) {
            sb.append(((raw[i].toInt() and 0xff) + 0x100).toString(16).substring(1))
        }
        return sb.toString()
    }
}
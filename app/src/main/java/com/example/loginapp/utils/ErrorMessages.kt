package com.example.loginapp.utils

object ErrorMessages {
    fun getJapaneseErrorMessage(errorCode: String?): String {
        return when (errorCode) {
            "ERROR_INVALID_EMAIL" -> "無効なメールアドレスです"
            "ERROR_EMAIL_ALREADY_IN_USE" -> "このメールアドレスは既に使用されています"
            "ERROR_WEAK_PASSWORD" -> "パスワードが弱すぎます"
            "ERROR_WRONG_PASSWORD" -> "パスワードが間違っています"
            "ERROR_USER_NOT_FOUND" -> "ユーザーが見つかりません"
            "ERROR_USER_DISABLED" -> "このユーザーは無効化されています"
            "ERROR_TOO_MANY_REQUESTS" -> "試行回数が多すぎます。しばらくしてから再試行してください"
            "USER_NULL" -> "ユーザー情報が取得できませんでした"
            "ERROR_EMAIL_IS_NOT_VERIFIED" -> "メールアドレスが確認されていません"
            "ERROR_EMAIL_SEND" -> "メールの送信に失敗しました"
            else -> "認証エラーが発生しました"
        }
    }
}
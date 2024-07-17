package model.utils

import com.probro.khoded.KhodedConfig

object PostgresUtils {
    val prodURI = KhodedConfig.prodUri
    val devURI = KhodedConfig.devUri
    val prodUserName = KhodedConfig.prodUsername
    val devUserName = KhodedConfig.devUsername
    val prodPassword = KhodedConfig.prodPassword
    val devPassword = KhodedConfig.devPassword
}
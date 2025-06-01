package com.elkhami.f1champions.core.logger

import org.slf4j.LoggerFactory

inline fun <reified T> T.loggerWithPrefix(prefix: String = "[DEV]"): PrefixedLogger {
    return PrefixedLogger(LoggerFactory.getLogger(T::class.java), prefix)
}

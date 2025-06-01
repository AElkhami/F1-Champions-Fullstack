package com.elkhami.f1champions.core.logger

import org.slf4j.Logger

class PrefixedLogger(
    private val logger: Logger,
    private val prefix: String,
) {
    fun info(msg: String) = logger.info("$prefix $msg")

    fun error(msg: String) = logger.error("$prefix $msg")

    fun warn(msg: String) = logger.warn("$prefix $msg")
}

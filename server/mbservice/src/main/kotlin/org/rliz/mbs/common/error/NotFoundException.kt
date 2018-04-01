package org.rliz.mbs.common.error

class NotFoundException(vararg identifiers: String) :
    RuntimeException(
        "Not found with ${identifiers.reduce { l, r -> "$l, $r" }}"
    )

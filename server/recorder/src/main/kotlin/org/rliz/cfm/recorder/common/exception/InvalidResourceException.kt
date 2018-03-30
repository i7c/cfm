package org.rliz.cfm.recorder.common.exception

import kotlin.reflect.KProperty

class InvalidResourceException : RuntimeException {

    constructor(field: KProperty<*>, problem: String = "missing") :
        super("Field ${field.name}: $problem")
}

package org.rliz.cfm.recorder.common.exception

import org.rliz.cfm.recorder.common.data.AbstractModel
import kotlin.reflect.KClass

class OutdatedException : RuntimeException {

    constructor(clazz: KClass<*> = AbstractModel::class)
            : super("$clazz(s) outdated, operation canceled")

}

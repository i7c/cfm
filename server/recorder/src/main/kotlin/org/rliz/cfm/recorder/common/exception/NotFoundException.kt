package org.rliz.cfm.recorder.common.exception

import org.rliz.cfm.recorder.common.data.AbstractModel
import kotlin.reflect.KClass

class NotFoundException : RuntimeException {

    constructor(clazz: KClass<*> = AbstractModel::class, specifier: Any = "id")
        : super("Could not find ${clazz.qualifiedName} identified by $specifier")

}
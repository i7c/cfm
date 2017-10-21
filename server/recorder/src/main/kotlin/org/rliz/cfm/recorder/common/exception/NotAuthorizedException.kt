package org.rliz.cfm.recorder.common.exception

import org.rliz.cfm.recorder.common.data.AbstractModel
import kotlin.reflect.KClass

class NotAuthorizedException : RuntimeException {

    constructor(clazz: KClass<*> = AbstractModel::class)
        : super("No authorization granted for accessing ${clazz.simpleName}")
}
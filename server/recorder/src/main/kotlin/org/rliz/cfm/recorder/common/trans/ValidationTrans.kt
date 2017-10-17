package org.rliz.cfm.recorder.common.trans

import org.rliz.cfm.recorder.common.exception.InvalidResourceException
import kotlin.reflect.KProperty0


fun <T : Any> nonNullField(accessor: KProperty0<T?>): T =
        accessor.get() ?: throw InvalidResourceException(accessor)

fun <C : Collection<E>, E : Any> nonEmptyCollection(accessor: KProperty0<C?>): C =
        nonNullField(accessor).takeIf(Collection<E>::isNotEmpty) ?: throw InvalidResourceException(accessor, "empty")

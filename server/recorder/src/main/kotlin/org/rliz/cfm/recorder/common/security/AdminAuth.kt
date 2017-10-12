package org.rliz.cfm.recorder.common.security

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION)
@Retention
@PreAuthorize("hasRole('ADMIN')")
annotation class AdminAuth

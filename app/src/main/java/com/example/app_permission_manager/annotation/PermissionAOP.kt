package com.example.app_permission_manager.annotation

import com.flyjingfish.android_aop_annotation.ProceedJoinPoint
import com.flyjingfish.android_aop_annotation.base.BasePointCut

class PermissionAOP: BasePointCut<Permission> {
    override fun invoke(joinPoint: ProceedJoinPoint, anno: Permission): Any? {
        TODO("Not yet implemented")
    }
}